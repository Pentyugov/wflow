package com.pentyugov.wflow.core.repository;


import com.pentyugov.wflow.core.domain.entity.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PermissionRepository extends BaseRepository<Permission> {

    @Transactional(readOnly = true)
    @Query("select p from sec$Permission p where p.name = ?1 and p.deleteDate is null")
    Optional<Permission> findByName(String name);
}
