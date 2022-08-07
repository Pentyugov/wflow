package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Contractor;
import com.pentyugov.wflow.core.dto.ContractorDto;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface ContractorService {

    String NAME = "wflow$ContractorService";

    List<Contractor> getAll();

    Contractor getById(UUID id) throws ContractorNotFoundException;

    ContractorDto add(ContractorDto contractorDto) throws ValidationException;

    ContractorDto update(ContractorDto contractorDto) throws ValidationException;

    void delete(UUID id);

    ContractorDto convert(Contractor contractor);

    Contractor convert(ContractorDto contractorDto);

}
