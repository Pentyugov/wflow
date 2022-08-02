package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.Position;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface PositionRepository extends BaseRepository<Position> {

    @Transactional(readOnly = true)
    @Query("select p from workflow$Position p where p.name = ?1")
    Optional<Position> findByName(String name);

    @Transactional(readOnly = true)
    @Query("select p from workflow$Position p where p.id <> ?1 and (p.code = ?2 or p.name = ?3)")
    Optional<Position> findByCodeOrName(UUID id, String code, String name);

    @Transactional(readOnly = true)
    @Query("select p from workflow$Position p where p.code = ?1 or p.name = ?2")
    Optional<Position> findByCodeOrName(String code, String name);
}
