package com.pentyugov.wflow.application.utils;

import com.pentyugov.wflow.core.domain.entity.BaseEntity;
import com.pentyugov.wflow.core.domain.entity.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class PersistenceManager {


    private final EntityManager entityManager;

    @Autowired
    public PersistenceManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <E extends BaseEntity> boolean isEntityNew(E entity) {
        if (entity.getId() == null)
            return true;
        return entityManager.find(Position.class, entity.getId()) == null;
    }

    @SuppressWarnings("unchecked")
    public <E extends BaseEntity> E reload(E entity) {
        if (entity.getId() == null)
            return null;
        return (E) entityManager.find(entity.getClass(), entity.getId());
    }
}
