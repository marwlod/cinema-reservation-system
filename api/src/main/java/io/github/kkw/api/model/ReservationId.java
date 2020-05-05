package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationId {
    private final int id;

    public ReservationId(@JsonProperty("id") int id) {
        this.id = id;
    }

    public ReservationId(String id) {
        this.id = Integer.parseInt(id);
    }

    public int getId() {
        return id;
    }
}
