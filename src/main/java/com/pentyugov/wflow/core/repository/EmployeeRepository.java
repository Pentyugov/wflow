package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends BaseRepository<Employee> {

    @Transactional(readOnly = true)
    @Query("select e from workflow$Employee e where e.email = ?1")
    Optional<Employee> findByEmail(String email);

    @Transactional(readOnly = true)
    @Query("select e from workflow$Employee e where e.department.id = ?1")
    List<Employee> findByEmployeesDepartment(UUID departmentId);

    @Transactional(readOnly = true)
    @Query("select e from workflow$Employee e where e.user.id = ?1")
    Optional<Employee> findByUserId(UUID userId);

    @Transactional(readOnly = true)
    @Query("select e from workflow$Employee e where e.department.id = ?1 and e.head = TRUE")
    Optional<Employee> findHeadOfDepartment(UUID departmentId);

    @Transactional(readOnly = true)
    @Query("select e from workflow$Employee e where e.personnelNumber = ?1")
    Optional<Employee> findByPersonnelNumber(Integer personnelNumber);
}
