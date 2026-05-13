package com.logistics.cargo_service.model;

import java.time.LocalDateTime;

public class Cargo {
    private long id ;
    private String trackingNumber;
    private long senderId;
    private long receiverId;
    private double weight;
    private String status;
    private String currentLocation;
    private LocalDateTime estimatedDeliveryDate;

    public Cargo(){}

    public Cargo(long id , String trackingNumber , long senderId , long receiverId , double weight ,String status , String currentLocation , LocalDateTime estimatedDeliveryDate){
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.weight = weight;
        this.status = status;
        this.currentLocation = currentLocation;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LocalDateTime getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }
}
