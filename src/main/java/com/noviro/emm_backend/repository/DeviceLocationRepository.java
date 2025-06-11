package com.noviro.emm_backend.repository;

import com.noviro.emm_backend.model.DeviceLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceLocationRepository extends JpaRepository<DeviceLocation, Long> {
    Optional<DeviceLocation> findByAmDeviceSerialNumber(String serialNumber);

    Optional<DeviceLocation> findByDeviceSerialNumber(String serialNumber);

    Optional<DeviceLocation> findTopByLinkedToDeviceFalseAndAmDeviceSerialNumberIsNullOrderByCreatedAtDesc();
}
