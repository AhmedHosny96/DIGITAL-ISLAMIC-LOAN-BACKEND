package com.sahay.loan.service;


import com.sahay.customer.model.Customer;
import com.sahay.customer.model.CustomerDocument;
import com.sahay.customer.repo.CustomerRepository;
import com.sahay.dto.CustomResponse;
import com.sahay.exception.CustomException;
import com.sahay.loan.dto.ApproveCollateralDto;
import com.sahay.loan.dto.CollateralDocumentDto;
import com.sahay.loan.dto.CreateCollateralDto;
import com.sahay.loan.entity.Collateral;
import com.sahay.loan.entity.CollateralImages;
import com.sahay.loan.repo.CollateralImagesRepo;
import com.sahay.loan.repo.CollateralRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollateralService {
    private final ModelMapper modelMapper;

    private final CollateralRepository collateralRepository;

    private final CollateralImagesRepo collateralImagesRepo;
    private final CustomerRepository customerRepository;

    // get all
    public List<Collateral> getAllCollaterals() {
        return collateralRepository.findAll();
    }

    // create
    public CustomResponse createCollateral(CreateCollateralDto collateralDto) {
        Optional<Collateral> byCollateralNumber = collateralRepository.findByCollateralNumber(collateralDto.getCollateralNumber());

        if (byCollateralNumber.isPresent()) {

            return CustomResponse.builder()
                    .response("004")
                    .responseDescription("Collateral already exists")
                    .build();
        }

        // todo : check customerId

//        var collateral = new Collateral();
//        collateral.setCollateralType(collateralDto.getCollateralType());
//        collateral.setCollateralNumber(collateralDto.getCollateralNumber());
//        collateral.setCustomerId(collateralDto.getCustomerId());
//        collateral.setAppraisalDate(collateralDto.getAppraisalDate());
//        collateral.setAppraisalAuthority(collateralDto.getAppraisalAuthority());
//        collateral.setStatus(0);
//        collateral.setCollateralDescription(collateralDto.getCollateralDescription());
//        collateral.setCreatedDate(LocalDateTime.now());
//        collateral.setProofOfOwnership(collateralDto.getProofOfOwnership());
//        collateral.setInsured(collateral.isInsured());
//        collateral.setCreatedBy(collateralDto.getCreatedBy());
//        collateral.setInsurancePolicy(collateralDto.getInsurancePolicy());
//        collateral.setInsuranceStatus(collateralDto.getInsuranceStatus());
//        collateral.setRemark(collateralDto.getRemark());

        var collateral = modelMapper.map(collateralDto, Collateral.class);

        collateralRepository.save(collateral);

        return CustomResponse.builder()
                .response("000")
                .responseDescription("Collateral created successfully")
                .build();

    }

    // create collateral documents
    @Transactional
    public CustomResponse uploadCollateralDocs(CollateralDocumentDto documentDto) {
        try {
            // Check if collateral exists
            Optional<Collateral> optionalCollateral = collateralRepository.findById(documentDto.getCollateralId());
            if (!optionalCollateral.isPresent()) {
                return CustomResponse.builder()
                        .response("004")
                        .responseDescription("Collateral not found")
                        .build();
            }
            // Save file to the file system
            String uploadDir = "E:\\Apps\\loan-docs"; // Specify your upload directory
            String fileName = System.currentTimeMillis() + "_" + documentDto.getDocument().getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(documentDto.getDocument().getInputStream(), filePath);

            // Save file path in the database
            CollateralImages collateralImages = CollateralImages.builder()
                    .collateralId(documentDto.getCollateralId())
                    .document("/" + uploadDir + "/" + fileName) // Adjust the path to start with a slash
                    .build();
            collateralImagesRepo.save(collateralImages);

            return CustomResponse.builder()
                    .response("000")
                    .responseDescription("Collateral documents uploaded successfully")
                    .build();
        } catch (IOException e) {
            return CustomResponse.builder()
                    .response("999")
                    .responseDescription("Failed to upload collateral documents : " + e.getMessage())
                    .build();
        }
    }

    public List<CollateralImages> getCollateralDocumentsByCollateralId(int collateralId) {
        return collateralImagesRepo.findByCollateralId(collateralId);
    }


    // approve or reject , status , 1

    public CustomResponse approveCollateral(Integer collateralId, ApproveCollateralDto approvalDto) {
        Optional<Collateral> optionalCollateral = collateralRepository.findById(collateralId);

        if (!optionalCollateral.isPresent()) {
            return CustomResponse.builder()
                    .response("004")
                    .responseDescription("Collateral not found ")
                    .build();
        }

        Collateral collateral = optionalCollateral.get();
        collateral.setStatus(approvalDto.getStatus());
        collateral.setVerifiedBy(approvalDto.getApprovedBy());
        collateral.setVerifierComment(approvalDto.getVerifiedComment());
        collateralRepository.save(collateral);

        return CustomResponse.builder()
                .response("000")
                .responseDescription("Collateral status updated successfully")
                .build();
    }

    // get collateral phone number

    public Collateral getCollateralByNumber(String collateralNumber) throws CustomException {

        if (!collateralRepository.findByCollateralNumber(collateralNumber).isPresent()) {
            throw new CustomException("Collateral not found");
        }

        return collateralRepository.findByCollateralNumber(collateralNumber).get();
    }

    public Collateral getCollateralByCustomerId(int customerId) throws CustomException {

        if (!collateralRepository.findByCustomerId(customerId).isPresent()) {
            throw new CustomException("Collateral not found");
        }
        return collateralRepository.findByCustomerId(customerId).get();
    }

    public Collateral getCollateralByPhone(String phoneNumber) throws CustomException {
        // Find the customer by phone number

        log.info("PHONE : {}", phoneNumber);
        Optional<Customer> byCustomerAccount = customerRepository.findByCustomerAccount(phoneNumber);

        if (!byCustomerAccount.isPresent()) {
            throw new CustomException("Customer account does not exist");
        }
        // Retrieve the customer ID
        int customerId = byCustomerAccount.get().getId();

        // Find collateral by customer ID
        Optional<Collateral> byCustomerId = collateralRepository.findByCustomerId(customerId);

        if (!byCustomerId.isPresent()) {
            throw new CustomException("Collateral not found for customer");
        }

        return byCustomerId.get();
    }

}
