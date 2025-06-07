package com.noviro.emm_backend.controller;

import com.noviro.emm_backend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/onboard")
    public ResponseEntity<String> onboardDevice(@RequestParam String enterpriseId) {
        deviceService.onboardDevice(enterpriseId);
        return ResponseEntity.ok("Device onboarding initiated.");
    }
}

