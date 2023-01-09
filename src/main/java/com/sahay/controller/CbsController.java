package com.sahay.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sahay.dto.ResponseMessage;
import com.sahay.cbs.*;
import com.sahay.client.CbsClient;
import com.sahay.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cbs")
@Slf4j
public class CbsController {

    public final CustomerRepository customerRepository;

    private final CbsClient cbsClient;

    @PostMapping(value = "/create-customer-account", consumes = "application/json")
    public ResponseEntity<?> createCustomerController(@RequestBody CcRequest request) throws Exception {

        log.info("customer request : {}" , request);
        var customerResponse = cbsClient.createCustomerInfo(request);
        var response = customerResponse.getReturn();
        String accountTitle = request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName();

        boolean success = customerResponse.getReturn().getMessage().equals("Success");

        if (!success) {
            var responseMessage = new ResponseMessage();
            responseMessage.setStatus("999");
            responseMessage.setMessage("Something failed while creating customer account");
            return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var createAccountRequest = new CaRequest();



        createAccountRequest.setReference(response.getReference());
        createAccountRequest.setProductId(26l);
        createAccountRequest.setAccountTitle(accountTitle);
        createAccountRequest.setCustomerNumber(response.getCustomerNumber());
        createAccountRequest.setCampaignRefId(327l);
        createAccountRequest.setRelOfficer(882l);
        createAccountRequest.setOpeningReasonId(491l);
        createAccountRequest.setSourceOfFundId(231l);
        createAccountRequest.setRiskClassId(551l);

        log.info("account request : {}" , createAccountRequest);
        var createDepositAccountResponse = cbsClient.createCustomerDepositAccount(createAccountRequest);

        var responseMessage = new ResponseMessage();
        responseMessage.setStatus("000");
        responseMessage.setMessage("Customer account created successfully!");
        responseMessage.setAccountName(accountTitle);
        responseMessage.setPhoneNumber(request.getMobileNumber());
        responseMessage.setAccountNumber(createDepositAccountResponse.getReturn().getAccountNumber());

        responseMessage.setCustomerNumber(response.getCustomerNumber());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }
//    @PostMapping(value = "/create-account", consumes = "application/json")
//    public CreateDepositAccountResponse createAccount(@RequestBody CaRequest request) throws Exception {
//        return cbsClient.createCustomerDepositAccount(request);
//    }

}
