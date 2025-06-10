package com.noviro.emm_backend.androidapimanagement;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;

@Configuration
public class AndroidManagementConfig {

    private static final String ANDROID_MANAGEMENT_SCOPE = "https://www.googleapis.com/auth/androidmanagement";

    @Value("${gcp.android.service-account-json-base64}")
    private String serviceAccountBase64;

    @Bean
    public AndroidManagement androidManagement() throws GeneralSecurityException, Exception {
        // Decode the Base64 string to get JSON bytes
        byte[] decodedJson = Base64.getDecoder().decode(serviceAccountBase64);

        // Wrap decoded bytes into InputStream
        ByteArrayInputStream jsonStream = new ByteArrayInputStream(decodedJson);

        // Create credentials from InputStream
        GoogleCredentials credentials = GoogleCredentials.fromStream(jsonStream)
                .createScoped(Collections.singleton(ANDROID_MANAGEMENT_SCOPE));

        return new AndroidManagement.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Your App Name")
                .build();
    }

    @Bean
    public  String androidAccessToken() throws Exception {
        byte[] decodedJson = Base64.getDecoder().decode(serviceAccountBase64);
        // Wrap decoded bytes into InputStream
        ByteArrayInputStream jsonStream = new ByteArrayInputStream(decodedJson);
        // Create credentials from InputStream
        GoogleCredentials credentials = GoogleCredentials.fromStream(jsonStream)
                .createScoped(Collections.singleton(ANDROID_MANAGEMENT_SCOPE));
        credentials.refreshIfExpired();
        String accessToken = credentials.getAccessToken().getTokenValue();
        System.out.println("Access Token: " + accessToken);

        return accessToken;
    }
}
