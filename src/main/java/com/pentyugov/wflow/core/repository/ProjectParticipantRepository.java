package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.ProjectParticipant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProjectParticipantRepository extends BaseRepository<ProjectParticipant> {

    @Transactional
    @Modifying
    @Query("delete from workflow$projectParticipant pp where pp.project.id = ?1")
    void removeAllForProject(UUID projectId);
}
