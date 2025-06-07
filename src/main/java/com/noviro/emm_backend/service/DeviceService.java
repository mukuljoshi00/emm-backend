package com.noviro.emm_backend.service;

import com.noviro.emm_backend.model.Organization;
import com.noviro.emm_backend.qr.QRService;
import com.noviro.emm_backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    private final QRService qrService;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public DeviceService(QRService qrService, OrganizationRepository organizationRepository) {
        this.qrService = qrService;
        this.organizationRepository = organizationRepository;
    }

    public void onboardDevice(String organizationId) {
        Organization org = organizationRepository.findById(java.util.UUID.fromString(organizationId))
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for id: " + organizationId));
        String enterpriseId = org.getEnterpriseName();
//        qrService.generateQRCode()
    }
}
