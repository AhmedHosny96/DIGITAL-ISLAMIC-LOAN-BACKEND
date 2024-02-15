package com.sahay.loan.service;

import com.sahay.dto.CustomResponse;
import com.sahay.loan.dto.ApproveGuarantorDto;
import com.sahay.loan.dto.GuarantorDto;
import com.sahay.loan.entity.Guarantor;
import com.sahay.loan.repo.GuarantorRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuarantorService {

    private final GuarantorRepo guarantorRepo;

    private final ModelMapper modelMapper;

    public List<GuarantorDto> getAllGuarantors() {
        List<Guarantor> guarantors = guarantorRepo.findAll();
        return guarantors.stream()
                .map(guarantor -> modelMapper.map(guarantor, GuarantorDto.class))
                .collect(Collectors.toList());
    }

    public Optional<GuarantorDto> getGuarantorByCustomerAccount(String customerAccount) {
        Optional<Guarantor> optionalGuarantor = guarantorRepo.findByCustomerAccount(customerAccount);
        return optionalGuarantor.map(guarantor -> modelMapper.map(guarantor, GuarantorDto.class));
    }


    public CustomResponse createGuarantor(GuarantorDto guarantorDto) {
        Optional<Guarantor> byCustomerAccount = guarantorRepo.findByCustomerAccount(guarantorDto.getCustomerAccount());

        if (byCustomerAccount.isPresent()) {
            return CustomResponse.builder()
                    .response("004")
                    .responseDescription("Guarantor account number exists")
                    .build();
        }

        Guarantor guarantor = Guarantor.builder()
                .customerAccount(guarantorDto.getCustomerAccount())
                .guarantorAccount(guarantorDto.getGuarantorAccount())
                .firstName(guarantorDto.getFirstName())
                .middleName(guarantorDto.getMiddleName())
                .lastName(guarantorDto.getLastName())
                .address(guarantorDto.getAddress())
                .city(guarantorDto.getCity())
                .relationship(guarantorDto.getRelationship())
                .occupation(guarantorDto.getOccupation())
                .isEmployed(guarantorDto.isEmployed())
                .employmentDetails(guarantorDto.getEmploymentDetails())
                .salary(guarantorDto.getSalary())
                .creditHistory(guarantorDto.getCreditHistory())
                .status(0)
                .remark(guarantorDto.getRemark())
                .createdBy(guarantorDto.getCreatedBy())
                .createdDate(guarantorDto.getCreatedDate())
//                .verifiedBy(guarantorDto.getVerifiedBy())
//                .verifiedDate(guarantorDto.getVerifiedDate())
                .build();

        guarantorRepo.save(guarantor);

        return CustomResponse.builder()
                .response("000")
                .responseDescription("Guarantor created successfully")
                .build();
    }

    public CustomResponse approveGuarantor(ApproveGuarantorDto approveGuarantorDto) {
        Optional<Guarantor> optionalGuarantor = guarantorRepo.findById(approveGuarantorDto.getGuarantorId());
        if (optionalGuarantor.isPresent()) {
            Guarantor guarantor = optionalGuarantor.get();
            guarantor.setStatus(approveGuarantorDto.getStatus()); // Set status to "1" for approval
            guarantor.setVerifiedBy(approveGuarantorDto.getVerifiedBy());
            guarantor.setVerifiedDate(LocalDateTime.now());
            guarantorRepo.save(guarantor);
            return CustomResponse.builder()
                    .response("000")
                    .responseDescription("Guarantor approved successfully")
                    .build();
        } else {
            return CustomResponse.builder()
                    .response("404")
                    .responseDescription("Guarantor not found")
                    .build();
        }
    }

    public CustomResponse updateGuarantor(Long id, GuarantorDto guarantorDto) {
        Optional<Guarantor> optionalGuarantor = guarantorRepo.findById(id);
        if (optionalGuarantor.isPresent()) {
            Guarantor existingGuarantor = optionalGuarantor.get();
            // Update existingGuarantor with the data from guarantorDto
            modelMapper.map(guarantorDto, existingGuarantor);
            guarantorRepo.save(existingGuarantor);
            return CustomResponse.builder()
                    .response("000")
                    .responseDescription("Guarantor updated successfully")
                    .build();
        } else {
            return CustomResponse.builder()
                    .response("404")
                    .responseDescription("Guarantor not found")
                    .build();
        }

    }
}
