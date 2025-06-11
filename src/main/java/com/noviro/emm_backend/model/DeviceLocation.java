package com.noviro.emm_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class DeviceLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String deviceSerialNumber;
    private String amDeviceSerialNumber;
    private LocalDateTime createdAt;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdated;
    private boolean linkedToDevice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getAmDeviceSerialNumber() {
        return amDeviceSerialNumber;
    }

    public void setAmDeviceSerialNumber(String amDeviceSerialNumber) {
        this.amDeviceSerialNumber = amDeviceSerialNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isLinkedToDevice() {
        return linkedToDevice;
    }

    public void setLinkedToDevice(boolean linkedToDevice) {
        this.linkedToDevice = linkedToDevice;
    }
}

