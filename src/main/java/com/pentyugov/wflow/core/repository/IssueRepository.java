package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Issue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IssueRepository extends BaseRepository<Issue> {

    @Transactional(readOnly = true)
    @Query("select distinct i from workflow$Issue i where i.card.id = ?1 order by i.createDate")
    List<Issue>  findAllByCardId(UUID cardId);

    @Transactional(readOnly = true)
    List<Issue> findAllByInitiatorId(UUID initiatorId);

    @Transactional(readOnly = true)
    List<Issue> findAllByExecutorId(UUID executorId);

    @Transactional(readOnly = true)
    @Query("select distinct i.card from workflow$Issue i where i.initiator.id = ?1")
    List<Card> findCardByInitiatorId(UUID initiatorId);

    @Transactional(readOnly = true)
    @Query("select distinct i.card from workflow$Issue i where i.executor.id = ?1 and i.card.state in ?2")
    List<Card> findCardByExecutorId(UUID executorId, List<String> results);

    @Transactional(readOnly = true)
    @Query("select distinct i.card from workflow$Issue i where i.executor.id = ?1 or i.initiator.id = ?1")
    List<Card> findCardsForUser(UUID userId);
}
