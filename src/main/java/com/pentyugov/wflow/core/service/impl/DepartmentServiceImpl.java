package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Department;
import com.pentyugov.wflow.core.dto.DepartmentDto;
import com.pentyugov.wflow.core.repository.DepartmentRepository;
import com.pentyugov.wflow.core.repository.EmployeeRepository;
import com.pentyugov.wflow.core.service.DepartmentService;
import com.pentyugov.wflow.core.service.MessageService;
import com.pentyugov.wflow.web.exception.DepartmentExistException;
import com.pentyugov.wflow.web.exception.DepartmentNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service(DepartmentService.NAME)
public class DepartmentServiceImpl extends AbstractService implements DepartmentService {

    private final ModelMapper modelMapper;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, MessageService messageService, ModelMapper modelMapper, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.messageService = messageService;
        this.employeeRepository = employeeRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

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

    public Department addNewDepartment(DepartmentDto departmentDto) throws DepartmentExistException {
        Department department = createDepartmentFromDepartmentDto(departmentDto);
        validateDepartment(department, false);
        return departmentRepository.save(department);
    }

    public Department updateDepartment(DepartmentDto departmentDto) throws DepartmentExistException, DepartmentNotFoundException {
        Department department = departmentRepository.getById(departmentDto.getId());
        department.setCode(departmentDto.getCode());
        department.setName(departmentDto.getName());
        if (!ObjectUtils.isEmpty(departmentDto.getParentDepartment())) {
            Department parent = getDepartmentById(departmentDto.getParentDepartment().getId());
            department.setParentDepartment(parent);
            department.setLevel(parent.getLevel() + 1);
        } else {
            department.setParentDepartment(null);
            department.setLevel(1);
        }
        if (!ObjectUtils.isEmpty(departmentDto.getHead())) {
            department.setHead(departmentDto.getHead());
        } else {
            department.setHead(false);
        }

        validateDepartment(department, true);
        return departmentRepository.save(department);
    }



    public void deleteDepartment(UUID id) throws ValidationException {
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

    public Department getDepartmentById(UUID id) throws DepartmentNotFoundException {
        return departmentRepository.findById(id).orElseThrow(()->
                new DepartmentNotFoundException(getMessage("exception.department.not.found", "id", id.toString())));
    }

    public void validateDepartment(Department department, boolean isUpdate) throws DepartmentExistException {
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

    public Department createDepartmentFromDepartmentDto(DepartmentDto departmentDto) {
        Department department = new Department();
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

    public DepartmentDto createDepartmentDtoFromDepartment(Department department) {
//        DepartmentDto departmentDto = new DepartmentDto();
//        departmentDto.setId(department.getId());
//        departmentDto.setCode(department.getCode());
//        departmentDto.setName(department.getName());
//        if (ObjectUtils.isEmpty(department.getHead())) {
//            departmentDto.setHead(false);
//        } else {
//            departmentDto.setHead(department.getHead());
//        }
//        Department parent = department.getParentDepartment();
//        departmentDto.setParentDepartment(parent != null ? createDepartmentDtoFromDepartment(parent) : null);
//        departmentDto.setLevel(department.getLevel());
//        return departmentDto;
        return modelMapper.map(department, DepartmentDto.class);
    }

}
