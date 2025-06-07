package com.noviro.emm_backend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String deviceId;

    @Column(nullable = false)
    private String enrollmentToken;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEnrollmentToken() {
        return enrollmentToken;
    }

    public void setEnrollmentToken(String enrollmentToken) {
        this.enrollmentToken = enrollmentToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}

