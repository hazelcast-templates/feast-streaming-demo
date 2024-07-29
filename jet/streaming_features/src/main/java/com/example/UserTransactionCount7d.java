package com.example;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Date;

public class UserTransactionCount7d {
    private final String user_id;
    private final long transaction_count_7d;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private final Date feature_timestamp;

    public UserTransactionCount7d(String user_id, long transaction_count_7d, Date feature_timestamp) {
        this.feature_timestamp = feature_timestamp;
        this.transaction_count_7d = transaction_count_7d;
        this.user_id = user_id;
    }

    public Date getFeature_timestamp() {
        return feature_timestamp;
    }

    public long getTransaction_count_7d() {
        return transaction_count_7d;
    }

    public String getUser_id() {
        return user_id;
    }
}
