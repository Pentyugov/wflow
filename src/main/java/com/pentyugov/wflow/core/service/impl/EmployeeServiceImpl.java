package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Employee;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.EmployeeDto;
import com.pentyugov.wflow.core.repository.EmployeeRepository;
import com.pentyugov.wflow.core.service.*;
import com.pentyugov.wflow.web.exception.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service(EmployeeService.NAME)
@RequiredArgsConstructor
public class EmployeeServiceImpl extends AbstractService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final PositionService positionService;
    private final ValidationService validationService;
    private final UserSettingsService userSettingsService;
    private final ModelMapper modelMapper;
    private final UserSessionService userSessionService;

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(UUID id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id).orElseThrow(() ->
                new EmployeeNotFoundException(getMessage(
                        "exception.employee.not.found",
                        getLocale(userSessionService.getCurrentUser()),
                        "id",
                        id.toString()
                )));
    }

    @Override
    public Employee add(EmployeeDto employeeDto)
            throws PositionNotFoundException, UserNotFoundException, EmployeeExistException,
            ValidationException, DepartmentNotFoundException {

        Employee employee = convert(employeeDto);
        validateEmployee(employee, false, getLocale(userSessionService.getCurrentUser()));
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(EmployeeDto employeeDto)
            throws PositionNotFoundException, UserNotFoundException, DepartmentNotFoundException,
            ValidationException, EmployeeExistException {

        Employee employee = convert(employeeDto);
        validateEmployee(employee, true, getLocale(userSessionService.getCurrentUser()));
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(UUID id) {
        employeeRepository.delete(id);
    }

    @Override
    public void updateAll(List<EmployeeDto> employeeDtos)
            throws PositionNotFoundException, UserNotFoundException, DepartmentNotFoundException {

        for (EmployeeDto employeeDto : employeeDtos) {
            Employee employee = convert(employeeDto);
            if (!ObjectUtils.isEmpty(employeeDto.getDepartment())) {
                employee.setDepartment(departmentService.getById(employeeDto.getDepartment().getId()));
            }
            employeeRepository.save(employee);
        }
    }

    @Override
    public Employee convert(EmployeeDto employeeDto)
            throws PositionNotFoundException, DepartmentNotFoundException, UserNotFoundException {

        Employee employee = employeeRepository.findById(employeeDto.getId()).orElse(new Employee());
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
            employee.setDepartment(departmentService.getById(employeeDto.getDepartment().getId()));
        } else {
            employee.setDepartment(null);
        }
        if (!ObjectUtils.isEmpty(employeeDto.getUser())) {
            employee.setUser(userService.getById(employeeDto.getUser().getId()));
        }

        return employee;
    }

    @Override
    public EmployeeDto convert(Employee employee) {
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        if (!ObjectUtils.isEmpty(employee.getDepartment())) {
            employeeDto.setDepartment(departmentService.convert(employee.getDepartment()));
        }
        if (!ObjectUtils.isEmpty(employee.getUser())) {
            employeeDto.setUser(userService.convert(employee.getUser()));
        }

        return employeeDto;
    }

    @Override
    public List<Employee> getByDepartment(UUID departmentId) {
        return employeeRepository.findByEmployeesDepartment(departmentId);
    }

    private void validateEmployee(Employee employee, boolean isUpdate, Locale locale)
            throws EmployeeExistException, ValidationException {

        if (isUpdate) {
            Employee existed = employeeRepository.findByEmail(employee.getEmail()).orElse(null);
            if (!ObjectUtils.isEmpty(existed) && !existed.getId().equals(employee.getId())) {
                throw new EmployeeExistException(getMessage(
                        "exception.employee.exist",
                        locale,
                        "email",
                        employee.getEmail()));
            }

            if (BooleanUtils.isTrue(employee.getHead()) && !ObjectUtils.isEmpty(employee.getDepartment())) {
                existed = employeeRepository.findHeadOfDepartment(employee.getDepartment().getId()).orElse(null);
                if (!ObjectUtils.isEmpty(existed) && !existed.getId().equals(employee.getId())) {
                    throw new EmployeeExistException(getMessage(
                            "exception.employee.head.exist",
                            locale,
                            employee.getDepartment().getName()
                    ));
                }
            }

            if (!ObjectUtils.isEmpty(employee.getUser())) {
                Employee e = employeeRepository.findByUserId(employee.getUser().getId()).orElse(null);
                if (!ObjectUtils.isEmpty(e) && !employee.getId().equals(e.getId())) {
                    throw new EmployeeExistException(getMessage("exception.employee.exist",
                            locale,
                            "user",
                            employee.getUser().getUsername()
                    ));
                }
            }

            if (!ObjectUtils.isEmpty(employee.getPersonnelNumber())) {
                Employee e = employeeRepository.findByPersonnelNumber(employee.getPersonnelNumber()).orElse(null);
                if (!ObjectUtils.isEmpty(e) && !employee.getId().equals(e.getId())) {
                    throw new EmployeeExistException(getMessage("exception.employee.exist",
                            locale,
                            "personnel number",
                            employee.getPersonnelNumber()
                    ));
                }
            }

        } else {
            if (!ObjectUtils.isEmpty(employeeRepository.findByEmail(employee.getEmail()))) {
                throw new EmployeeExistException(getMessage("exception.employee.exist",
                        locale,
                        "email",
                        employee.getEmail()));
            }

            if (!ObjectUtils.isEmpty(employeeRepository.findByPersonnelNumber(employee.getPersonnelNumber()))) {
                throw new EmployeeExistException(getMessage("exception.employee.exist",
                        locale,
                        "personnel number",
                        employee.getPersonnelNumber()));
            }

            if (BooleanUtils.isTrue(employee.getHead()) && !ObjectUtils.isEmpty(
                    employeeRepository.findHeadOfDepartment(employee.getDepartment().getId())
            )) {
                throw new EmployeeExistException(getMessage(
                        "exception.employee.head.exist",
                        locale,
                        employee.getDepartment().getName()
                ));
            }

            if (!ObjectUtils.isEmpty(employee.getUser())) {
                Employee e = employeeRepository.findByUserId(employee.getUser().getId()).orElse(null);
                if (!ObjectUtils.isEmpty(e)) {
                    throw new EmployeeExistException(getMessage(
                            "exception.employee.exist",
                            locale,
                            "user",
                            employee.getUser().getUsername()
                    ));
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

        if ((!ObjectUtils.isEmpty(employee.getDismissalDate()) && !ObjectUtils.isEmpty(employee.getHireDate()))
                && employee.getDismissalDate().before(employee.getHireDate())) {
            throw new ValidationException(getMessage("exception.validation.invalid.dismissal.date", locale));
        }
    }

    private Locale getLocale(User user) {
        UserSettings userSettings = this.userSettingsService.getUserSettings(user);
        return new Locale(userSettings.getLocale());
    }
}
