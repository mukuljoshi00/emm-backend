package com.noviro.emm_backend.androidapimanagement;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidmanagement.v1.model.Device;
import com.google.api.services.androidmanagement.v1.model.ListDevicesResponse;
import com.google.api.services.androidmanagement.v1.model.Policy;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noviro.emm_backend.model.DeviceLocation;
import com.noviro.emm_backend.repository.DeviceLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private DeviceLocationRepository deviceLocationRepository;

//    @PostMapping("/create")
//    public Enterprise createEnterprise(@RequestParam String displayName) throws IOException {
//        return enterpriseService.createEnterprise(displayName);
//    }

    @PostMapping("/enrollment-token")
    public ResponseEntity<byte[]> createEnrollmentToken(@RequestParam String enterpriseName,
                                                        @RequestParam String policyName,
                                                        @RequestParam(required = false) String userName) throws IOException, WriterException {
        byte[] qrCode = enterpriseService.createEnrollmentToken(enterpriseName, policyName, userName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCode.length);
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<Policy> getPolicyById(
            @PathVariable String policyId,
            @RequestParam String enterpriseName
    ) {
        Policy policy = enterpriseService.getPolicyById(enterpriseName, policyId);
        return policy != null ? ResponseEntity.ok(policy) : ResponseEntity.notFound().build();
    }

    /**
     * Generate QR code image as Base64 string from enrollment token QR code content
     */
    @GetMapping(value = "/enrollment-token/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getEnrollmentQrCode(@RequestParam String qrCodeContent) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 400, 400);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    @PostMapping("/declare/policy")
    public ResponseEntity<Policy> createOrUpdatePolicy(@RequestParam String enterpriseName,
                                                       @RequestParam String policyId,
                                                       @RequestBody String policyJson) {
        try {
            JsonFactory jsonFactory = new JacksonFactory();
            Policy policy = jsonFactory.fromString(policyJson, Policy.class);
            Policy createdPolicy = enterpriseService.createOrUpdatePolicy(enterpriseName, policyId, policy);
            return ResponseEntity.ok(createdPolicy);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/devices")
    public List<Device> listDevices(@RequestParam String enterpriseName) throws IOException {
        ListDevicesResponse response = enterpriseService.listDevices(enterpriseName);
        return response.getDevices();
    }

    @DeleteMapping("/{enterpriseId}/{deviceId}")
    public ResponseEntity<Void> deleteDevice(
            @PathVariable String enterpriseId,
            @PathVariable String deviceId) {
        String deviceName = String.format("enterprises/%s/devices/%s", enterpriseId, deviceId);
        try {
            enterpriseService.deleteDevice(deviceName);
            return ResponseEntity.noContent().build(); // 204
        } catch (IOException e) {
            return ResponseEntity.status(500).build(); // or custom error handling
        }
    }

    @GetMapping("/token")
    public String getToken() throws IOException {
        return enterpriseService.getAndroidAccessToken();
    }

    @Transactional
    @PostMapping("/latest-enrolled-device")
    public ResponseEntity<?> updateLatestDeviceLocation(@RequestParam String enterpriseName) throws IOException {
        ListDevicesResponse response = enterpriseService.listDevices(enterpriseName);
        List<Device> devices = response.getDevices();
        if (devices == null || devices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No devices found");
        }
        devices.sort((d1, d2) -> {
            if (d1.getEnrollmentTime() == null || d2.getEnrollmentTime() == null) return 0;
            return d2.getEnrollmentTime().compareTo(d1.getEnrollmentTime());
        });
        Device latestDevice = devices.get(0);
        String latestSerial = latestDevice.getHardwareInfo() != null ? latestDevice.getHardwareInfo().getSerialNumber() : null;
        if (latestSerial == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Latest device does not have a serial number");
        }
        var deviceLocationOpt = deviceLocationRepository.findTopByLinkedToDeviceFalseAndAmDeviceSerialNumberIsNullOrderByCreatedAtDesc();
        if (deviceLocationOpt.isPresent()) {
            DeviceLocation deviceLocation = deviceLocationOpt.get();
            deviceLocation.setAmDeviceSerialNumber(latestSerial);
            deviceLocationRepository.save(deviceLocation);
            return ResponseEntity.ok(deviceLocation);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching DeviceLocation found");
        }
    }
}
