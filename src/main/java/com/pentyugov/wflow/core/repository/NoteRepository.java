package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.Note;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends BaseRepository<Note> {

    @Transactional(readOnly = true)
    @Query("select n from workflow$Note n where n.user.id = ?1 order by n.createDate DESC")
    List<Note> findAllByUser(UUID user);
}
