package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.web.payload.request.FiltersRequest;

public interface FilterService {

    String NAME = "wflow$FilterService";

    String buildQueryString(Class<?> clazz, FiltersRequest filterRequest);
}
