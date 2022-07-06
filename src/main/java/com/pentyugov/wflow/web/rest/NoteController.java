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
@RequestMapping("/api/note")
public class NoteController extends AbstractController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/get-all-for-current-user")
    public ResponseEntity<Object> getNotesByUser(Principal principal) throws UserNotFoundException {
        User user = userService.getCurrentUser(principal);
        List<NoteDto> notes = noteService.getNotesByUser(user.getId());
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PostMapping("/add-new-note")
    public ResponseEntity<NoteDto> addNewNote(@RequestBody NoteDto newNote, Principal principal) throws UserNotFoundException {
        User user = userService.getCurrentUser(principal);
        Note note = noteService.createNewNote(newNote, user);
        return new ResponseEntity<>(noteService.createNoteDtoFromNote(note), HttpStatus.OK);
    }

    @PutMapping("/update-note")
    public ResponseEntity<NoteDto> updateNote(@RequestBody NoteDto noteDto) {
        Note note = noteService.updateNote(noteDto);
        return new ResponseEntity<>(noteService.createNoteDtoFromNote(note), HttpStatus.OK);
    }

    @DeleteMapping("/delete-note/{id}")
    public ResponseEntity<HttpResponse> deleteNote(@PathVariable String id) {
        noteService.deleteNote(UUID.fromString(id));
        String message = String.format("Note with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

}
