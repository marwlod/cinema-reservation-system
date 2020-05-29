package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.SpecialOfferEntity;

public class SpecialOffer {

    private final int specialOfferId;
    private final String code;
    private final int percentage;

    public SpecialOffer(
            @JsonProperty("specialOfferId") int specialOfferId,
            @JsonProperty("code") String code,
            @JsonProperty("percentage") int percentage ){
        this.specialOfferId=specialOfferId;
        this.code=code;
        this.percentage=percentage;
    }

    public SpecialOffer(final SpecialOfferEntity specialOfferEntity){
        this(specialOfferEntity.getSpecialOfferId(),
                specialOfferEntity.getCode(),
                specialOfferEntity.getPercentage());
    }

    public int getSpecialOfferId() {
        return specialOfferId;
    }

    public String getCode() {
        return code;
    }

    public int getPercentage() {
        return percentage;
    }
}
