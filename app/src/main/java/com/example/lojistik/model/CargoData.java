package com.example.lojistik.model;

/**
 * Represents a cargo assigned to a user/courier.
 * Follows SRP and OOP principles.
 */
public class CargoData {
    private final long id;
    private final String trackingNumber;
    private final long senderId;
    private final long receiverId;
    private final double weight;
    private final String status; // Assuming API returns a status, or we can just use "Bekliyor" etc.

    public CargoData(long id, String trackingNumber, long senderId, long receiverId, double weight, String status) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.weight = weight;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
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

    public String getStatus() {
        return status;
    }
}
