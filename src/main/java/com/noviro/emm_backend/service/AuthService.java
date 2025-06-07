package com.noviro.emm_backend.service;

import com.noviro.emm_backend.jwt.JwtProvider;
import com.noviro.emm_backend.model.User;
import com.noviro.emm_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public Map<String, Object> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            return null;
        }

        User user = userOpt.get();
        String token = jwtProvider.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("email", user.getEmail());
        userInfo.put("name", user.getName());
        userInfo.put("role", user.getRole());
        userInfo.put("organizationId", user.getOrganizationId());

        response.put("user", userInfo);
        return response;
    }
}
