package com.pentyugov.wflow.web.feign.service;

import com.pentyugov.wflow.web.feign.dto.TaskFeignDto;
import com.pentyugov.wflow.web.feign.fallback.TaskFallBack;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = TaskFeignService.WORKFLOW_SERVICE_NAME, fallback = TaskFallBack.class)
public interface TaskFeignService {

    String WORKFLOW_SERVICE_NAME = "wflow-workflow-service";
    String BASE_URL = "/api/v1/tasks/";
    @GetMapping(path = BASE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getAll(@RequestHeader HttpHeaders headers);

    @GetMapping(path = BASE_URL + "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getById(@RequestHeader HttpHeaders headers, @PathVariable String id);

    @GetMapping(path = BASE_URL + "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getFiltered(@RequestHeader HttpHeaders headers, @RequestBody FiltersRequest request);

    @GetMapping(path = BASE_URL + "{id}/history")
    ResponseEntity<Object> getTaskHistory(@RequestHeader HttpHeaders headers, @PathVariable String id);

    @PostMapping(path = BASE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> add(@RequestHeader HttpHeaders headers, @RequestBody TaskFeignDto task);

    @PutMapping(path = BASE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> update(@RequestHeader HttpHeaders headers, @RequestBody TaskFeignDto task);

    @DeleteMapping(path = BASE_URL + "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> update(@RequestHeader HttpHeaders headers, @PathVariable String id);


}
