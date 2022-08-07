package com.pentyugov.wflow.web.rest;


import com.pentyugov.wflow.core.domain.entity.Employee;
import com.pentyugov.wflow.core.dto.EmployeeDto;
import com.pentyugov.wflow.core.service.EmployeeService;
import com.pentyugov.wflow.web.exception.*;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController extends AbstractController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<EmployeeDto> employees = new ArrayList<>();
        employeeService.getAll().forEach(employee ->
                employees.add(employeeService.convert(employee)));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) throws EmployeeNotFoundException {
        Employee employee = employeeService.getById(UUID.fromString(id));
        return new ResponseEntity<>(employeeService.convert(employee), HttpStatus.OK);
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<Object> getByDepartments(@PathVariable String id) {
        List<EmployeeDto> employees = new ArrayList<>();
        employeeService.getByDepartment(UUID.fromString(id)).forEach(employee ->
                employees.add(employeeService.convert(employee)));
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody EmployeeDto employeeDto)
            throws UserNotFoundException, EmployeeExistException, PositionNotFoundException, ValidationException,
            DepartmentNotFoundException {

        Employee employee = employeeService.add(employeeDto);
        return new ResponseEntity<>(employeeService.convert(employee), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody EmployeeDto employeeDto)
            throws UserNotFoundException, EmployeeExistException, DepartmentNotFoundException,
            PositionNotFoundException, ValidationException {

        Employee employee = employeeService.update(employeeDto);
        return new ResponseEntity<>(employeeService.convert(employee), HttpStatus.OK);
    }

    @PutMapping("/all")
    public ResponseEntity<Object> updateAll(@RequestBody List<EmployeeDto> employees)
            throws PositionNotFoundException,
            UserNotFoundException, DepartmentNotFoundException {

        this.employeeService.updateAll(employees);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        employeeService.delete(UUID.fromString(id));
        String message = String.format("Employee with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

}
