package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Project;
import com.pentyugov.wflow.core.dto.ProjectDto;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public interface ProjectService {
    String NAME = "wflow$ProjectService";

    List<Project> getAll();

    Project getById(UUID id) throws ProjectNotFoundException;

    Project add(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException;

    Project update(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException, ProjectNotFoundException;

    void delete(UUID id);

    List<ProjectDto> getAvailable();

    ProjectDto getProjectDtoById(UUID id) throws ProjectNotFoundException;

    Project convert(ProjectDto projectDto);

    ProjectDto convert(Project project);

}
