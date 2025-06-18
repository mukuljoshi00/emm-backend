package com.noviro.emm_backend.startup;

import com.noviro.emm_backend.model.Employee;
import com.noviro.emm_backend.repository.EmployeeRepository;
import com.noviro.emm_backend.service.EmployeeService;
import com.noviro.emm_backend.service.OrganizationService;
import com.noviro.emm_backend.model.Organization;
import com.noviro.emm_backend.model.Role;
import com.noviro.emm_backend.model.User;
import com.noviro.emm_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            OrganizationService organizationService,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Create super admin if not exists
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setId(UUID.randomUUID());
                admin.setEmail("admin@example.com");
                admin.setName("Super Admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(Role.SUPER_ADMIN);
                userRepository.save(admin);
            }

            // Create a test organization and org admin
            if (userRepository.findByEmail("orgadmin@example.com").isEmpty()) {
                Organization org = new Organization();
                org.setId(UUID.randomUUID());
                org.setName("Noviro");
                org.setCloudProjectId("test-cloud-project-id");
                org.setEnterpriseName("Test Enterprise");
                org.setOrganizationStatus("ACTIVE");
                org = organizationService.createOrganization(org);

                User orgAdmin = new User();
                orgAdmin.setId(UUID.randomUUID());
                orgAdmin.setEmail("orgadmin@example.com");
                orgAdmin.setName("Org Admin");
                orgAdmin.setPasswordHash(passwordEncoder.encode("orgadmin123"));
                orgAdmin.setRole(Role.ORG_ADMIN);
                orgAdmin.setOrganizationId(org.getId());
                userRepository.save(orgAdmin);

                // Create a regular user in the organization
                User user = new User();
                user.setId(UUID.randomUUID());
                user.setEmail("user@example.com");
                user.setName("Regular User");
                user.setPasswordHash(passwordEncoder.encode("user123"));
                user.setRole(Role.USER);
                user.setOrganizationId(org.getId());
                userRepository.save(user);
            }
        };
    }

    @Bean
    public CommandLineRunner initDataEmployee(
            EmployeeRepository userRepository,
            EmployeeService organizationService, EmployeeRepository employeeRepository) {
        return args -> {
            // Create super admin if not exists
            if (employeeRepository.findEmployeeByEmail("pradeep@noviro.com").isEmpty()) {
                Employee employee = new Employee();
                employee.setId(UUID.randomUUID());
                employee.setEmail("pradeep@noviro.com");
                employee.setName("pradeep");
                employee.setPosition("Software Engineer");
                employee.setDepartment("Development");

                employeeRepository.save(employee);
            }

            if (employeeRepository.findEmployeeByEmail("anand@noviro.com").isEmpty()) {
                Employee employee = new Employee();
                employee.setId(UUID.randomUUID());
                employee.setEmail("anand@noviro.com");
                employee.setName("anand");
                employee.setPosition("Software Engineer");
                employee.setDepartment("Development");

                employeeRepository.save(employee);
            }
            if (employeeRepository.findEmployeeByEmail("pavan@noviro.com").isEmpty()) {
                Employee employee = new Employee();
                employee.setId(UUID.randomUUID());
                employee.setEmail("pavan@noviro.com");
                employee.setName("pavan");
                employee.setPosition("Principal Engineer");
                employee.setDepartment("Development");

                employeeRepository.save(employee);
            }
            if (employeeRepository.findEmployeeByEmail("mukul@noviro.com").isEmpty()) {
                Employee employee = new Employee();
                employee.setId(UUID.randomUUID());
                employee.setEmail("mukul@noviro.com");
                employee.setName("mukul");
                employee.setPosition("Project Manager");
                employee.setDepartment("Management");

                employeeRepository.save(employee);
            }
            if (employeeRepository.findEmployeeByEmail("mahesh@noviro.com").isEmpty()) {
                Employee employee = new Employee();
                employee.setId(UUID.randomUUID());
                employee.setEmail("mahesh@noviro.com");
                employee.setName("mahesh");
                employee.setPosition("Human Resources");
                employee.setDepartment("Management");
                employeeRepository.save(employee);
            }
        };
    }
}
