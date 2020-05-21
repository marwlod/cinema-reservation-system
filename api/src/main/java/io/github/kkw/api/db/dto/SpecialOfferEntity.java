package io.github.kkw.api.db.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SpecialOfferEntity {

    @Id
    private int specialOfferId;
    private String code;
    private int percentage;

    public SpecialOfferEntity(){

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

    public void setSpecialOfferId(int specialOfferId) {
        this.specialOfferId = specialOfferId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
