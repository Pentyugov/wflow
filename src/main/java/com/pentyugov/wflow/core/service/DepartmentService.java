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

    List<Department> getAllDepartments();

    List<Department> getPossibleParentDepartments(UUID id);

    List<Department> getChildren(UUID id);

    Department addNewDepartment(DepartmentDto departmentDto) throws DepartmentExistException;

    Department updateDepartment(DepartmentDto departmentDto) throws DepartmentExistException, DepartmentNotFoundException;

    void deleteDepartment(UUID id) throws ValidationException;

    Department getDepartmentById(UUID id) throws DepartmentNotFoundException;

    void validateDepartment(Department department, boolean isUpdate) throws DepartmentExistException;

    Department createDepartmentFromDepartmentDto(DepartmentDto departmentDto);

    DepartmentDto createDepartmentDtoFromDepartment(Department department);

}
