package com.noviro.emm_backend.controller;

import com.google.api.services.androidmanagement.v1.model.ListDevicesResponse;
import com.noviro.emm_backend.androidapimanagement.EnterpriseService;
import com.noviro.emm_backend.model.Device;
import com.noviro.emm_backend.model.DeviceLocation;
import com.noviro.emm_backend.repository.DeviceLocationRepository;
import com.noviro.emm_backend.service.DeviceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/location")
public class DeviceLocationController {

    @Autowired
    private DeviceLocationRepository deviceLocationRepository;

    @Autowired
    DeviceService deviceService;

    @Autowired
    EnterpriseService enterpriseService;

    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/update")
    public ResponseEntity<?> updateLocation(@RequestBody LocationUpdateRequest request) throws IOException {
        try{
        Optional<DeviceLocation> existing = deviceLocationRepository.findByDeviceSerialNumber(request.getSerialNumber());
        DeviceLocation location = existing.orElseGet(DeviceLocation::new);
        location.setDeviceSerialNumber(request.getSerialNumber());
        if (location.getCreatedAt() == null) {
            location.setCreatedAt(LocalDateTime.now());
        }
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setLastUpdated(LocalDateTime.now());
        location.setAndroidId(request.androidId);
        location.setDeviceIdentifier(request.deviceIdentifier);
        if(!location.isLinkedToDevice()){
            deviceLocationRepository.save(location);
            return ResponseEntity.ok().build();
        }
        //save device ids with device table
        Device device=deviceService.getDeviceByTemporaryId(request.getDeviceIdentifier());
        if(device != null) {
            device.setAndroidId(request.getAndroidId());
            device.setStatus("ONLINE");
            String policyName=device.getOrganization().getEnterpriseName()+"/policies/"+device.getPolicyId();
            getDeviceId(policyName,device,location);
            enterpriseService.updateDevicePolicy(device.getOrganization().getEnterpriseName(), device.getDeviceId(),"policy1");
            device.setPolicyId("policy1");
            deviceService.saveDevice(device);
            location.setLinkedToDevice(true);
            deviceLocationRepository.save(location);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Device not found for identifier: " + request.getDeviceIdentifier());
        }}catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while updating the location: " + e.getMessage());
        }
    }

    public String getDeviceId(String policyName,Device device,DeviceLocation deviceLocation) throws IOException {
        String enterpriseId = device.getOrganization().getEnterpriseName();
        ListDevicesResponse listDevicesResponse=enterpriseService.listDevices(enterpriseId);
        AtomicReference<String> deviceId= new AtomicReference<>();
        listDevicesResponse.getDevices().stream()
                .filter(d -> d.getPolicyName().equals(policyName))
                .findFirst()
                .ifPresent(d -> {
                    device.setDeviceId(d.getName());
                    deviceLocation.setAmDeviceSerialNumber(d.getHardwareInfo().getSerialNumber());
                });

        // Assuming the policyId is the same as the deviceId for simplicity
        // In a real application, you might need to fetch this from a service or database
        return deviceId.get();
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
        private String deviceIdentifier;
        private String androidId;

        public String getDeviceIdentifier() {
            return deviceIdentifier;
        }
        public void setDeviceIdentifier(String deviceIdentifier) {
            this.deviceIdentifier = deviceIdentifier;
        }
        public String getAndroidId() {
            return androidId;
        }
        public void setAndroidId(String androidId) {
            this.androidId = androidId;
        }

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

