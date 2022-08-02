package com.pentyugov.wflow.application.utils;

import com.pentyugov.wflow.core.domain.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class PersistenceManager {

    private final EntityManager entityManager;

    public <E extends BaseEntity> boolean isEntityNew(E entity) {
        if (entity.getId() == null)
            return true;
        return entityManager.find(entity.getClass(), entity.getId()) == null;
    }

    @SuppressWarnings("unchecked")
    public <E extends BaseEntity> E reload(E entity) {
        if (entity.getId() == null)
            return null;
        return (E) entityManager.find(entity.getClass(), entity.getId());
    }
}
