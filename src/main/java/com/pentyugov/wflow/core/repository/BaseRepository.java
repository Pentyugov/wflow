package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.deleteDate is null order by e.createDate")
    List<T> findAll();

    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id = ?1 and e.deleteDate is null")
    Optional<T> findById(UUID id);

    @Query("select e from #{#entityName} e where e.deleteDate is null")
    @Transactional(readOnly = true)
    List<T> findDeleted();

    @Transactional(readOnly = true)
    default boolean exists(UUID id) {
        return findById(id).isPresent();
    }

    @Query("update #{#entityName} e set e.deleteDate = current_timestamp where e.id = ?1")
    @Transactional
    @Modifying
    void delete(UUID id);

    @Override
    @Transactional
    default void delete(T entity) {
        delete(entity.getId());
    }

    @Transactional
    default void delete(Iterable<? extends BaseEntity> entities) {
        entities.forEach(entity -> delete(entity.getId()));
    }

    @Override
    @Query("update #{#entityName} e set e.deleteDate = current_timestamp")
    @Transactional
    @Modifying
    void deleteAll();
}
