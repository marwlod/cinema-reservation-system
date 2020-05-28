package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferAddRequest {

    private final String code;
    private final int percentage;

    public SpecialOfferAddRequest(
            @JsonProperty("code") String code,
            @JsonProperty("percentage") int percentage) {
        this.code = code;
        this.percentage = percentage;
    }

    public String getCode() {
        return code;
    }

    public int getPercentage() {
        return percentage;
    }
}
