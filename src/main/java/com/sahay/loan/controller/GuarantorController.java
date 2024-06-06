package com.sahay.loan.controller;


import com.sahay.dto.CustomResponse;
import com.sahay.exception.CustomException;
import com.sahay.loan.dto.ApproveGuarantorDto;
import com.sahay.loan.dto.GuarantorDto;
import com.sahay.loan.entity.Guarantor;
import com.sahay.loan.service.GuarantorService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/guarantor")
@RequiredArgsConstructor
public class GuarantorController {

    private final GuarantorService guarantorService;

    @GetMapping(value = "/pending", produces = "application/json")
    public ResponseEntity<?> getPendingGuarantor(@RequestParam int status) {
        JSONObject pendingGuarantorList = guarantorService.getPendingGuarantorList(status);
        return ResponseEntity.ok(pendingGuarantorList.toString());
    }

    @GetMapping(value = "/loan-info", produces = "application/json")
    public ResponseEntity<?> getLoanInfo(@RequestParam String guarantorAccount) throws CustomException {
        CustomResponse loanDetailsByGuarantor = guarantorService.getLoanDetailsByGuarantor(guarantorAccount);
        return ResponseEntity.ok(loanDetailsByGuarantor);
    }

    @GetMapping
    public ResponseEntity<List<?>> getAllGuarantors() {
        List<Guarantor> guarantors = guarantorService.getAllGuarantors();
        return ResponseEntity.ok(guarantors);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> guarantorConfirmation(@RequestParam String guarantorAccount, @RequestParam Integer status) {
        CustomResponse customResponse = guarantorService.confirmationByGuarantorViaUSSD(guarantorAccount, status);
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/{customerAccount}")
    public ResponseEntity<Guarantor> getGuarantorByCustomerAccount(@PathVariable String customerAccount) {
        Optional<Guarantor> optionalGuarantor = guarantorService.getGuarantorByCustomerAccount(customerAccount);
        return optionalGuarantor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createGuarantor(@RequestBody GuarantorDto guarantorDto) {
        CustomResponse response = guarantorService.createGuarantor(guarantorDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<CustomResponse> approveGuarantor(@RequestBody ApproveGuarantorDto approveGuarantorDto) throws CustomException {
        CustomResponse response = guarantorService.approveGuarantor(approveGuarantorDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{customerAccount}")
    public ResponseEntity<CustomResponse> updateGuarantor(@PathVariable Long id, @RequestBody GuarantorDto guarantorDto) {
        CustomResponse response = guarantorService.updateGuarantor(id, guarantorDto);
        return ResponseEntity.ok(response);
    }

}
