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

    List<Project> getAllProjects();

    List<ProjectDto> getAvailable(Principal principal) throws UserNotFoundException;

    Project getProjectById(UUID id) throws ProjectNotFoundException;

    ProjectDto getProjectDtoById(UUID id) throws ProjectNotFoundException;

    ProjectDto createProjectDto(Project project);

    Project updateProject(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException;

    void deleteProject(UUID id);

    Project createNewProject(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException;
}
