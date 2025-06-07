package com.noviro.emm_backend.androidapimanagement;

import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.api.services.androidmanagement.v1.model.*;
import com.google.zxing.WriterException;
import com.noviro.emm_backend.qr.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EnterpriseService {

    @Autowired
    private AndroidManagement androidManagement;

    @Autowired
    QRService qrService;

    private final String PROJECT_ID = "YOUR_PROJECT_ID";  // Replace or make dynamic

    /**
     * Create a new enterprise.
     */
//    public Enterprise createEnterprise(String enterpriseDisplayName) throws IOException {
//        Enterprise enterprise = new Enterprise()
//                .setEnterpriseDisplayName(enterpriseDisplayName)
//                .setProjectId(PROJECT_ID);
//
//        AndroidManagement.Enterprises.Create request = androidManagement.enterprises().create(enterprise);
//        return request.execute();
//    }

    /**
     * Generate enrollment token for given enterprise.
     */
    public byte[]  createEnrollmentToken(String enterpriseName, String policyName, String userName) throws IOException, WriterException {
        EnrollmentToken token = new EnrollmentToken()
                .setPolicyName("policy1");
        if (userName != null && !userName.isEmpty()) {
            token.setUser(new User().setAccountIdentifier(userName));
        }

        AndroidManagement.Enterprises.EnrollmentTokens.Create request = androidManagement.enterprises()
                .enrollmentTokens()
                .create(enterpriseName, token);
        Object object=request.execute().get("qrCode");
        return qrService.generateQRCode(object,300, 300);
    }


    public Policy getPolicyById(String enterpriseName, String policyId) {
        String fullPolicyName = enterpriseName + "/policies/" + policyId;

        try {
            return androidManagement
                    .enterprises()
                    .policies()
                    .get(fullPolicyName)
                    .execute();
        } catch (IOException e) {
            // Log the exception here if needed
            return null;
        }
    }

    /**
     * Create or update a policy.
     */
    public Policy createOrUpdatePolicy(String enterpriseName, String policyId, Policy policy) throws IOException {
        AndroidManagement.Enterprises.Policies.Patch request = androidManagement.enterprises()
                .policies()
                .patch(enterpriseName + "/policies/" + "policy1", policy);
        return request.execute();
    }

    /**
     * List devices in an enterprise.
     */
    public ListDevicesResponse listDevices(String enterpriseName) throws IOException {
        AndroidManagement.Enterprises.Devices.List request = androidManagement.enterprises().devices().list(enterpriseName);
        return request.execute();
    }
}
