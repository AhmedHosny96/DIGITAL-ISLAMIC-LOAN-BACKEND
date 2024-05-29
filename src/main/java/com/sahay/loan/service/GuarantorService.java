package com.sahay.loan.service;

import com.sahay.customer.CustomerService;
import com.sahay.dto.CustomResponse;
import com.sahay.loan.dto.ApproveGuarantorDto;
import com.sahay.loan.dto.GuarantorDto;
import com.sahay.loan.entity.Guarantor;
import com.sahay.loan.entity.Request;
import com.sahay.loan.repo.GuarantorRepo;
import com.sahay.loan.repo.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuarantorService {

    private final GuarantorRepo guarantorRepo;

    private final UtilityService utilityService;

    private final ModelMapper modelMapper;

    private final OtpRepository otpRepository;

    public JSONObject getPendingGuarantorList(int status) {
        Optional<List<Guarantor>> guarantorByStatus = guarantorRepo.findGuarantorByStatus(status);

        var customResponse = new JSONObject();
        if (guarantorByStatus.get().isEmpty()) {
            customResponse.put("response", "004");
            customResponse.put("responseDescription", "No data found");
            return customResponse;
        }

        customResponse.put("response", "000");
        customResponse.put("responseDescription", "success");
        customResponse.put("guarantors", guarantorByStatus.get());

        return customResponse;
    }

//    private final CustomerService customerService;

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

        if (!guarantorByGuarantorAccount.isPresent()) {
            return CustomResponse.builder().response("004").responseDescription("Guarantor not found").build();
        }
        Guarantor guarantor = guarantorByGuarantorAccount.get();

        if (status == 1) {
            guarantor.setHasLoanAttached(true);
            guarantorRepo.save(guarantor);
            // send sms to the customer

            // notify the customer acc

            utilityService.sendConfirmationMessage(guarantor.getCustomerAccount(),
                    "Dear customer guarantor has successfully accepted to guarantee you "
            );

            return CustomResponse.builder().response("000").responseDescription("Guarantor accepted the deal").build();
        } else {

            utilityService.sendConfirmationMessage(guarantor.getCustomerAccount(),
                    "Dear customer guarantor has rejected to guarantee you "
            );

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

            Optional<Request> byStatusAndAccountNumber = otpRepository.findByStatusAndAccountNumber(0, guarantor.getCustomerAccount());

            Request request = byStatusAndAccountNumber.get();

            // notify guarantor here

            String loanMessagePattern = "Dear customer, account {0} has selected you as a guarantor for an amount of {1}. To accept, please dial *873#6*3*1.";

            String message = MessageFormat.format(loanMessagePattern,
                    request.getAccountNumber(),
                    request.getPrincipalAmount());

            utilityService.sendConfirmationMessage(guarantor.getGuarantorAccount(), message);

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
