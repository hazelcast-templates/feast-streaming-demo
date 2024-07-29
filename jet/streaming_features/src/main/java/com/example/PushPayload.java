package com.example;

import java.util.List;
import java.util.Map;

public class PushPayload {
    private String push_source_name;
    private Map<String, List<Object>> df;
    private String to;

    public Map<String, List<Object>> getDf() {
        return df;
    }

    public void setDf(Map<String, List<Object>> df) {
        this.df = df;
    }

    public String getPush_source_name() {
        return push_source_name;
    }

    public void setPush_source_name(String push_source_name) {
        this.push_source_name = push_source_name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
