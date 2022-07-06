package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Issue;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.CardHistoryDto;

import java.util.List;


public interface IssueService {

    String NAME = "wflow$IssueService";

    Issue saveIssue(Issue issue);

    List<Issue> getIssuesByInitiator(User initiator);

    List<Issue> getIssuesByExecutor(User executor);

    List<Card> getCardsByInitiator(User initiator);

    List<Card> getCardsByExecutor(User executor);

    List<Card> getAllCardsForUser(User user);

    Issue createIssue(Card card, User currentUser, User initiator, User executor);

    List<Issue> getAllIssuesByCard(Card card);

    CardHistoryDto createCardHistoryDto(Issue issue);
}
