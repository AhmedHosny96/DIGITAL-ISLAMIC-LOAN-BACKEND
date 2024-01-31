package com.sahay.loan;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/musharaka")
@RequiredArgsConstructor
public class LoanController {


    private final LoanService loanService;


    @GetMapping("/products")
    public ResponseEntity<?> getProductSetup() {
        try {
            List<Object> loanProducts = loanService.getLoanProducts();
            return new ResponseEntity<>(loanProducts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving product setup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // loan application confirmation

    @PostMapping(value = "/confirm", produces = "application/json")
    public ResponseEntity<?> confirmLoanApplication(@RequestBody LoanConfirmationRequest confirmationRequest) {
        JSONObject response = loanService.confirmLoanApplication(confirmationRequest);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
