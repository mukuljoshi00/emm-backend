package com.noviro.emm_backend.controller;

import com.noviro.emm_backend.service.OrganizationService;
import com.noviro.emm_backend.model.Organization;
import com.noviro.emm_backend.model.Role;
import com.noviro.emm_backend.model.User;
import com.noviro.emm_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orgs")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('ORG_ADMIN', 'USER','SUPER_ADMIN')")
    public ResponseEntity<Organization> getMyOrganization(@AuthenticationPrincipal String userId) {
        Organization org = organizationService.getUserOrganization(UUID.fromString(userId));
        return ResponseEntity.ok(org);
    }

    @PostMapping
    public ResponseEntity<?> createOrganization(@RequestBody Organization organization, @AuthenticationPrincipal String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOrganizationId() != null) {
            return ResponseEntity.badRequest().body("User already has an organization");
        }

        Organization newOrg = organizationService.createOrganization(organization);
        user.setOrganizationId(newOrg.getId());
        user.setRole(Role.ORG_ADMIN);
        userRepository.save(user);

        return ResponseEntity.ok(newOrg);
    }
}
