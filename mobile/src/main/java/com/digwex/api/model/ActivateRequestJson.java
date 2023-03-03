package com.digwex.api.model;

public class ActivateRequestJson {

    public String pin;
    public String platform;

    public ActivateRequestJson(String pin, String platform) {
        this.pin = pin;
        this.platform = platform;
    }

    public ActivateRequestJson() {
    }
}
