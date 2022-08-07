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
import com.pentyugov.wflow.core.service.UserSessionService;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    private final UserSessionService userSessionService;

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public List<ProjectDto> getAvailable() {
        if (userSessionService.isCurrentUserAdmin() || userSessionService.isUserInRole(PROJECT_MANAGER)) {
            return projectRepository.findAll()
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
        List<Project> projects = projectRepository.getAvailableProjectsForUser(userSessionService.getCurrentUser().getId());
        return projects
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Project getById(UUID id) throws ProjectNotFoundException {
        LOGGER.info("trying to find project with id" + id);
        return projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException(getMessage("exception.user.with.id.not.found", id.toString())));
    }

    @Override
    public ProjectDto getProjectDtoById(UUID id) throws ProjectNotFoundException {
        return convert(getById(id));
    }

    @Override
    public Project add(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException {
        Project project = convert(projectDto);
        return projectRepository.save(project);
    }

    @Override
    public Project update(ProjectDto projectDto) throws UserNotFoundException, ContractorNotFoundException,
            ProjectNotFoundException {

        Project project = convert(projectDto);
        return projectRepository.save(project);
    }

    @Override
    public void delete(UUID id) {
        this.projectRepository.delete(id);
    }

    @Override
    public Project convert(ProjectDto projectDto) {
        Project project = projectRepository.findById(projectDto.getId()).orElse(new Project());
        project.setName(projectDto.getName());
        project.setCode(projectDto.getCode());
        project.setStatus(projectDto.getStatus());
        project.setClosingDate(projectDto.getClosingDate());
        project.setDueDate(projectDto.getDueDate());
        project.setConclusionDate(projectDto.getConclusionDate());
        if (!ObjectUtils.isEmpty(projectDto.getProjectManager())) {
            project.setProjectManager(userService.getById(projectDto.getProjectManager().getId()));
        }
        project.setProjectParticipants(updateProjectParticipants(
                project, projectDto.getParticipants()
                        .stream()
                        .map(userService::convert)
                        .collect(Collectors.toList())));

        if (!ObjectUtils.isEmpty(projectDto.getContractor())) {
            project.setContractor(contractorService.getById(projectDto.getContractor().getId()));
        } else {
            project.setContractor(null);
        }
        return project;

    }

    @Override
    public ProjectDto convert(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .code(project.getCode())
                .status(project.getStatus())
                .closingDate(project.getClosingDate())
                .dueDate(project.getDueDate())
                .conclusionDate(project.getConclusionDate())
                .participants(
                        project
                        .getProjectParticipants()
                        .stream()
                        .map(projectParticipant -> userService.convert(projectParticipant.getUser()))
                        .collect(Collectors.toList())
                )
                .projectManager(userService.convert(project.getProjectManager()))
                .contractor(
                        project.getContractor() != null ? contractorService.convert(project.getContractor()) : null
                )
                .build();
    }

    private List<ProjectParticipant> updateProjectParticipants(Project project, List<User> newParticipants) {
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
                    pp.setUser(userService.getById(user.getId()));
                    projectParticipants.add(pp);
                } catch (UserNotFoundException e) {
                    LOGGER.error("NO USER WITH ID " + user.getId());
                }
            }
        });

        return projectParticipantRepository.saveAll(projectParticipants);
    }


}
