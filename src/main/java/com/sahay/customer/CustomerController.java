package com.sahay.customer;


import com.sahay.customer.dto.ApproveOnBoardDto;
import com.sahay.customer.dto.OnBoardDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    //   TODO : ONBOARD
    @PostMapping(value = "/onboard", produces = "application/json")
    public ResponseEntity<?> customerOnBoard(@RequestBody OnBoardDto accountRequest) throws Exception {
        JSONObject onBoardResponse = customerService.customerOnboard(accountRequest);
        return new ResponseEntity<>(onBoardResponse.toString(), HttpStatus.OK);
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
