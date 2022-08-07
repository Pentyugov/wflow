package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Department;
import com.pentyugov.wflow.core.dto.DepartmentDto;
import com.pentyugov.wflow.web.exception.DepartmentExistException;
import com.pentyugov.wflow.web.exception.DepartmentNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    String NAME = "wflow$DepartmentService";

    List<Department> getAll();

    Department getById(UUID id) throws DepartmentNotFoundException;

    Department add(DepartmentDto departmentDto) throws DepartmentExistException;

    Department update(DepartmentDto departmentDto) throws DepartmentExistException, DepartmentNotFoundException;

    void delete(UUID id) throws ValidationException;

    List<Department> getPossibleParentDepartments(UUID id);

    List<Department> getChildren(UUID id);

    Department convert(DepartmentDto departmentDto);

    DepartmentDto convert(Department department);

}
