package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HallId {

    private final int id;

    public HallId(@JsonProperty("id") int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }
}
