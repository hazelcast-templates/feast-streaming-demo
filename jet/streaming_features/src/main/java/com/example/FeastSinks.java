package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.jet.json.JsonUtil;
import com.hazelcast.jet.pipeline.Sink;
import com.hazelcast.jet.pipeline.SinkBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeastSinks {
    public static final MediaType JSON = MediaType.get("application/json");

    private static Map<String, List<Object>> createDataFrameFrom(Object obj) throws IOException {
        var m = JsonUtil.mapFrom(obj);
        if (m == null) {
            throw new IllegalArgumentException("Cannot parse dataframe");
        }
        var m2 = new HashMap<String, List<Object>>(m.size());
        for (var entry : m.entrySet()) {
            m2.put(entry.getKey(), Collections.singletonList(entry.getValue()));
        }
        return m2;
    }

    public static Sink<Object> push(String baseUrl, String pushSourceName) {
        var mapper = new ObjectMapper();
        return SinkBuilder.sinkBuilder("feast",
            ctx -> new FeastPushConfig(baseUrl, pushSourceName, "online")
        ).receiveFn((cfg, item) -> {
            var client = new OkHttpClient();
            var payload = new PushPayload();
            payload.setPush_source_name(cfg.getPushSourceName());
            payload.setDf(createDataFrameFrom(item));
            payload.setTo(cfg.getTarget());
            var json = mapper.writeValueAsString(payload);
            System.out.printf("\n\nJSON: %s\n\n", json);
            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url(cfg.getUrl())
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.code() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + response.code());
                }
                var responseBody = response.body();
                if (responseBody != null) {
                    responseBody.close();
                }
            }
        }).build();
    }
}

