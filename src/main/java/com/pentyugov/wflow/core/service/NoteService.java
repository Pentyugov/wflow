package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Note;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.NoteDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface NoteService {

    String NAME = "wflow$NoteService";

    List<Note> getForUser(UUID userId);

    Note add(NoteDto noteDto);

    Note update(NoteDto noteDto);

    void delete(UUID id);

    NoteDto convert(Note note);

    Note convert(NoteDto noteDto);
}
