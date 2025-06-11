package com.noviro.emm_backend.controller;

import com.noviro.emm_backend.model.DeviceLocation;
import com.noviro.emm_backend.repository.DeviceLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/location")
public class DeviceLocationController {

    @Autowired
    private DeviceLocationRepository deviceLocationRepository;

    @PostMapping("/update")
    public ResponseEntity<?> updateLocation(@RequestBody LocationUpdateRequest request) {
        Optional<DeviceLocation> existing = deviceLocationRepository.findByDeviceSerialNumber(request.getSerialNumber());
        DeviceLocation location = existing.orElseGet(DeviceLocation::new);
        location.setDeviceSerialNumber(request.getSerialNumber());
        if (location.getCreatedAt() == null) {
            location.setCreatedAt(LocalDateTime.now());
        }
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setLastUpdated(LocalDateTime.now());
        deviceLocationRepository.save(location);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/{serialNumber}")
    public ResponseEntity<?> getLocation(@PathVariable String serialNumber) {
        Optional<DeviceLocation> locationOpt = deviceLocationRepository.findByAmDeviceSerialNumber(serialNumber);
        if (locationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        DeviceLocation location = locationOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("serialNumber", location.getAmDeviceSerialNumber());
        response.put("latitude", location.getLatitude());
        response.put("longitude", location.getLongitude());
        response.put("lastUpdated", location.getLastUpdated());
        return ResponseEntity.ok(response);
    }

    public static class LocationUpdateRequest {
        private String serialNumber;
        private Double latitude;
        private Double longitude;

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
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
    }
}

