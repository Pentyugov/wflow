package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Position;
import com.pentyugov.wflow.core.dto.PositionDto;
import com.pentyugov.wflow.core.service.PositionService;
import com.pentyugov.wflow.web.exception.PositionExistException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/positions")
public class PositionController extends AbstractController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<PositionDto> positions = new ArrayList<>();
        positionService.getAllPositions().forEach(position ->
                positions.add(positionService.createPositionDtoFromPosition(position)));
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PositionDto> add(@RequestBody PositionDto positionDto) throws PositionExistException {
        Position position = positionService.createNewPosition(positionDto);
        return new ResponseEntity<>(positionService.createPositionDtoFromPosition(position), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PositionDto> update(@RequestBody PositionDto positionDto) throws PositionExistException {
        Position position = positionService.updatePosition(positionDto);
        return new ResponseEntity<>(positionService.createPositionDtoFromPosition(position), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        positionService.deletePosition(UUID.fromString(id));
        String message = String.format("Position with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

}
