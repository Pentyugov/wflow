package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends BaseRepository<Department> {

    @Transactional(readOnly = true)
    @Query("select d from workflow$Department d where d.name = ?1")
    Optional<Department> findByName(String name);

    @Transactional(readOnly = true)
    @Query("select d from workflow$Department d where d.code = ?1")
    Optional<Department> findByCode(String code);

    @Transactional(readOnly = true)
    @Query("select d from workflow$Department d where d.head = TRUE")
    Optional<Department> findHeadDepartment();

    @Transactional(readOnly = true)
    @Query("select d from workflow$Department d where d.parentDepartment.id = ?1")
    List<Department> getDepartmentByParentDepartment(UUID parentDepartmentId);

    @Transactional(readOnly = true)
    @Query("select d from workflow$Department d where d.id not in ?1")
    List<Department> getPossibleParentDepartments(List<UUID> ids);
}
