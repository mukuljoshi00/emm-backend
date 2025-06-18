package com.noviro.emm_backend.controller;

import com.google.zxing.WriterException;
import com.noviro.emm_backend.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping(value = "/onboard",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> onboardDevice(@RequestParam String enterpriseId,@RequestParam(required = false) String employeeId) throws IOException, WriterException {


        return ResponseEntity.ok(deviceService.onboardDevice(enterpriseId,employeeId));
    }
}

