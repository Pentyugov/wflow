package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.service.GroovyService;
import com.pentyugov.wflow.web.payload.request.GroovyRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/actuator")
@AllArgsConstructor
public class ActuatorController {

    private final GroovyService groovyService;

    @PostMapping("/groovy/execute")
    public ResponseEntity<Object> executeGroovyScript(@RequestBody GroovyRequest request) {
        return new ResponseEntity<>(groovyService.execute(request), HttpStatus.OK);
    }

}
