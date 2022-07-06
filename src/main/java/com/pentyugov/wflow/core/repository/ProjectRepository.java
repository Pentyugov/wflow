package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends BaseRepository<Project> {


    @Transactional(readOnly = true)
    @Query("select distinct pp.project from workflow$projectParticipant pp where pp.user.id = ?1 ")
    List<Project> getAvailableProjectsForUser(UUID userId);
}
