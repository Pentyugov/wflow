package com.pentyugov.wflow.web.payload.request;

import java.util.List;
import java.util.UUID;

public class TaskFiltersRequest {
    private List<UUID> ids;
    private List<TaskFilter> taskFilters;

    public List<UUID> getIds() {
        return ids;
    }

    public void setIds(List<UUID> ids) {
        this.ids = ids;
    }

    public List<TaskFilter> getTaskFilters() {
        return taskFilters;
    }

    public void setTaskFilters(List<TaskFilter> taskFilters) {
        this.taskFilters = taskFilters;
    }

    public static class TaskFilter {
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
