package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class FiltersRequest {

    private List<UUID> ids;
    private List<Filter> filters;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Filter {
        private String property;
        private String condition;

    }
}
