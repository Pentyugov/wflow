package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Position;
import com.pentyugov.wflow.core.dto.PositionDto;
import com.pentyugov.wflow.web.exception.PositionExistException;
import com.pentyugov.wflow.web.exception.PositionNotFoundException;

import java.util.List;
import java.util.UUID;


public interface PositionService {

    String NAME = "wflow$PositionService";

    List<Position> getAll();

    Position getById(UUID id) throws PositionNotFoundException;

    Position add(PositionDto positionDto) throws PositionExistException;

    Position update(PositionDto positionDto) throws PositionExistException;

    void delete(UUID id);

    boolean isPositionExist(Position position);

    PositionDto convert(Position position);

}
