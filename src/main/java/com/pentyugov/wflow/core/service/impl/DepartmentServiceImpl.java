package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Department;
import com.pentyugov.wflow.core.dto.DepartmentDto;
import com.pentyugov.wflow.core.repository.DepartmentRepository;
import com.pentyugov.wflow.core.repository.EmployeeRepository;
import com.pentyugov.wflow.core.service.DepartmentService;
import com.pentyugov.wflow.web.exception.DepartmentExistException;
import com.pentyugov.wflow.web.exception.DepartmentNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service(DepartmentService.NAME)
@RequiredArgsConstructor
public class DepartmentServiceImpl extends AbstractService implements DepartmentService {

    private final ModelMapper modelMapper;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getById(UUID id) throws DepartmentNotFoundException {
        return departmentRepository.findById(id).orElseThrow(()->
                new DepartmentNotFoundException(getMessage("exception.department.not.found", "id", id.toString())));
    }

    @Override
    public Department add(DepartmentDto departmentDto) throws DepartmentExistException {
        Department department = convert(departmentDto);
        validate(department, false);
        return departmentRepository.save(department);
    }

    @Override
    public Department update(DepartmentDto departmentDto) throws DepartmentExistException, DepartmentNotFoundException {
        Department department = convert(departmentDto);
        validate(department, true);
        return departmentRepository.save(department);
    }

    @Override
    public void delete(UUID id) throws ValidationException {
        if (CollectionUtils.isEmpty(employeeRepository.findByEmployeesDepartment(id))) {
            departmentRepository.getDepartmentByParentDepartment(id).forEach(department -> {
                department.setParentDepartment(null);
                departmentRepository.save(department);
            });
            departmentRepository.delete(id);
        } else {
            throw new ValidationException(getMessage("exception.validation.unable.delete.department"));
        }

    }

    @Override
    public List<Department> getPossibleParentDepartments(UUID id) {
        Set<UUID> ids = new HashSet<>();
        ids.add(id);
        List<Department> tmp = departmentRepository.getDepartmentByParentDepartment(id);
        for (Department department : tmp) {
            ids.add(department.getId());
            List<Department> departments = getChildren(department.getId());
            departments.forEach(d -> ids.add(d.getId()));
        }
        return departmentRepository.getPossibleParentDepartments(new ArrayList<>(ids));
    }

    @Override
    public List<Department> getChildren(UUID id) {
        List<Department> result = new ArrayList<>();
        List<Department> children = departmentRepository.getDepartmentByParentDepartment(id);
        if (!CollectionUtils.isEmpty(children)) {
            result.addAll(children);
            for (Department department : children) {
                result.addAll(getChildren(department.getId()));
            }
        }

        return result;
    }

    @Override
    public Department convert(DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(departmentDto.getId()).orElse(new Department());
        if (!ObjectUtils.isEmpty(departmentDto.getId())) {
            department.setId(departmentDto.getId());
        }
        department.setCode(departmentDto.getCode());
        department.setName(departmentDto.getName());
        if (ObjectUtils.isEmpty(departmentDto.getHead())) {
            department.setHead(false);
        } else {
            department.setHead(departmentDto.getHead());
        }
        if (departmentDto.getParentDepartment() != null) {
            UUID parentDepartmentId = departmentDto.getParentDepartment().getId();
            Department parent = departmentRepository.findById(parentDepartmentId).orElse(null);
            department.setParentDepartment(parent);
            if (!ObjectUtils.isEmpty(parent)) {
                department.setLevel(parent.getLevel() + 1);
            }

        } else {
            department.setLevel(1);
        }
        return department;
    }

    @Override
    public DepartmentDto convert(Department department) {
        return modelMapper.map(department, DepartmentDto.class);
    }

    private void validate(Department department, boolean isUpdate) throws DepartmentExistException {
        if (isUpdate) {
            Department existedDepartment = departmentRepository.findByCode(department.getCode()).orElse(null);
            if (existedDepartment != null && !existedDepartment.getId().equals(department.getId())) {
                throw new DepartmentExistException(getMessage("exception.department.exist", "code", department.getCode()));
            }
            existedDepartment = departmentRepository.findByName(department.getName()).orElse(null);
            if (existedDepartment != null && !existedDepartment.getId().equals(department.getId())) {
                throw new DepartmentExistException(getMessage("exception.department.exist", "name", department.getName()));
            }

            if (department.getHead()) {
                existedDepartment = departmentRepository.findHeadDepartment().orElse(null);
                if (existedDepartment != null && !existedDepartment.getId().equals(department.getId())) {
                    throw new DepartmentExistException(getMessage("exception.department.head.exist"));
                }
            }

        } else {
            if (department.getHead()) {
                if (!ObjectUtils.isEmpty(departmentRepository.findHeadDepartment())) {
                    throw new DepartmentExistException(getMessage("exception.department.head.exist"));
                }
            }

            if (!ObjectUtils.isEmpty(departmentRepository.findByCode(department.getCode()))) {
                throw new DepartmentExistException(getMessage("exception.department.exist", "code", department.getCode()));
            }
            if (!ObjectUtils.isEmpty(departmentRepository.findByName(department.getName()))) {
                throw new DepartmentExistException(getMessage("exception.department.exist", "name", department.getName()));
            }
        }

    }

}
