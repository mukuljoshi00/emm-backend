package com.noviro.emm_backend.controller;

import com.noviro.emm_backend.service.AuthService;
import com.noviro.emm_backend.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = authService.login(request.getEmail(), request.getPassword());
        if (response == null) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        return ResponseEntity.ok(response);
    }
}

