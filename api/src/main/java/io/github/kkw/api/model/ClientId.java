package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientId {
    private final int id;

    public ClientId(@JsonProperty("id") int id) {
        this.id = id;
    }

    public ClientId(String id) {
        this.id = Integer.parseInt(id);
    }

    public int getId() {
        return id;
    }
}
