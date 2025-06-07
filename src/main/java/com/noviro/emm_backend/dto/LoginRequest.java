package com.noviro.emm_backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;
}
