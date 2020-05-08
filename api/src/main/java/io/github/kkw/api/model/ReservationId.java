package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationId {
    private final int clientId;

    public ReservationId(@JsonProperty("clientId") int clientId) {
        this.clientId = clientId;
    }

    public ReservationId(String clientId) {
        this.clientId = Integer.parseInt(clientId);
    }

    public int getClientId() {
        return clientId;
    }
}
