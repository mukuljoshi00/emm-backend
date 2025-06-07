package com.noviro.emm_backend.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "cloud_project_id")
    private String cloudProjectId;

    @Column(name = "enterprise_name")
    private String enterpriseName;

    @Column(name = "organization_status")
    private String organizationStatus;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCloudProjectId() {
        return cloudProjectId;
    }

    public void setCloudProjectId(String cloudProjectId) {
        this.cloudProjectId = cloudProjectId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getOrganizationStatus() {
        return organizationStatus;
    }

    public void setOrganizationStatus(String organizationStatus) {
        this.organizationStatus = organizationStatus;
    }
}
