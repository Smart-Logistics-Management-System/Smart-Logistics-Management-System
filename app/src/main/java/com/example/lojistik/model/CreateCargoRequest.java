package com.example.lojistik.model;

/**
 * DTO for creating a new cargo.
 * Used by admins.
 */
public class CreateCargoRequest {
    private final long senderId;
    private final long receiverId;
    private final double weight;

    public CreateCargoRequest(long senderId, long receiverId, double weight) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.weight = weight;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public double getWeight() {
        return weight;
    }

    public String toJson() {
        return "{"
                + "\"senderId\":" + senderId + ","
                + "\"receiverId\":" + receiverId + ","
                + "\"weight\":" + weight
                + "}";
    }
}
