package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Note;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.NoteDto;
import com.pentyugov.wflow.core.service.NoteService;
import com.pentyugov.wflow.core.service.UserSessionService;
import com.pentyugov.wflow.web.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController extends AbstractController {

    private final NoteService noteService;
    private final UserSessionService userSessionService;

    @GetMapping
    public ResponseEntity<Object> getNotesByUser() {
        User user = userSessionService.getCurrentUser();
        List<NoteDto> notes = noteService.getNotesByUser(user.getId());
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpResponse> addNewNote(@RequestBody NoteDto newNote) {
        User user = userSessionService.getCurrentUser();
        Note note = noteService.createNewNote(newNote, user);
        if (note != null) {
            return response(HttpStatus.CREATED, HttpStatus.CREATED.getReasonPhrase());
        }
        return response(HttpStatus.NOT_MODIFIED, HttpStatus.NOT_MODIFIED.getReasonPhrase());
    }

    @PutMapping
    public ResponseEntity<HttpResponse> updateNote(@RequestBody NoteDto noteDto) {
        Note note = noteService.updateNote(noteDto);
        if (note != null) {
            return response(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
        }
        return response(HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.getReasonPhrase());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteNote(@PathVariable String id) {
        noteService.deleteNote(UUID.fromString(id));
        return response(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
    }

}
