package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.ScreenPermissions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ScreenPermissionRepository extends BaseRepository<ScreenPermissions> {

    @Transactional(readOnly = true)
    @Query("select sp from security$ScreenPermissions sp where sp.role.id in :ids")
    List<ScreenPermissions> findAllByRoles(@Param("ids") List<UUID> ids);

    @Transactional(readOnly = true)
    @Query("select sp from security$ScreenPermissions sp where sp.role.id in :ids and sp.screen = :screenId")
    List<ScreenPermissions> findScreenByRoles(@Param("ids") List<UUID> ids, @Param("screenId") String screenId);
}
