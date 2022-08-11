package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.web.payload.request.GroovyRequest;

public interface GroovyService {
    String NAME = "wflow$GroovyService";

    Object execute(String script);

    Object execute(GroovyRequest groovyRequest);
}
