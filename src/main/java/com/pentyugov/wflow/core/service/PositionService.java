package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Position;
import com.pentyugov.wflow.core.dto.PositionDto;
import com.pentyugov.wflow.web.exception.PositionExistException;
import com.pentyugov.wflow.web.exception.PositionNotFoundException;

import java.util.List;
import java.util.UUID;


public interface PositionService {

    String NAME = "wflow$PositionService";

    List<Position> getAllPositions();

    Position getByName(String name) throws PositionNotFoundException;

    Position createNewPosition(PositionDto positionDto) throws PositionExistException;

    Position updatePosition(PositionDto positionDto) throws PositionExistException;

    boolean isPositionExist(Position position);

    Position getById(UUID id) throws PositionNotFoundException;

    PositionDto createPositionDtoFromPosition(Position position);

    Position createPositionFromPositionDto(PositionDto positionDto);

    void deletePosition(UUID id);


}
