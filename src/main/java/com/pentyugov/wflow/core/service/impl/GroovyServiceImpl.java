package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.service.GroovyService;
import com.pentyugov.wflow.web.payload.request.GroovyRequest;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.springframework.stereotype.Service;

@Service(GroovyService.NAME)
public class GroovyServiceImpl implements GroovyService {

    private final GroovyShell shell = new GroovyShell(new Binding());

    @Override
    public Object execute(String scriptString) {

        try {
            Script script = shell.parse(scriptString);
            return script.run();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Override
    public Object execute(GroovyRequest groovyRequest) {
        return execute(groovyRequest.getScript());
    }

}
