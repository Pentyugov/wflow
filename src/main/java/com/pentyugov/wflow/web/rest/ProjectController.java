package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Project;
import com.pentyugov.wflow.core.dto.ProjectDto;
import com.pentyugov.wflow.core.service.ProjectService;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")

public class ProjectController  extends ExceptionHandling {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllProjects() {
        List<ProjectDto> projects =
            projectService.getAllProjects().stream().map(projectService::createProjectDto).collect(Collectors.toList());
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<Object> getAvailableProjects() {
        List<ProjectDto> projects = projectService.getAvailable();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) throws ProjectNotFoundException {
        return new ResponseEntity<>(projectService.getProjectDtoById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addProject(@RequestBody ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException {
        Project project = projectService.createNewProject(projectDto);
        return new ResponseEntity<>(projectService.createProjectDto(project), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateProject(@RequestBody ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException, ProjectNotFoundException {
        Project project = projectService.updateProject(projectDto);
        return new ResponseEntity<>(projectService.createProjectDto(project), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteProject(@PathVariable String id) {
        projectService.deleteProject(UUID.fromString(id));
        String message = String.format("Project with id: %s was deleted", id);
        return response(message);
    }

    private ResponseEntity<HttpResponse> response(String message) {
        HttpResponse body = new HttpResponse();
        body.setTimeStamp(new Date());
        body.setHttpStatus(HttpStatus.OK);
        body.setHttpStatusCode(HttpStatus.OK.value());
        body.setReason(HttpStatus.OK.getReasonPhrase().toUpperCase());
        body.setMessage(message);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
