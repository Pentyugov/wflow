package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Contractor;
import com.pentyugov.wflow.core.dto.ContractorDto;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface ContractorService {

    String NAME = "wflow$ContractorService";

    List<Contractor> getAllContractors();

    ContractorDto createContractorDto(Contractor contractor);

    ContractorDto addNewContractor(ContractorDto contractorDto) throws ValidationException;

    ContractorDto updateContractor(ContractorDto contractorDto) throws ValidationException;

    Contractor getUserById(UUID id) throws ContractorNotFoundException;

    void deleteContractor(UUID id);
}
