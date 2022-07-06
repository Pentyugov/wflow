package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.SysMail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMailRepository extends BaseRepository<SysMail> {

    @Query("select sm from sys$Mail sm where sm.isSend <> true or sm.isSend is null")
    List<SysMail> getNotSent();
}
