package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Issue;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.service.IssueService;
import com.pentyugov.wflow.core.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(WorkflowService.NAME)
public class WorkflowServiceImpl implements WorkflowService {

    private final IssueService issueService;

    @Autowired
    public WorkflowServiceImpl(IssueService issueService) {
        this.issueService = issueService;
    }

    public Task startTaskProcess(Task task, User currentUser) {
        Issue issue = issueService.createIssue(task, currentUser, task.getInitiator(), task.getExecutor());
        issue.setResult(Task.STATE_ASSIGNED);
        issue = issueService.saveIssue(issue);
        task.setIssue(issue);
        task.setStarted(true);
        task.setState(Task.STATE_ASSIGNED);
        return task;
    }

    public Task cancelTaskProcess(Task task, User currentUser, String comment) {
        Issue issue = createIssue(task, currentUser);
        issue.setUser(currentUser);
        issue.setResult(Task.STATE_CANCELED);
        issue.setComment(comment);
        issue = issueService.saveIssue(issue);
        task.setIssue(issue);
        task.setStarted(false);
        task.setState(Task.STATE_CANCELED);
        return task;
    }

    public Task executeTask(Task task, User currentUser, String comment) {
        Issue issue = createIssue(task, currentUser);
        issue.setUser(currentUser);
        issue.setResult(Task.STATE_EXECUTED);
        issue.setComment(comment);
        issue = issueService.saveIssue(issue);
        task.setIssue(issue);
        task.setExecutionDateFact(new Date());
        task.setOverdue(task.getExecutionDatePlan().before(task.getExecutionDateFact()));
        task.setState(Task.STATE_EXECUTED);
        return task;
    }

    public Task reworkTask(Task task, User currentUser, String comment) {
        Issue issue = createIssue(task, currentUser);
        issue.setUser(currentUser);
        issue.setResult(Task.STATE_REWORK);
        issue.setComment(comment);
        issue = issueService.saveIssue(issue);
        task.setIssue(issue);
        task.setExecutionDateFact(new Date());
        task.setState(Task.STATE_REWORK);
        return task;
    }

    public Task finishTask(Task task, User currentUser, String comment) {
        Issue issue = createIssue(task, currentUser);
        issue.setUser(currentUser);
        issue.setResult(Task.STATE_FINISHED);
        issue.setComment(comment);
        issue = issueService.saveIssue(issue);
        task.setIssue(issue);
        task.setStarted(false);
        task.setExecutionDateFact(new Date());
        task.setState(Task.STATE_FINISHED);
        return task;
    }

    private Issue createIssue(Task task, User currentUser) {
        return issueService.createIssue(task, currentUser, task.getInitiator(), task.getExecutor());
    }
}
