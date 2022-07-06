package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Department;
import com.pentyugov.wflow.core.dto.DepartmentDto;
import com.pentyugov.wflow.core.service.DepartmentService;
import com.pentyugov.wflow.web.exception.DepartmentExistException;
import com.pentyugov.wflow.web.exception.DepartmentNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/department")
public class DepartmentController extends AbstractController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/get-all-departments")
    public ResponseEntity<Object> getAllDepartments() {
        List<DepartmentDto> departments = new ArrayList<>();
        departmentService.getAllDepartments().forEach(department ->
                departments.add(departmentService.createDepartmentDtoFromDepartment(department)));
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/get-department-by-id/{id}")
    public ResponseEntity<Object> getDepartmentById(@PathVariable String id) throws DepartmentNotFoundException {
        Department department = departmentService.getDepartmentById(UUID.fromString(id));
        return new ResponseEntity<>(departmentService.createDepartmentDtoFromDepartment(department), HttpStatus.OK);
    }

    @GetMapping("/get-possible-parent/{id}")
    public ResponseEntity<Object> getPossibleParentDepartments(@PathVariable String id) {
        List<DepartmentDto> departments = new ArrayList<>();
        departmentService.getPossibleParentDepartments(UUID.fromString(id)).forEach(department ->
                departments.add(departmentService.createDepartmentDtoFromDepartment(department)));
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @PostMapping("/add-new-department")
    public ResponseEntity<Object> addNewDepartment(@RequestBody DepartmentDto departmentDto) throws DepartmentExistException {
        Department department = departmentService.addNewDepartment(departmentDto);
        return new ResponseEntity<>(departmentService.createDepartmentDtoFromDepartment(department), HttpStatus.OK);
    }

    @PutMapping("/update-department")
    public ResponseEntity<Object> updateDepartment(@RequestBody DepartmentDto departmentDto) throws DepartmentExistException, DepartmentNotFoundException {
        Department department = departmentService.updateDepartment(departmentDto);
        return new ResponseEntity<>(departmentService.createDepartmentDtoFromDepartment(department), HttpStatus.OK);
    }

    @DeleteMapping("/delete-department/{id}")
    public ResponseEntity<HttpResponse> deleteDepartment(@PathVariable String id) throws ValidationException {
        departmentService.deleteDepartment(UUID.fromString(id));
        String message = String.format("Department with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

}
