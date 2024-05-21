package com.sahay.customer;


import com.sahay.customer.dto.ApproveOnBoardDto;
import com.sahay.customer.dto.CreateCustomerDocument;
import com.sahay.customer.dto.OnBoardDto;
import com.sahay.customer.model.CustomerBranch;
import com.sahay.customer.model.CustomerDocument;
import com.sahay.dto.CustomResponse;
import com.sahay.exception.CustomException;
import com.sahay.loan.dto.CollateralDocumentDto;
import com.sahay.loan.entity.Collateral;
import com.sahay.loan.service.CollateralService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final CollateralService collateralService;

    @GetMapping(value = "/pending", produces = "application/json")
    public ResponseEntity<?> getPendingCustomerdocs(@RequestParam("status") int status) {
        List<CustomerDocument> pendingDocuments = customerService.getPendingDocuments(status);
        return new ResponseEntity<>(pendingDocuments, HttpStatus.OK);
    }


    @PostMapping("/upload")
    public ResponseEntity<CustomResponse> uploadCustomerDetails(@RequestParam("file") MultipartFile document,
                                                                @RequestParam("documentType") String documentType,
                                                                @RequestParam("description") String description,
                                                                @RequestParam("customerId") Integer customerId,
                                                                @RequestParam("documentNumber") String documentNumber,
                                                                @RequestParam("createdBy") Integer createdBy
    ) {


        // Construct CollateralDocumentDto using the file and collateralId
        CreateCustomerDocument documentDto = new CreateCustomerDocument();
        documentDto.setFile(document);
        documentDto.setDocumentType(documentType);
        documentDto.setDocumentDescription(description);
        documentDto.setCustomerId(customerId);
        documentDto.setDocumentNumber(documentNumber);
        documentDto.setCreatedBy(createdBy);

        CustomResponse response = customerService.uploadCustomerDocuments(documentDto);
        HttpStatus httpStatus = HttpStatus.OK;
        if (!response.getResponse().equals("000")) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Set appropriate HTTP status code for error response
        }
        return ResponseEntity.status(httpStatus).body(response);
    }

    // GET EXISTING CUSTOMER

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getCustomer(@RequestParam String accountNumber) {
        String customer = customerService.getCustomer(accountNumber);

        var response = new JSONObject();

        if (customer == null) {
            response.put("response", "004");
            response.put("responseDescription", "customer not found");

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }

        var customerInfo = new JSONObject(customer);

        response.put("response", "000");
        response.put("responseDescription", "success");
        response.put("customer", customerInfo.getJSONArray("Customer").getJSONObject(0));

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);

    }

    @GetMapping(value = "/documents", produces = "application/json")
    public ResponseEntity<?> getAllBranches(@RequestParam("customerId") int customerId) {
        List<CustomerDocument> customerDocumentById = customerService.getCustomerDocumentById(customerId);
        return new ResponseEntity<>(customerDocumentById, HttpStatus.OK);
    }

    // GET BRANCHES

    @GetMapping(value = "/branches", produces = "application/json")
    public ResponseEntity<?> getAllBranches() {
        List<CustomerBranch> allBranches = customerService.getAllBranches();
        return new ResponseEntity<>(allBranches, HttpStatus.OK);
    }

    @GetMapping(value = "/collateral", produces = "application/json")
    public ResponseEntity<?> getCustomerCollateral(@RequestParam("phoneNumber") String phoneNumber) throws CustomException {

        var response = new JSONObject();

        try {
            Collateral collateralByPhone = collateralService.getCollateralByPhone(phoneNumber);

            JSONObject collateral = new JSONObject(collateralByPhone);

            response.put("response", "000");
            response.put("responseDescription", "success");
            response.put("collateral", collateral);

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (CustomException e) {

            response.put("response", "004");
            response.put("responseDescription", e.getMessage());
            // Handle the custom exception
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }
    }

    //   TODO : ONBOARD
    @PostMapping(value = "/onboard", produces = "application/json")
    public ResponseEntity<?> customerOnBoard(@RequestBody OnBoardDto accountRequest) throws Exception {
        JSONObject onBoardResponse = customerService.customerOnboard(accountRequest);
        return new ResponseEntity<>(onBoardResponse.toString(), HttpStatus.OK);
    }

    // todo : GET ONBOARDED CUSTOMERS BY ACCOUNT AND STATUS
    @GetMapping(value = "/onboard", produces = "application/json")
    public ResponseEntity<?> getOnBoardedCustomers(@RequestParam(required = false) String accountNumber,
                                                   @RequestParam(required = false) Integer status) {
        JSONObject onboardedCustomerByAccountOrStatus = customerService.getOnboardedCustomerByAccountOrStatus(accountNumber, status);

//
        return ResponseEntity.ok(onboardedCustomerByAccountOrStatus.toString());
    }

    // TODO : APPROVE ONBOARD
    @PostMapping(value = "/onboard-approval", produces = "application/json")
    public ResponseEntity<?> customerOnBoard(@RequestBody ApproveOnBoardDto request) throws Exception {
        JSONObject onBoardResponse = customerService.approveOnboard(request);
        return new ResponseEntity<>(onBoardResponse.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/eligibility", produces = "application/json")
    public ResponseEntity<?> getAccountEligibility(@RequestBody EligibilityRequest eligibilityRequest) throws Exception {

        EligibilityResponse accountEligibility = customerService.checkAccountEligibility(eligibilityRequest);

        return new ResponseEntity<>(accountEligibility, HttpStatus.OK);
    }


}
