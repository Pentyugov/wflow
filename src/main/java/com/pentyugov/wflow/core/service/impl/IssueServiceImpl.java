package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Issue;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.CardHistoryDto;
import com.pentyugov.wflow.core.repository.IssueRepository;
import com.pentyugov.wflow.core.service.IssueService;
import com.pentyugov.wflow.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service(IssueService.NAME)
@RequiredArgsConstructor
public class IssueServiceImpl extends AbstractService implements IssueService {

    private final IssueRepository issueRepository;
    private final UserService userService;

    @Override
    public Issue saveIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    public List<Card> getCardsByExecutor(User executor) {
        return issueRepository.findCardByExecutorId(executor.getId(), Arrays.asList(Task.STATE_ASSIGNED, Task.STATE_REWORK));
    }

    public List<Card> getAllCardsForUser(User user) {
        return issueRepository.findCardsForUser(user.getId());
    }

    @Override
    public List<Card> findCardByExecutorIdAndResult(User user, String result) {
        return issueRepository.findCardByExecutorIdAndResult(user.getId(), result);
    }

    @Override
    public Issue createIssue(Card card, User currentUser, User initiator, User executor) {
        Issue issue = new Issue();
        issue.setCard(card);
        issue.setUser(currentUser);
        issue.setInitiator(initiator);
        issue.setExecutor(executor);
        return issue;
    }

    @Override
    public List<Issue> getAllIssuesByCard(Card card) {
        return issueRepository.findAllByCardId(card.getId());
    }

    @Override
    public CardHistoryDto createCardHistoryDto(Issue issue) {
        CardHistoryDto cardHistoryDto = new CardHistoryDto();
        cardHistoryDto.setId(issue.getId());
        cardHistoryDto.setCreateDate(Date.from(issue.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()));
        cardHistoryDto.setComment(issue.getComment());
        cardHistoryDto.setUser(userService.convert(issue.getUser()));
        cardHistoryDto.setResult(issue.getResult());
        return cardHistoryDto;
    }
}
