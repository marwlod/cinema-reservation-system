package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientId {
    private final int clientId;

    public ClientId(@JsonProperty("clientId") int clientId) {
        this.clientId = clientId;
    }

    public ClientId(String clientId) {
        this.clientId = Integer.parseInt(clientId);
    }

    public int getClientId() {
        return clientId;
    }
}
