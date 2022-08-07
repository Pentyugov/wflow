package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Contractor;
import com.pentyugov.wflow.core.dto.ContractorDto;
import com.pentyugov.wflow.core.repository.ContractorRepository;
import com.pentyugov.wflow.core.service.ContractorService;
import com.pentyugov.wflow.core.service.ValidationService;
import com.pentyugov.wflow.web.exception.ContractorNotFoundException;
import com.pentyugov.wflow.web.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service(ContractorService.NAME)
@RequiredArgsConstructor
public class ContractorServiceImpl extends AbstractService implements ContractorService {

    private final ModelMapper modelMapper;
    private final ContractorRepository contractorRepository;
    private final ValidationService validationService;

    @Override
    public List<Contractor> getAll() {
        return contractorRepository.findAll();
    }

    @Override
    public Contractor getById(UUID id) throws ContractorNotFoundException {
        return contractorRepository.findById(id).orElseThrow(
            () -> new ContractorNotFoundException(getMessage("exception.contractor.with.id.not.found", id)));
    }

    @Override
    public ContractorDto add(ContractorDto contractorDto) throws ValidationException {
        Contractor contractor = modelMapper.map(contractorDto, Contractor.class);
        validateContractor(contractor);
        contractorRepository.save(contractor);
        return convert(contractor);
    }

    @Override
    public ContractorDto update(ContractorDto contractorDto) throws ValidationException {
        Contractor contractor = convert(contractorDto);
        validateContractor(contractor);
        contractorRepository.save(contractor);
        return convert(contractor);
    }

    @Override
    public void delete(UUID id) {
        contractorRepository.delete(id);
    }

    @Override
    public ContractorDto convert(Contractor contractor) {
        return modelMapper.map(contractor, ContractorDto.class);
    }

    @Override
    public Contractor convert(ContractorDto contractorDto) {
        Contractor contractor = contractorRepository.findById(contractorDto.getId()).orElse(new Contractor());

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
        contractor.setIsOrganization(contractorDto.getIsOrganization());

        return contractor;
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

}
