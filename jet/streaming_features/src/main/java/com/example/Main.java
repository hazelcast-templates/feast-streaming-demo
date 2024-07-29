package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.aggregate.AggregateOperations;
import com.hazelcast.jet.kafka.KafkaSources;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.StreamSource;
import com.hazelcast.jet.pipeline.WindowDefinition;
import com.hazelcast.map.impl.MapEntrySimple;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static com.hazelcast.jet.pipeline.WindowDefinition.sliding;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {
    private final static long MONITORING_INTERVAL_7_DAYS = DAYS.toMillis(7);
    private final static long REPORTING_INTERVAL = SECONDS.toMillis(1);

    public static Pipeline createPipeline(String feastBaseUrl, String kafkaBaseUrl) {
        var mapper = new ObjectMapper();
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", kafkaBaseUrl);
        props.setProperty("key.deserializer", StringDeserializer.class.getCanonicalName());
        props.setProperty("value.deserializer", StringDeserializer.class.getCanonicalName());
        props.setProperty("auto.offset.reset", "earliest");

        StreamSource<Map.Entry<String, String>> kafkaSource = KafkaSources.kafka(props, "transaction");
        Pipeline pipeline = Pipeline.create();
        pipeline
                .readFrom(kafkaSource)
                .withNativeTimestamps(0)
                .map(item -> mapper.readValue(item.getValue(), Transaction.class))
                .groupingKey(Transaction::getAcct_num)
                .window(sliding(MONITORING_INTERVAL_7_DAYS, REPORTING_INTERVAL))
                .aggregate(counting())
                .map(item -> {
                    var userId = item.getKey();
                    // set the current datetime
                    var timestamp = new Date();
                    var utc = new UserTransactionCount7d(userId, item.getValue(), timestamp);
                    return (Map.Entry<String, UserTransactionCount7d>) new MapEntrySimple(userId, utc);
                })
                .map(item -> mapper.writeValueAsString(item.getValue()))
                .writeTo(FeastSinks.push(feastBaseUrl, "user_transaction_count_7d"));
        return pipeline;
    }

    public static void main(String[] args) {
        var feastBaseUrl = "http://localhost:6566";
        var kafkaBaseUrl = "localhost:9092";
        if (args.length >= 1) {
            feastBaseUrl = args[0];
        }
        if (args.length >= 2) {
            kafkaBaseUrl = args[1];
        }
        Pipeline pipeline = createPipeline(feastBaseUrl, kafkaBaseUrl);
        HazelcastInstance hz = Hazelcast.bootstrappedInstance();
        hz.getJet().newJob(pipeline);
    }
}
