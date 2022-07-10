package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Employee;
import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.EmployeeDto;
import com.pentyugov.wflow.core.repository.EmployeeRepository;
import com.pentyugov.wflow.core.service.*;
import com.pentyugov.wflow.web.exception.*;
import org.apache.commons.lang3.BooleanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service(EmployeeService.NAME)
public class EmployeeServiceImpl extends AbstractService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final PositionService positionService;
    private final ValidationService validationService;
    private final UserSettingsService userSettingsService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentService departmentService,
                               UserService userService,
                               PositionService positionService,
                               ValidationService validationService, UserSettingsService userSettingsService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
        this.userService = userService;
        this.positionService = positionService;
        this.validationService = validationService;
        this.userSettingsService = userSettingsService;
        this.modelMapper = modelMapper;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id, Principal principal) throws EmployeeNotFoundException, UserNotFoundException {
        Locale locale = getLocale(principal);
        return employeeRepository.findById(id).orElseThrow(() ->
                new EmployeeNotFoundException(getMessage("exception.employee.not.found", locale, "id", id.toString())));
    }

    public Employee addNewEmployee(EmployeeDto employeeDto, Principal principal) throws PositionNotFoundException, UserNotFoundException, EmployeeExistException, ValidationException, DepartmentNotFoundException {
        Employee employee = createEmployeeFromDto(employeeDto);
        validateEmployee(employee, false, getLocale(principal));
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(EmployeeDto employeeDto, Principal principal) throws PositionNotFoundException, UserNotFoundException, DepartmentNotFoundException, ValidationException, EmployeeExistException {
        Employee employee = createEmployeeFromDto(employeeDto);
        validateEmployee(employee, true, getLocale(principal));
        return employeeRepository.save(employee);
    }


    public void updateAll(List<EmployeeDto> employeeDtos) throws PositionNotFoundException, UserNotFoundException, DepartmentNotFoundException {
        for (EmployeeDto employeeDto : employeeDtos) {
            Employee employee = createEmployeeFromDto(employeeDto);
            if (!ObjectUtils.isEmpty(employeeDto.getDepartment())) {
                employee.setDepartment(departmentService.getDepartmentById(employeeDto.getDepartment().getId()));
            }
            employeeRepository.save(employee);
        }
    }

    private void validateEmployee(Employee employee, boolean isUpdate, Locale locale) throws EmployeeExistException, ValidationException {
        if (isUpdate) {
            Employee existed = employeeRepository.findByEmail(employee.getEmail()).orElse(null);
            if (!ObjectUtils.isEmpty(existed) && !existed.getId().equals(employee.getId())) {
                throw new EmployeeExistException(getMessage("exception.employee.exist", locale, "email", employee.getEmail()));
            }

            if (BooleanUtils.isTrue(employee.getHead()) && !ObjectUtils.isEmpty(employee.getDepartment())) {
                existed = employeeRepository.findHeadOfDepartment(employee.getDepartment().getId()).orElse(null);
                if (!ObjectUtils.isEmpty(existed) && !existed.getId().equals(employee.getId())) {
                    throw new EmployeeExistException(getMessage("exception.employee.head.exist", locale, employee.getDepartment().getName()));
                }
            }

            if (!ObjectUtils.isEmpty(employee.getUser())) {
                Employee e = employeeRepository.findByUserId(employee.getUser().getId()).orElse(null);
                if (!ObjectUtils.isEmpty(e) && !employee.getId().equals(e.getId())) {
                    throw new EmployeeExistException(getMessage("exception.employee.exist", locale, "user", employee.getUser().getUsername()));
                }
            }

            if (!ObjectUtils.isEmpty(employee.getPersonnelNumber())) {
                Employee e = employeeRepository.findByPersonnelNumber(employee.getPersonnelNumber()).orElse(null);
                if (!ObjectUtils.isEmpty(e) && !employee.getId().equals(e.getId())) {
                    throw new EmployeeExistException(getMessage("exception.employee.exist", locale, "personnel number", employee.getPersonnelNumber()));
                }
            }

        } else {
            if(!ObjectUtils.isEmpty(employeeRepository.findByEmail(employee.getEmail()))) {
                throw new EmployeeExistException(getMessage("exception.employee.exist", locale, "email", employee.getEmail()));
            }

            if (!ObjectUtils.isEmpty(employeeRepository.findByPersonnelNumber(employee.getPersonnelNumber()))) {
                throw new EmployeeExistException(getMessage("exception.employee.exist", locale, "personnel number", employee.getPersonnelNumber()));
            }

            if (BooleanUtils.isTrue(employee.getHead()) && !ObjectUtils.isEmpty(employeeRepository.findHeadOfDepartment(employee.getDepartment().getId()))) {
                throw new EmployeeExistException(getMessage("exception.employee.head.exist", locale, employee.getDepartment().getName()));
            }

            if (!ObjectUtils.isEmpty(employee.getUser())) {
                Employee e = employeeRepository.findByUserId(employee.getUser().getId()).orElse(null);
                if (!ObjectUtils.isEmpty(e)) {
                    throw new EmployeeExistException(getMessage("exception.employee.exist", locale, "user", employee.getUser().getUsername()));
                }
            }
        }

        if (StringUtils.hasText(employee.getEmail()) && !validationService.isEmailValid(employee.getEmail())) {
            throw new ValidationException("Invalid email");
        }

        if (StringUtils.hasText(employee.getPhoneNumber())) {
            String parsed = validationService.parsePhoneNumber(employee.getPhoneNumber());
            if (ObjectUtils.isEmpty(parsed)) {
                throw new ValidationException("Invalid phone number");
            } else {
                employee.setPhoneNumber("+" + parsed);
            }
        }

        if ((!ObjectUtils.isEmpty(employee.getDismissalDate()) && !ObjectUtils.isEmpty(employee.getHireDate())) &&
                employee.getDismissalDate().before(employee.getHireDate())) {
            throw new ValidationException(getMessage("exception.validation.invalid.dismissal.date", locale));
        }
    }

    public Employee createEmployeeFromDto(EmployeeDto employeeDto) throws PositionNotFoundException, DepartmentNotFoundException, UserNotFoundException {
        Employee employee = null;
        if (!ObjectUtils.isEmpty(employeeDto.getId())) {
            employee = employeeRepository.getById(employeeDto.getId());
        }

        if (ObjectUtils.isEmpty(employee)) {
            employee = new Employee();
            employee.setId(employeeDto.getId());
        }
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setMiddleName(employeeDto.getMiddleName());
        employee.setSalary(employeeDto.getSalary());
        employee.setHireDate(employeeDto.getHireDate());
        employee.setDismissalDate(employeeDto.getDismissalDate());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setEmail(employeeDto.getEmail());
        employee.setHead(employeeDto.getHead());
        employee.setPersonnelNumber(employeeDto.getPersonnelNumber());

        if (!ObjectUtils.isEmpty(employeeDto.getPosition())) {
            employee.setPosition(positionService.getById(employeeDto.getPosition().getId()));
        }
        if (!ObjectUtils.isEmpty(employeeDto.getDepartment())) {
            employee.setDepartment(departmentService.getDepartmentById(employeeDto.getDepartment().getId()));
        } else {
            employee.setDepartment(null);
        }
        if (!ObjectUtils.isEmpty(employeeDto.getUser())) {
            employee.setUser(userService.getUserById(employeeDto.getUser().getId()));
        }

        return employee;
    }

    public EmployeeDto createEmployeeDtoFromEmployee(Employee employee) {
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        if (!ObjectUtils.isEmpty(employee.getDepartment())) {
            employeeDto.setDepartment(departmentService.createDepartmentDtoFromDepartment(employee.getDepartment()));
        }
        if (!ObjectUtils.isEmpty(employee.getUser())) {
            employeeDto.setUser(userService.createUserDtoFromUser(employee.getUser()));
        }

        return employeeDto;
    }

    public List<Employee> getEmployeesByDepartment(UUID departmentId) {
        return employeeRepository.findByEmployeesDepartment(departmentId);
    }

    private Locale getLocale(Principal principal) throws UserNotFoundException {
        UserSettings userSettings = this.userSettingsService.getUserSettings(this.userService.getUserByPrincipal(principal));
        return new Locale(userSettings.getLocale());
    }

    public void deleteEmployee(UUID id) {
        employeeRepository.delete(id);
    }
}