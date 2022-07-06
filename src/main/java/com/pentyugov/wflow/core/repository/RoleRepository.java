package com.pentyugov.wflow.core.repository;


import com.pentyugov.wflow.core.domain.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    @Transactional(readOnly = true)
    @Query("select r from sec$Role r where r.name = ?1 and r.deleteDate is null")
    Optional<Role> findByName(String name);

    @Transactional(readOnly = true)
    @Query("select r from sec$Role r where r.name in ?1 and r.deleteDate is null")
    List<Role> findRolesByNames(List<String> names);


}
