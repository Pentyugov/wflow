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

    List<NoteDto> getNotesByUser(UUID userId);

    Note createNewNote(NoteDto noteDto, User user);

    Note updateNote(NoteDto noteDto);

    void deleteNote(UUID id);

    NoteDto createNoteDtoFromNote(Note note);

    Note createNoteFromNoteDto(NoteDto noteDto, User user);
}
