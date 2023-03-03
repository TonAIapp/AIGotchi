package com.digwex.api.model;

public class StepsJson {

    public Integer steps;
    public Long telegram_id;

    public StepsJson(Integer steps, Long telegramId) {
        this.steps = steps;
        this.telegram_id = telegramId;
    }

    public StepsJson() {
    }
}
