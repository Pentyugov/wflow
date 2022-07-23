package com.pentyugov.wflow.web.payload.request;

import java.util.List;
import java.util.UUID;

public class FiltersRequest {
    private List<UUID> ids;
    private List<Filter> filters;

    public List<UUID> getIds() {
        return ids;
    }

    public void setIds(List<UUID> ids) {
        this.ids = ids;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public static class Filter {
        private String property;
        private String condition;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }
}
