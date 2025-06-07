package com.noviro.emm_backend.organization;

import com.noviro.emm_backend.repository.OrganizationRepository;
import com.noviro.emm_backend.model.Organization;
import com.noviro.emm_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.noviro.emm_backend.service.OrganizationService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization getOrganizationById(UUID id) {
        return organizationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    @Override
    public Organization createOrganization(Organization organization) {
        organization.setId(UUID.randomUUID());
        return organizationRepository.save(organization);
    }

    @Override
    public Organization getUserOrganization(UUID userId) {
        return userRepository.findById(userId)
            .map(user -> user.getOrganizationId() != null ?
                organizationRepository.findById(user.getOrganizationId()).orElse(null) : null)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
