package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.application.utils.InstallerUtils;
import com.pentyugov.wflow.core.dto.EmployeeDto;
import com.pentyugov.wflow.core.service.ValidationService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@Validated
public class TestController extends ExceptionHandling {

    private final ValidationService validationService;
    private final InstallerUtils installerUtils;
    private final ApplicationContext context;

    @Autowired
    public TestController(ValidationService validationService, InstallerUtils installerUtils, ApplicationContext context) {

        this.validationService = validationService;
        this.installerUtils = installerUtils;
        this.context = context;
    }

    @PostMapping("/phone-validation")
    public ResponseEntity<Object> validatePhone(@RequestBody EmployeeDto employeeDto) {
        validationService.parsePhoneNumber(employeeDto.getPhoneNumber());
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @GetMapping("/ping")
    public ResponseEntity<Object> ping() {
        installerUtils.stopRedis();
        return new ResponseEntity<>("pinged", HttpStatus.OK);
    }

    @PostMapping("/shutdown")
    public void shutdownContext() {
        ((ConfigurableApplicationContext) context).close();
    }

}
