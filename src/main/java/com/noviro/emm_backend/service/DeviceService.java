package com.noviro.emm_backend.service;

import com.google.api.services.androidmanagement.v1.model.Application;
import com.google.api.services.androidmanagement.v1.model.Policy;
import com.google.zxing.WriterException;
import com.noviro.emm_backend.androidapimanagement.EnterpriseService;
import com.noviro.emm_backend.model.Device;
import com.noviro.emm_backend.model.Employee;
import com.noviro.emm_backend.model.Organization;
import com.noviro.emm_backend.qr.QRService;
import com.noviro.emm_backend.repository.DeviceRepository;
import com.noviro.emm_backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DeviceService {
    private final QRService qrService;
    private final OrganizationRepository organizationRepository;
    private final EnterpriseService enterpriseService;
    private final EmployeeService employeeService;
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(QRService qrService, OrganizationRepository organizationRepository, EnterpriseService enterpriseService, EmployeeService employeeService, DeviceRepository deviceRepository) {
        this.qrService = qrService;
        this.organizationRepository = organizationRepository;
        this.enterpriseService = enterpriseService;
        this.employeeService = employeeService;
        this.deviceRepository = deviceRepository;
    }

    public Device getDeviceByTemporaryId(String temporaryId) {
        return deviceRepository.findByTemporaryId(temporaryId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found for temporary id: " + temporaryId));
    }

    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }

    public byte[] onboardDevice(String organizationId, String employeeId) throws IOException, WriterException {
        String userEmail = null;
        Employee employee = null;
        if (employeeId != null) {
            employee = employeeService.getEmployeeById(employeeId);
            userEmail = employee.getEmail();
        }
        Organization org = organizationRepository.findByEnterpriseName(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for id: " + organizationId));
        String enterpriseId = org.getEnterpriseName();
        Policy policy = enterpriseService.getPolicyById(enterpriseId, "policy1");
        if (policy == null) {
            throw new IllegalArgumentException("Policy not found for enterprise: " + enterpriseId);
        }

        String newPolicyId = UUID.randomUUID().toString();
        policy.setName(enterpriseId + "/policies/"+newPolicyId);
        String deviceIdentifier = UUID.randomUUID().toString();
        //find the device tracekr application
        //inject device identifier into that policy
        Map<String, Object> secrets = new HashMap<>();
        secrets.put("deviceIdentifier", deviceIdentifier);
        if(userEmail != null) {
            secrets.put("userEmail", userEmail);
        }
        policy.getApplications().stream().filter(app->app.getPackageName().equals("com.example.locationtracker")).findFirst().get().setManagedConfiguration(secrets);
        Policy newPolicy = enterpriseService.createOrUpdatePolicy(enterpriseId, newPolicyId, policy);
        //if this is for a user
        //
        Device device = new Device();
        device.setId(UUID.randomUUID());
        device.setEmployee(employee);
        device.setStatus("INACTIVE");
        device.setPolicyId(newPolicyId);
        device.setTemporaryId(deviceIdentifier);
        device.setOrganization(org);
        device.setAndroidId("");
        deviceRepository.save(device);
        return enterpriseService.createEnrollmentToken(enterpriseId, newPolicyId, userEmail);
    }

    /*
    device gets onboarded
    installs app
    app sends device identifier to backend  along with android id
    lookup device by identifier
    if device location status is INACTIVE
    update device status to ACTIVE

     */

}
