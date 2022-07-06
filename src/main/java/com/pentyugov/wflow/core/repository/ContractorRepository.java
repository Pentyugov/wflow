package com.pentyugov.wflow.core.repository;


import com.pentyugov.wflow.core.domain.entity.Contractor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ContractorRepository extends BaseRepository<Contractor> {

    @Transactional(readOnly = true)
    @Query("select c from workflow$Contractor c where c.name = ?1")
    Optional<Contractor> findByName(String name);

}