package com.noviro.emm_backend.controller;

import com.noviro.emm_backend.model.Employee;
import com.noviro.emm_backend.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final  EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/all")
     public List<Employee> getAllEmployees(@RequestParam String organizationId) {
         return employeeService.getAllEmployees(organizationId);
     }

     @PostMapping("/create")
     public Employee createEmployee(@RequestBody Employee employee) {
         return employeeService.createEmployee(employee);
     }



}
