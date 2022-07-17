package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Note;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.NoteDto;
import com.pentyugov.wflow.core.repository.NoteRepository;
import com.pentyugov.wflow.core.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(NoteService.NAME)
public class NoteServiceImpl extends AbstractService implements NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteDto> getNotesByUser(UUID userId) {
        List<NoteDto> result = new ArrayList<>();
        noteRepository.findAllByUser(userId).forEach(note -> result.add(createNoteDtoFromNote(note)));
        return result;
    }

    public Note createNewNote(NoteDto noteDto, User user) {
        Note note = createNoteFromNoteDto(noteDto, user);
        return noteRepository.save(note);
    }

    public Note updateNote(NoteDto noteDto) {
        Note note = noteRepository.findById(noteDto.getId()).orElseThrow(null);
        if (note != null) {
            note.setCategory(noteDto.getCategory());
            note.setDescription(noteDto.getDescription());
            note.setTitle(noteDto.getTitle());
            note.setColor(noteDto.getColor());
            return noteRepository.save(note);
        }
        return null;
    }

    public void deleteNote(UUID id) {
        noteRepository.delete(id);
    }

    public NoteDto createNoteDtoFromNote(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setCategory(note.getCategory());
        noteDto.setTitle(note.getTitle());
        noteDto.setDescription(note.getDescription());
        noteDto.setColor(note.getColor());
        noteDto.setDate(Date.from(note.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()));
        return noteDto;
    }

    public Note createNoteFromNoteDto(NoteDto noteDto, User user) {
        Note note = new Note();
        note.setId(noteDto.getId());
        note.setCategory(noteDto.getCategory());
        note.setTitle(noteDto.getTitle());
        note.setDescription(noteDto.getDescription());
        note.setColor(noteDto.getColor());
        note.setUser(user);
        return note;
    }
}
