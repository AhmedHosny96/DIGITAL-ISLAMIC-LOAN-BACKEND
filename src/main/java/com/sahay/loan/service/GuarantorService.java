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

    public List<Guarantor> getAllGuarantors() {
        List<Guarantor> guarantors = guarantorRepo.findAll();
        return guarantors;
    }

    public Optional<Guarantor> getGuarantorByCustomerAccount(String customerAccount) {
        return guarantorRepo.findByCustomerAccount(customerAccount);

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
                .hasLoanAttached(false)

                .build();

        guarantorRepo.save(guarantor);

        return CustomResponse.builder()
                .response("000")
                .responseDescription("Guarantor created successfully")
                .build();
    }

    // confirmation by guarantor
    public CustomResponse confirmationByGuarantorViaUSSD(String guarantorAccount, int status) {

        Optional<Guarantor> guarantorByGuarantorAccount = guarantorRepo.findGuarantorByGuarantorAccount(guarantorAccount);
        if (!guarantorByGuarantorAccount.isPresent())
            return CustomResponse.builder().response("004").responseDescription("Guarantor not found").build();

        Guarantor guarantor = guarantorByGuarantorAccount.get();

        if (status == 1) {
            guarantor.setHasLoanAttached(true);
            // send sms to the customer

            return CustomResponse.builder().response("000").responseDescription("Guarantor accepted the deal").build();

        }

        // send sms
        return CustomResponse.builder().response("004").responseDescription("Guarantor has rejected the deal").build();


    }

    public CustomResponse approveGuarantor(ApproveGuarantorDto approveGuarantorDto) {
        Optional<Guarantor> optionalGuarantor = guarantorRepo.findById(approveGuarantorDto.getGuarantorId());
        if (optionalGuarantor.isPresent()) {
            Guarantor guarantor = optionalGuarantor.get();
            guarantor.setStatus(approveGuarantorDto.getStatus()); // Set status to "1" for approval , "2"
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
