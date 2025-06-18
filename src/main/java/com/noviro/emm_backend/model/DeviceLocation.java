package com.noviro.emm_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class DeviceLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceSerialNumber;
    private String amDeviceSerialNumber;
    private String androidId;
    private String deviceIdentifier;
    private Date createdAt;
    private Double latitude;
    private Double longitude;
    private Date lastUpdated;
    private boolean linkedToDevice;
}

