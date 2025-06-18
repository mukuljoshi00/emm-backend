package com.noviro.emm_backend.repository;

import com.noviro.emm_backend.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    Optional<Device> findByDeviceId(String deviceId);

    Optional<Device> findBySerialNumber(String serialNumber);

    Optional<Device> findByTemporaryId(String temporaryId);

}
