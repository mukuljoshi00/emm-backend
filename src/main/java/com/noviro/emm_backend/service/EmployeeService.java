package com.noviro.emm_backend.service;

import com.noviro.emm_backend.model.Employee;
import com.noviro.emm_backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }



    public List<Employee> getAllEmployees(String organizationId) {
        List<Employee> employeeList = employeeRepository.findEmployeesByOrganizationId(UUID.fromString(organizationId));
        return employeeList;
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(String employeeId) {
        return employeeRepository.findById(UUID.fromString(employeeId))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found for id: " + employeeId));
    }

}
