package com.sahay.loan.controller;


import com.sahay.customer.model.Customer;
import com.sahay.customer.model.CustomerDocument;
import com.sahay.dto.CustomResponse;
import com.sahay.exception.CustomException;
import com.sahay.loan.dto.ApproveCollateralDto;
import com.sahay.loan.dto.CollateralDocumentDto;
import com.sahay.loan.dto.CreateCollateralDto;
import com.sahay.loan.dto.LoanConfirmationDto;
import com.sahay.loan.entity.Collateral;
import com.sahay.loan.entity.CollateralImages;
import com.sahay.loan.service.CollateralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collateral")
@RequiredArgsConstructor
@Slf4j
public class CollateralController {

    private final CollateralService collateralService;


    @GetMapping("/all")
    public ResponseEntity<?> getAllCollaterals() {
        try {
            List<Collateral> allCollaterals = collateralService.getAllCollaterals();
            return new ResponseEntity<>(allCollaterals, HttpStatus.OK);
        } catch (Exception e) {

            log.info("ERROR OCCURED : {}", e.getMessage());
            return new ResponseEntity<>("Error retrieving product setup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/customer")
//    public ResponseEntity<?> getCollateralByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
//        try {
//            Collateral collateral = collateralService.getCollateralByPhone(phoneNumber);
//            return new ResponseEntity<>(collateral, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // create collateral alone
    @PostMapping()
    public ResponseEntity<?> createCollateral(@RequestBody CreateCollateralDto collateralDto) {
        CustomResponse collateral = collateralService.createCollateral(collateralDto);
        return new ResponseEntity<>(collateral, HttpStatus.OK);
    }

    // approve or reject
    @PutMapping("/{collateralId}")
    public ResponseEntity<?> approveCollateral(@PathVariable Integer collateralId, @RequestBody ApproveCollateralDto approveCollateralDto) {
        try {
            CustomResponse collateral = collateralService.approveCollateral(collateralId, approveCollateralDto);
            return new ResponseEntity<>(collateral, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error approving collateral: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/documents", produces = "application/json")
    public ResponseEntity<?> getAllBranches(@RequestParam("collateralId") int collateralId) {
        List<CollateralImages> customerDocumentById = collateralService.getCollateralDocumentsByCollateralId(collateralId);
        return new ResponseEntity<>(customerDocumentById, HttpStatus.OK);
    }

    // collateral documents
    @PostMapping("/upload")
    public ResponseEntity<CustomResponse> uploadCollateralDocuments(@RequestParam("document") MultipartFile document, @RequestParam("collateralId") Integer collateralId, @RequestParam("description") String description) {
        // Construct CollateralDocumentDto using the file and collateralId
        CollateralDocumentDto documentDto = new CollateralDocumentDto();
        documentDto.setDocument(document);
        documentDto.setCollateralId(collateralId);
        documentDto.setDescription(description);

        CustomResponse response = collateralService.uploadCollateralDocs(documentDto);
        HttpStatus httpStatus = HttpStatus.OK;
        if (!response.getResponse().equals("000")) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Set appropriate HTTP status code for error response
        }
        return ResponseEntity.status(httpStatus).body(response);
    }

}
