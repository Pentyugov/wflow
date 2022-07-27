package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Department;
import com.pentyugov.wflow.core.dto.ContractorDto;
import com.pentyugov.wflow.core.service.ContractorService;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.DepartmentNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contractors")
public class ContractorController extends AbstractController {


    private final ContractorService contractorService;

    @Autowired
    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<ContractorDto> contractorDtos = contractorService.getAllContractors()
                .stream()
                .map(contractorService::createContractorDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(contractorDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) throws ContractorNotFoundException {
        ContractorDto contractorDto = contractorService.createContractorDto(contractorService.getContractorById(UUID.fromString(id)));
        return new ResponseEntity<>(contractorDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ContractorDto contractorDto) throws ValidationException {
        ContractorDto contractor = contractorService.addNewContractor(contractorDto);
        return new ResponseEntity<>(contractor, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updater(@RequestBody ContractorDto contractorDto) throws ValidationException {
        ContractorDto contractor = contractorService.updateContractor(contractorDto);
        return new ResponseEntity<>(contractor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        contractorService.deleteContractor(UUID.fromString(id));
        String message = String.format("Contractor with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

}
