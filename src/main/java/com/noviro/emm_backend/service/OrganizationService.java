package com.noviro.emm_backend.service;

import com.noviro.emm_backend.model.Organization;
import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    List<Organization> getAllOrganizations();
    Organization getOrganizationById(UUID id);
    Organization createOrganization(Organization organization);
    Organization getUserOrganization(UUID userId);
}
