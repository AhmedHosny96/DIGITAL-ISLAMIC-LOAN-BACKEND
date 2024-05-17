package com.sahay.loan.controller;

import com.sahay.loan.dto.*;
import com.sahay.loan.entity.Product;
import com.sahay.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {


    private final LoanService loanService;

    @GetMapping("/products")
    public ResponseEntity<?> getProductSetup() {
        try {
            List<Product> loanProducts = loanService.getAllProducts();

            return new ResponseEntity<>(loanProducts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving product setup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // calculate loan amount
    @PostMapping(value = "/markup", produces = "application/json")
    public ResponseEntity<?> calculateLoanMarkup(@RequestBody @Valid LoanCalculationDto loanCalculationDto) {
        try {
            JSONObject calculatedLoan = loanService.calculateLoan(loanCalculationDto);
            return new ResponseEntity<>(calculatedLoan.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving product setup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // loan application confirmation

    @PostMapping(value = "/confirm", produces = "application/json")
    public ResponseEntity<?> confirmLoanApplication(@RequestBody LoanConfirmationDto confirmationRequest) {
        JSONObject response = loanService.confirmLoanApplication(confirmationRequest);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    // cancel and u hold the amount

    @PostMapping(value = "/cancel", produces = "application/json")
    public ResponseEntity<?> cancelApplication(@RequestBody CancelLoanDto cancelLoanDto) {
        JSONObject response = loanService.cancelApplication(cancelLoanDto);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/cancel/approval", produces = "application/json")
    public ResponseEntity<?> approveCancelApplication(@RequestBody ApproveCancelDto cancelLoanRequest) {
        JSONObject response = loanService.approveCancellation(cancelLoanRequest);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    // loan application
    @PostMapping(value = "/application", produces = "application/json")
    public ResponseEntity<?> applyLoan(@RequestBody LoanApplicationDto loanApplicationDto) {
        JSONObject response = loanService.createLoanApplication(loanApplicationDto);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    // collateral


    // guarantor


}
