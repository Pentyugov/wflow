package com.pentyugov.wflow.web.feign.fallback;

import com.pentyugov.wflow.web.feign.dto.TaskFeignDto;
import com.pentyugov.wflow.web.feign.service.TaskFeignService;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskFallBack implements TaskFeignService {
    @Override
    public ResponseEntity<Object> getAll(HttpHeaders headers) {
        return getFallBackResponse();
    }

    @Override
    public ResponseEntity<Object> getById(HttpHeaders headers, String id) {
        return getFallBackResponse();
    }

    @Override
    public ResponseEntity<Object> getFiltered(HttpHeaders headers, FiltersRequest request) {
        return getFallBackResponse();
    }

    @Override
    public ResponseEntity<Object> getTaskHistory(HttpHeaders headers, String id) {
        return getFallBackResponse();
    }

    @Override
    public ResponseEntity<Object> add(HttpHeaders headers, TaskFeignDto task) {
        return getFallBackResponse();
    }

    @Override
    public ResponseEntity<Object> update(HttpHeaders headers, TaskFeignDto task) {
        return getFallBackResponse();
    }

    @Override
    public ResponseEntity<Object> update(HttpHeaders headers, String id) {
        return getFallBackResponse();
    }

    private ResponseEntity<Object> getFallBackResponse() {
        return ResponseEntity.internalServerError().body("ERROR: Service is not available now");
    }
}
