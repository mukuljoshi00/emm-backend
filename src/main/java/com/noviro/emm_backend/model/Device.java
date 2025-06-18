package com.noviro.emm_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "devices")
public class Device {
    @Id
    private UUID id;

    //will be android id in case of android devices.
    private String androidId;

    private String deviceId;

    private String serialNumber;

    private String policyId;

    private String temporaryId;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}

