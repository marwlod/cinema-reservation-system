package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientId {
    private final int id;

    public ClientId(@JsonProperty("id") int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
