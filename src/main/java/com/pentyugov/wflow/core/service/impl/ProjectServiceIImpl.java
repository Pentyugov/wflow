package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Project;
import com.pentyugov.wflow.core.domain.entity.ProjectParticipant;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.ProjectDto;
import com.pentyugov.wflow.core.repository.ProjectParticipantRepository;
import com.pentyugov.wflow.core.repository.ProjectRepository;
import com.pentyugov.wflow.core.service.ContractorService;
import com.pentyugov.wflow.core.service.ProjectService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.pentyugov.wflow.core.domain.entity.Role.PROJECT_MANAGER;

@Service(ProjectService.NAME)
@RequiredArgsConstructor
public class ProjectServiceIImpl extends AbstractService implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceIImpl.class);

    private final ProjectRepository projectRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final UserService userService;
    private final ContractorService contractorService;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<ProjectDto> getAvailable(Principal principal) throws UserNotFoundException {
        User currentUser = userService.getUserByPrincipal(principal);
        if (userService.isUserAdmin(currentUser) || userService.isUserInRole(currentUser, PROJECT_MANAGER)) {
            return projectRepository.findAll()
                    .stream()
                    .map(this::createProjectDto)
                    .collect(Collectors.toList());
        }
        List<Project> projects = projectRepository.getAvailableProjectsForUser(userService.getUserByPrincipal(principal).getId());
        return projects
                .stream()
                .map(this::createProjectDto)
                .collect(Collectors.toList());
    }

    @Override
    public Project getProjectById(UUID id) throws ProjectNotFoundException {
        LOGGER.info("trying to find project with id" + id);
        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException(getMessage("exception.user.with.id.not.found", id.toString())));
    }

    @Override
    public ProjectDto getProjectDtoById(UUID id) throws ProjectNotFoundException {
        return createProjectDto(getProjectById(id));
    }

    @Override
    public Project createNewProject(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setCode(projectDto.getCode());
        project.setStatus(projectDto.getStatus());
        project.setDueDate(projectDto.getDueDate());
        project.setConclusionDate(projectDto.getConclusionDate());
        project.setClosingDate(projectDto.getClosingDate());
        if (projectDto.getContractor() != null) {
            project.setContractor(contractorService.getContractorById(projectDto.getContractor().getId()));
        }
        project.setProjectManager(userService.getUserById(projectDto.getProjectManager().getId()));
        project.setProjectParticipants(updateProjectParticipants(
                project, projectDto.getParticipants()
                        .stream()
                        .map(userService::createUserFromDto)
                        .collect(Collectors.toList())));
        if (!ObjectUtils.isEmpty(projectDto.getProjectManager())) {
            project.setProjectManager(userService.getUserById(projectDto.getProjectManager().getId()));
        }
        if (!ObjectUtils.isEmpty(projectDto.getContractor())) {
            project.setContractor(contractorService.getContractorById(projectDto.getContractor().getId()));
        }

        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public Project updateProject(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException, ProjectNotFoundException {
        Project project = projectRepository.findById(projectDto.getId()).orElseThrow(
                () -> new ProjectNotFoundException(getMessage("exception.user.with.id.not.found", projectDto.getId().toString()))
        );
        project.setName(projectDto.getName());
        project.setCode(projectDto.getCode());
        project.setStatus(projectDto.getStatus());
        project.setClosingDate(projectDto.getClosingDate());
        project.setDueDate(projectDto.getDueDate());
        project.setConclusionDate(projectDto.getConclusionDate());
        if (!ObjectUtils.isEmpty(projectDto.getProjectManager())) {
            project.setProjectManager(userService.getUserById(projectDto.getProjectManager().getId()));
        }
        project.setProjectParticipants(updateProjectParticipants(
                project, projectDto.getParticipants()
                        .stream()
                        .map(userService::createUserFromDto)
                        .collect(Collectors.toList())));

        if (!ObjectUtils.isEmpty(projectDto.getContractor())) {
            project.setContractor(contractorService.getContractorById(projectDto.getContractor().getId()));
        } else {
            project.setContractor(null);
        }
        return projectRepository.save(project);

    }

    @Override
    public void deleteProject(UUID id) {
        this.projectRepository.delete(id);
    }

    public List<ProjectParticipant> updateProjectParticipants(Project project, List<User> newParticipants) {
        projectParticipantRepository.removeAllForProject(project.getId());

        List<ProjectParticipant> projectParticipants = new ArrayList<>();

        ProjectParticipant pm = new ProjectParticipant();
        pm.setProject(project);
        pm.setUser(project.getProjectManager());
        projectParticipants.add(pm);

        newParticipants.forEach(user -> {
            if (!user.equals(project.getProjectManager())) {
                ProjectParticipant pp = new ProjectParticipant();
                pp.setProject(project);
                try {
                    pp.setUser(userService.getUserById(user.getId()));
                    projectParticipants.add(pp);
                } catch (UserNotFoundException e) {
                    LOGGER.error("NO USER WITH ID " + user.getId());
                }
            }
        });

        return projectParticipantRepository.saveAll(projectParticipants);
    }


    public ProjectDto createProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setCode(project.getCode());
        projectDto.setStatus(project.getStatus());
        projectDto.setClosingDate(project.getClosingDate());
        projectDto.setDueDate(project.getDueDate());
        projectDto.setConclusionDate(project.getConclusionDate());
        projectDto.setParticipants(project
                .getProjectParticipants()
                .stream()
                .map(projectParticipant -> userService.createUserDtoFromUser(projectParticipant.getUser()))
                .collect(Collectors.toList()));
        projectDto.setProjectManager(userService.createUserDtoFromUser(project.getProjectManager()));

        if (project.getContractor() != null) {
            projectDto.setContractor(contractorService.createContractorDto(project.getContractor()));
        }


        return projectDto;
    }


}
