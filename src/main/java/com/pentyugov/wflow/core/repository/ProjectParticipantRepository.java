package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.Project;
import com.pentyugov.wflow.core.domain.entity.ProjectParticipant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProjectParticipantRepository extends BaseRepository<ProjectParticipant> {

    @Transactional(readOnly = true)
    @Query("select distinct pp.project from workflow$projectParticipant pp ")
    List<Project> getAvailableProjectsForUser(UUID userId);

    @Transactional(readOnly = true)
    @Query("select pp from workflow$projectParticipant pp where pp.project.id = ?1")
    List<ProjectParticipant> getAllByProject(UUID projectId);

    @Transactional(readOnly = true)
    @Query("select pp from workflow$projectParticipant pp where pp.project.id = ?1 and pp.user.id in ?2")
    List<ProjectParticipant> getAllByParticipantsForProject(UUID projectId, List<UUID> participantsIds);

    @Transactional
    @Modifying
    @Query("delete from workflow$projectParticipant pp where pp.project.id = ?1")
    void removeAllForProject(UUID projectId);
}
