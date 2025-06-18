package com.noviro.emm_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    private UUID id;
    private String name;
    private String email;
    private String position;
    private String department;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
