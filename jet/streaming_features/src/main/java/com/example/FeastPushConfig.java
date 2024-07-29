package com.example;

public class FeastPushConfig {
    private final String url;
    private final String pushSourceName;
    private final String target;

    public FeastPushConfig(String baseUrl, String pushSourceName, String target) {
        this.url = baseUrl + "/push";
        this.pushSourceName = pushSourceName;
        this.target = target;
    }

    public String getPushSourceName() {
        return pushSourceName;
    }

    public String getTarget() {
        return target;
    }

    public String getUrl() {
        return url;
    }
}
