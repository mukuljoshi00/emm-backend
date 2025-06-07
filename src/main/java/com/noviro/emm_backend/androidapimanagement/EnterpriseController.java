package com.noviro.emm_backend.androidapimanagement;

import com.google.api.services.androidmanagement.v1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;
//
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

    @PostMapping("/policy")
    public Policy createOrUpdatePolicy(@RequestParam String enterpriseName,
                                       @RequestParam String policyId,
                                       @RequestBody Policy policy) throws IOException {
        return enterpriseService.createOrUpdatePolicy(enterpriseName, policyId, policy);
    }

    @GetMapping("/devices")
    public List<Device> listDevices(@RequestParam String enterpriseName) throws IOException {
        ListDevicesResponse response = enterpriseService.listDevices(enterpriseName);
        return response.getDevices();
    }
}
