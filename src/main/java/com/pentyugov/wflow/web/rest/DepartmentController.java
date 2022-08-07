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
@RequestMapping("/api/departments")
public class DepartmentController extends AbstractController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<DepartmentDto> departments = new ArrayList<>();
        departmentService.getAll().forEach(department ->
                departments.add(departmentService.convert(department)));
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) throws DepartmentNotFoundException {
        Department department = departmentService.getById(UUID.fromString(id));
        return new ResponseEntity<>(departmentService.convert(department), HttpStatus.OK);
    }

    @GetMapping("/{id}/possible-parent")
    public ResponseEntity<Object> getPossibleParentDepartments(@PathVariable String id) {
        List<DepartmentDto> departments = new ArrayList<>();
        departmentService.getPossibleParentDepartments(UUID.fromString(id)).forEach(department ->
                departments.add(departmentService.convert(department)));
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody DepartmentDto departmentDto) throws DepartmentExistException {
        Department department = departmentService.add(departmentDto);
        return new ResponseEntity<>(departmentService.convert(department), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody DepartmentDto departmentDto)
            throws DepartmentExistException, DepartmentNotFoundException {

        Department department = departmentService.update(departmentDto);
        return new ResponseEntity<>(departmentService.convert(department), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) throws ValidationException {
        departmentService.delete(UUID.fromString(id));
        String message = String.format("Department with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

}
