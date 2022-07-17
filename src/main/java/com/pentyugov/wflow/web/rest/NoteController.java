package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Note;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.NoteDto;
import com.pentyugov.wflow.core.service.NoteService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
public class NoteController extends AbstractController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getNotesByUser(Principal principal) throws UserNotFoundException {
        User user = userService.getCurrentUser(principal);
        List<NoteDto> notes = noteService.getNotesByUser(user.getId());
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpResponse> addNewNote(@RequestBody NoteDto newNote, Principal principal) throws UserNotFoundException {
        User user = userService.getCurrentUser(principal);
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
