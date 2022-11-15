package com.sahay.controller;

import com.sahay.dto.ResponseMessage;
import com.sahay.cbs.*;
import com.sahay.client.CbsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cbs")
public class CbsController {

    @Autowired
    private   CbsClient cbsClient;

    @PostMapping(value = "/create-customer-account", consumes = "application/json")
    public ResponseEntity<?> createCustomerController(@RequestBody CcRequest request) throws Exception {

        CreateCustomerResponse customerResponse = cbsClient.createCustomerInfo(request);
        CcResponse response = customerResponse.getReturn();
        String accountTitle = request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName();

        boolean success = customerResponse.getReturn().getMessage().equals("Success");

        if (success) {
            CaRequest createAccountRequest = new CaRequest();

//            createAccountRequest.setReference(response.getReference());
            createAccountRequest.setProductId(26l);
            createAccountRequest.setAccountTitle(accountTitle);
            createAccountRequest.setCustomerNumber(response.getCustomerNumber());
            createAccountRequest.setCampaignRefId(327l);
            createAccountRequest.setRelOfficer(882l);
            createAccountRequest.setOpeningReasonId(491l);
            createAccountRequest.setSourceOfFundId(231l);
            createAccountRequest.setRiskClassId(551l);
            CreateDepositAccountResponse createDepositAccountResponse = cbsClient.createCustomerDepositAccount(createAccountRequest);

            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setStatus("000");
            responseMessage.setMessage("Customer account created successfully!");
            responseMessage.setAccountNumber(createDepositAccountResponse.getReturn().getAccountNumber());
            responseMessage.setCustomerNumber(response.getCustomerNumber());
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } else {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setStatus("999");
            responseMessage.setMessage("Something failed while creating customer account");
            return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping(value = "/create-account", consumes = "application/json")
    public CreateDepositAccountResponse createAccount(@RequestBody CaRequest request) throws Exception {

        String reference = request.getReference();
        Long productId = request.getProductId();
        String customerNo = request.getCustomerNumber();
        String accountT = request.getAccountTitle();
        Long campaignRefId = request.getCampaignRefId();
        Long relOfficer = request.getRelOfficer();
        Long openingRId = request.getOpeningReasonId();
        Long sourceId = request.getSourceOfFundId();
        Long riskclassid = request.getRiskClassId();

        System.out.println("reference = " + reference);
        System.out.println("prodcut = " + productId);
        System.out.println("customerNo = " + customerNo);
        System.out.println("accountTitle = " + accountT);
        System.out.println("campaignRefId = " + campaignRefId);
        System.out.println("rel officer = " + relOfficer);
        System.out.println("openId = " + openingRId);
        System.out.println("sourceId = " + sourceId);
        System.out.println("riskclassId = " + riskclassid);
        return cbsClient.createCustomerDepositAccount(request);
    }

}
