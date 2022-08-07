package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Note;
import com.pentyugov.wflow.core.dto.NoteDto;
import com.pentyugov.wflow.core.repository.NoteRepository;
import com.pentyugov.wflow.core.service.NoteService;
import com.pentyugov.wflow.core.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(NoteService.NAME)
@RequiredArgsConstructor
public class NoteServiceImpl extends AbstractService implements NoteService {

    private final NoteRepository noteRepository;
    private final UserSessionService userSessionService;

    @Override
    public List<Note> getForUser(UUID userId) {
        return noteRepository.findAllByUser(userId);
    }

    @Override
    public Note add(NoteDto noteDto) {
        Note note = convert(noteDto);
        return noteRepository.save(note);
    }

    @Override
    public Note update(NoteDto noteDto) {
        Note note = convert(noteDto);
        return noteRepository.save(note);
    }

    @Override
    public void delete(UUID id) {
        noteRepository.delete(id);
    }

    @Override
    public Note convert(NoteDto noteDto) {
        Note note = noteRepository.findById(noteDto.getId()).orElse(new Note());
        note.setId(noteDto.getId());
        note.setCategory(noteDto.getCategory());
        note.setTitle(noteDto.getTitle());
        note.setDescription(noteDto.getDescription());
        note.setColor(noteDto.getColor());
        note.setUser(userSessionService.getCurrentUser());
        return note;
    }

    @Override
    public NoteDto convert(Note note) {
        return NoteDto.builder()
                .id(note.getId())
                .category(note.getCategory())
                .title(note.getTitle())
                .description(note.getDescription())
                .color(note.getColor())
                .date(Date.from(note.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }

}
