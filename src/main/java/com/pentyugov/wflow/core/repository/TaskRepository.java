package com.pentyugov.wflow.core.repository;


import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends BaseRepository<Task> {

    @Transactional(readOnly = true)
    @Query("select t from workflow$Task t where t.priority = ?1 and " +
            "(t.creator.id =?2 or t.executor.id = ?2 or t.initiator.id = ?2)")
    List<Task> findAllByPriorityForUser(int priority, UUID userId);

    @Transactional(readOnly = true)
    @Query("select t from workflow$Task t where t.priority = ?1 and t.executor.id = ?2 and t.started = TRUE and t in ?3")
    List<Task> findAllByPriorityWhereUserExecutor(int priority, UUID userId, List<Card> cards);

    @Transactional(readOnly = true)
    Page<Task> findByIdInOrInitiatorId(Collection<UUID> id, UUID initiatorId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("select t from workflow$Task t where t.creator.id =?1 or t.executor.id = ?1 or t.initiator.id = ?1")
    List<Task> findAllForUser(UUID userId);

    @Transactional(readOnly = true)
    @Query("select t from workflow$Task t where t.executor.id = ?1 and t.started = TRUE and t.state in ?2")
    List<Task> findActiveForExecutor(UUID userId, List<String> states);

    @Transactional(readOnly = true)
    @Query("select t from workflow$Task t where t.started = TRUE and t.executionDatePlan is not NULL")
    List<Task> findActiveWithDueDate();

    @Transactional(readOnly = true)
    @Query("select t from workflow$Task t where t.id in ?1")
    Page<Task> findActiveTasksPage(List<UUID> taskIds, Pageable pageable);

}
