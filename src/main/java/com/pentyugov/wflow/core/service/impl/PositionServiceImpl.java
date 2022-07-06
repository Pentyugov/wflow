package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Position;
import com.pentyugov.wflow.core.dto.PositionDto;
import com.pentyugov.wflow.core.repository.PositionRepository;
import com.pentyugov.wflow.core.service.PositionService;
import com.pentyugov.wflow.web.exception.PositionExistException;
import com.pentyugov.wflow.web.exception.PositionNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service(PositionService.NAME)
public class PositionServiceImpl extends AbstractService implements PositionService {

    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, ModelMapper modelMapper) {
        this.positionRepository = positionRepository;
        this.modelMapper = modelMapper;
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Position getByName(String name) throws PositionNotFoundException {
        return positionRepository
                .findByName(name)
                .orElseThrow(() -> new PositionNotFoundException(getMessage("exception.position.not.found", "name", name)));
    }

    public Position createNewPosition(PositionDto positionDto) throws PositionExistException {
        Position position = new Position();
        position.setName(positionDto.getName());
        position.setCode(positionDto.getCode());
        if (!isPositionExist(position)) {
            return positionRepository.save(position);
        }
        throw new PositionExistException(getMessage("exception.position.exist", null));
    }

    public Position updatePosition(PositionDto positionDto) throws PositionExistException {
        Position position = positionRepository.getById(positionDto.getId());
        position.setName(positionDto.getName());
        position.setCode(positionDto.getCode());
        if (!isPositionExist(position)) {
            return positionRepository.save(position);
        }
        throw new PositionExistException(getMessage("exception.position.exist", null));
    }

    public boolean isPositionExist(Position position) {
        if (persistenceManager.isEntityNew(position)) {
            return positionRepository.findByCodeOrName(position.getCode(), position.getName()).isPresent();
        }
        return positionRepository.findByCodeOrName(position.getId(), position.getCode(), position.getName()).isPresent();
    }

    public Position getById(UUID id) throws PositionNotFoundException {
        return positionRepository.findById(id)
                .orElseThrow(() -> new PositionNotFoundException(getMessage("exception.position.not.found", "id", id.toString())));
    }

    public PositionDto createPositionDtoFromPosition(Position position) {
        return modelMapper.map(position, PositionDto.class);
    }

    public Position createPositionFromPositionDto(PositionDto positionDto) {
        return modelMapper.map(positionDto, Position.class);
    }

    public void deletePosition(UUID id) {
        positionRepository.delete(id);
    }
}
