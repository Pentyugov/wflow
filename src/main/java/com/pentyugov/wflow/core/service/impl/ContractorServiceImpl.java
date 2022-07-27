package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Contractor;
import com.pentyugov.wflow.core.dto.ContractorDto;
import com.pentyugov.wflow.core.repository.ContractorRepository;
import com.pentyugov.wflow.core.service.ContractorService;
import com.pentyugov.wflow.core.service.ValidationService;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service(ContractorService.NAME)
public class ContractorServiceImpl extends AbstractService implements ContractorService {

    private final ModelMapper modelMapper;
    private final ContractorRepository contractorRepository;
    private final ValidationService validationService;

    @Autowired
    public ContractorServiceImpl(ModelMapper modelMapper, ContractorRepository contractorRepository, ValidationService validationService) {
        this.modelMapper = modelMapper;
        this.contractorRepository = contractorRepository;
        this.validationService = validationService;
    }

    @Override
    public List<Contractor> getAllContractors() {
        return contractorRepository.findAll();
    }

    @Override
    public ContractorDto createContractorDto(Contractor contractor) {
        return modelMapper.map(contractor, ContractorDto.class);
    }

    @Override
    public ContractorDto addNewContractor(ContractorDto contractorDto) throws ValidationException {
        Contractor contractor = modelMapper.map(contractorDto, Contractor.class);
        validateContractor(contractor);
        contractorRepository.save(contractor);
        return createContractorDto(contractor);
    }

    @Override
    public ContractorDto updateContractor(ContractorDto contractorDto) throws ValidationException {
        Contractor contractor = createContractorFromDto(contractorDto);
        validateContractor(contractor);
        contractorRepository.save(contractor);
        return createContractorDto(contractor);
    }

    @Override
    public Contractor getContractorById(UUID id) throws ContractorNotFoundException {
        return contractorRepository.findById(id).orElseThrow(() -> new ContractorNotFoundException(getMessage("exception.contractor.with.id.not.found", id)));
    }

    @Override
    public void deleteContractor(UUID id) {
        contractorRepository.delete(id);
    }

    private void validateContractor(Contractor contractor) throws ValidationException {
        if (StringUtils.hasText(contractor.getPhone())) {
            String parsed = validationService.parsePhoneNumber(contractor.getPhone());
            if (ObjectUtils.isEmpty(parsed)) {
                throw new ValidationException("Invalid phone number");
            } else {
                contractor.setPhone("+" + parsed);
            }
        }
    }

    private Contractor createContractorFromDto(ContractorDto contractorDto) {
        Contractor contractor;
        if (contractorDto.getId() != null) {
            contractor = contractorRepository.getById(contractorDto.getId());
        } else {
            contractor = new Contractor();
        }

        contractor.setName(contractorDto.getName());
        contractor.setFullName(contractorDto.getFullName());
        contractor.setInn(contractorDto.getInn());
        contractor.setKpp(contractorDto.getKpp());
        contractor.setOkpo(contractorDto.getOkpo());
        contractor.setPostalAddress(contractorDto.getPostalAddress());
        contractor.setLegalAddress(contractorDto.getLegalAddress());
        contractor.setPhone(contractorDto.getPhone());
        contractor.setFax(contractorDto.getFax());
        contractor.setEmail(contractorDto.getEmail());
        contractor.setComment(contractorDto.getEmail());
        contractor.setWebsite(contractorDto.getWebsite());
        contractor.setNonResident(contractorDto.getNonResident());
        contractor.setSupplier(contractorDto.getSupplier());
        contractor.setCustomer(contractorDto.getCustomer());
        contractor.setOrganization(contractorDto.getIsOrganization());

        return contractor;
    }


}
