package com.sahay.loan.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sahay.cbs.PostGLToGLTransferResponse;
import com.sahay.cbs.TxRequest;
import com.sahay.config.CbsClient;
import com.sahay.exception.ApiException;
import com.sahay.loan.dto.*;
import com.sahay.loan.entity.Request;
import com.sahay.loan.repo.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

//    dbo.GetProductSetupAsJSON()

    private final String SAHAY_API = "http://172.16.1.17:8013/channel/request";

    private final Double LOAN_LIMIT = 100_000.0;

    private final RestTemplate restTemplate;

    private final OtpRepository otpRepository;

    private final UtilityService utilityService;

    @Autowired
    private CbsClient cbsClient;
    
    private final JdbcTemplate jdbcTemplate;

//    private final AccountService accountService;

    // TODO : LOAN PRODUCTS

    public List<Object> getLoanProducts() {
        String procedureCall = "{CALL GetProductSetupAsJSON}";

        log.info(procedureCall);

        return jdbcTemplate.query(procedureCall, (resultSet, rowNum) -> {
            try {
                String jsonResult = resultSet.getString(1); // Assuming the JSON result is in the first column

                ObjectMapper objectMapper = new ObjectMapper();
                // Configure ObjectMapper to use BigDecimal for floating-point numbers
                objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
                objectMapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);

                Object[] products = objectMapper.readValue(jsonResult, Object[].class);

                List<Object> productList = Arrays.asList(products);

                log.info("PRODUCTS: {}", productList);
                return productList;
            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON result", e);
                throw new RuntimeException("Error parsing JSON result", e);
            }
        });
    }

    // TODO : LOAN CONFIRMATION
    public JSONObject confirmLoanApplication(LoanConfirmationDto confirmationRequest) {
        var customResponse = new JSONObject();
        return otpRepository.findByReferenceAndOtp(
                confirmationRequest.getReference(), confirmationRequest.getOtp())
                .map(otpEntity -> {
                    // hold the 20% here
                    double principalAmount = otpEntity.getPrincipalAmount();
                    String accountNumber = otpEntity.getAccountNumber();

                    double twentyPercent = principalAmount * 0.02;

                    log.info("20% : {}", twentyPercent);
//                    String reference = generateReference();

                    JSONObject heldResponse = holdTheAmount(accountNumber, twentyPercent, confirmationRequest.getReference());
                    log.info("NEW HOLD RESPONSE : {}", heldResponse);
                    String parkTransactionId = heldResponse.getString("reference");
                    log.info("HELD RESPONSE : {}", heldResponse);
                    // update otp record
//                    otpEntity.setReference(reference);
//                    otpEntity.setPrincipalAmount();
                    otpEntity.setDepositAmount(twentyPercent);
                    otpEntity.setParkReference(parkTransactionId);
                    otpEntity.setStatus(1);
                    otpRepository.save(otpEntity);
                    customResponse.put("response", "000");
                    customResponse.put("responseDescription", "Loan Application process of ETB " + principalAmount + " is initiated, and 20% compulsory saving of amount " + twentyPercent + " is withheld");
                    customResponse.put("reference", confirmationRequest.getReference());
//                    customResponse.put("accountNumber" , co)
//                    otpRepository.delete(otpEntity);
                    return customResponse;
                })
                .orElseGet(() -> {

                    customResponse.put("response", "001"); // Assuming "001" represents an error code
                    customResponse.put("responseDescription", "Confirmation failed");
                    return customResponse;
                });
    }

    // loan application
    public JSONObject createLoanApplication(LoanApplicationDto loanApplicationDto) {

        var customResponse = new JSONObject();

        try {
            // Make the POST request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Create request body using JSONObject


            Request request = otpRepository.findByReference(loanApplicationDto.getReference()).get();

            String accountNumber = request.getAccountNumber();
            double principalAmount = request.getPrincipalAmount();

            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", "channel");
            requestBodyJson.put("password", "$_@C0NNEKT");
            requestBodyJson.put("messageType", "1200");
            requestBodyJson.put("serviceCode", "3002");
            requestBodyJson.put("transactionId", "digital-loan" + UUID.randomUUID());
            requestBodyJson.put("parkTransactionId", utilityService.getParkReferenceByReference(loanApplicationDto.getReference()));
            requestBodyJson.put("msisdn", accountNumber);
            requestBodyJson.put("accountNumber", accountNumber);
            requestBodyJson.put("amount", String.valueOf(principalAmount));
            requestBodyJson.put("period", String.valueOf(loanApplicationDto.getPeriod()));
            requestBodyJson.put("productId", String.valueOf(loanApplicationDto.getProductId()));
            requestBodyJson.put("accountName", loanApplicationDto.getCustomerName());
            requestBodyJson.put("createDate", LocalDate.now());
            requestBodyJson.put("transactionType", "DLP");
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("LOAN APPLICATION REQUEST : {}", requestBodyJson);


            // Create the HTTP entity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(SAHAY_API, requestEntity, String.class);

            JSONObject responseBodyJson = new JSONObject(responseEntity.getBody());

            log.info("LOAN APPLICATION RESPONSE : {}", responseBodyJson);

            String response = responseBodyJson.getString("response");


            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", responseBodyJson.getString("responseDescription"));
                return customResponse;
            }

            customResponse.put("response", "000");
            customResponse.put("responseDescription", "Loan application completed successfully");
//            customResponse.put("accountNumber", loanApplicationRequest.getAccountNumber());
//            customResponse.put("amount", loanApplicationRequest.getAmount());
            customResponse.put("reference", loanApplicationDto.getReference());

            // post to CBS GL
//            TxRequest txRequest = new TxRequest();
//            txRequest.setAmount(BigDecimal.valueOf(loanApplicationRequest.getAmount()));
//            txRequest.setCreditAccount("cbs-murabaha-gl");
//            txRequest.setDebitAccount("digital-loan-gl");
//            txRequest.setCurrency("ETB");
//            txRequest.setNarration("FROM DIGITAL LOAN SERVICE");
//
//            postToCbsGl(txRequest);

            return customResponse;

        } catch (HttpClientErrorException e) {
            customResponse.put("response", "999"); // or another appropriate error code
            customResponse.put("responseDescription", e.getMessage());
            return customResponse;

        } catch (Exception e) {
            // Handle other exceptions
            customResponse.put("response", "999"); // or another appropriate error code
            customResponse.put("responseDescription", e.getMessage());
            return customResponse;
        }


    }


    public JSONObject holdTheAmount(String phoneNumber, double amount, String loanTransactionId) {

        var customResponse = new JSONObject();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", "channel");
            requestBodyJson.put("password", "$_@C0NNEKT");
            requestBodyJson.put("messageType", "1200");
            requestBodyJson.put("serviceCode", "3000");
            requestBodyJson.put("transactionId", "digital-loan-" + UUID.randomUUID());
            requestBodyJson.put("loanReqTransactionId", loanTransactionId);
            requestBodyJson.put("msisdn", phoneNumber);
            requestBodyJson.put("customerAccount", phoneNumber);
            requestBodyJson.put("transactionType", "LPRKT");
            requestBodyJson.put("amount", String.valueOf(amount));
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("PARK REQUEST : {}", requestBodyJson);

            // Convert JSON object to string
            String requestBody = requestBodyJson.toString();

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Make the HTTP POST request
            ResponseEntity<String> responseEntity = restTemplate.exchange(SAHAY_API, HttpMethod.POST, requestEntity, String.class);

            // Print the response
            var responseBody = new JSONObject(responseEntity.getBody());

            log.info("PARK RESPONSE : {}", responseBody);

            String response = responseBody.getString("response");

            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", responseBody.getString("responseDescription"));
                return customResponse;
            }

            customResponse.put("response", "000");
            customResponse.put("responseDescription", "Amount successfully held on the account.");
            customResponse.put("holdAmount", amount);
            customResponse.put("accountNumber", phoneNumber);
            customResponse.put("reference", responseBody.getString("transactionRef"));
            return customResponse;

        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors (e.g., 4xx status codes)
            customResponse.put("response", "999");
            customResponse.put("responseDescription", e.getRawStatusCode());
            return customResponse;

        } catch (Exception e) {
            // Handle other exceptions
            customResponse.put("response", "999");
            customResponse.put("responseDescription", "An error occurred: " + e.getMessage());
            return customResponse;

        }
    }


    public JSONObject cancelApplication(CancelLoanDto cancelLoanDto) {
        var customResponse = new JSONObject();

        try {
            Request request = otpRepository.findByReference(cancelLoanDto.getReference()).get();


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", "channel");
            requestBodyJson.put("password", "$_@C0NNEKT");
            requestBodyJson.put("messageType", "1200");
            requestBodyJson.put("serviceCode", "3001");
            requestBodyJson.put("transactionId", "digital-loan-" + UUID.randomUUID());
            requestBodyJson.put("parkTransactionId", utilityService.getParkReferenceByReference(cancelLoanDto.getReference()));
            requestBodyJson.put("comments", cancelLoanDto.getReason());
            requestBodyJson.put("transactionType", "LPRKT");
            requestBodyJson.put("isCustomerInitiated", "1");
            requestBodyJson.put("msisdn", request.getAccountNumber());
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("UNPARK REQUEST : {}", requestBodyJson);

            String requestBody = requestBodyJson.toString();

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Make the HTTP POST request
            ResponseEntity<String> responseEntity = restTemplate.exchange(SAHAY_API, HttpMethod.POST, requestEntity, String.class);

            // Print the response
            var responseBody = new JSONObject(responseEntity.getBody());
            String response = responseBody.getString("response");

            log.info("UNPARK RESPONSE : {}", responseBody);

            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", responseBody.getString("responseDescription"));
                return customResponse;
            }

//            Request request = otpRepository.findByReference(cancelLoanRequest.getReference()).get();
//            request.setStatus(0);
//            request.setCreatedAt(LocalDateTime.now());
//            request.setDepositAmount(0);
//            otpRepository.save(request);

            customResponse.put("response", "000");
            customResponse.put("responseDescription", "Application cancel request submitted successfully!");
            customResponse.put("reference", cancelLoanDto.getReference());
            customResponse.put("accountNumber", request.getAccountNumber());
            return customResponse;

        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors (e.g., 4xx status codes)
            customResponse.put("response", "999");
            customResponse.put("responseDescription", "Client error: " + e.getRawStatusCode() + " " + e.getStatusText());
            return customResponse;

        } catch (Exception e) {
            // Handle other exceptions
            customResponse.put("response", "999");
            customResponse.put("responseDescription", "An error occurred: " + e.getMessage());
            return customResponse;

        }
    }

    public JSONObject approveCancellation(ApproveCancelDto approveCancelDto) {
        var customResponse = new JSONObject();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", "channel");
            requestBodyJson.put("password", "$_@C0NNEKT");
            requestBodyJson.put("messageType", "1200");
            requestBodyJson.put("serviceCode", "3003");
            requestBodyJson.put("transactionId", "digital-loan-" + UUID.randomUUID());
            requestBodyJson.put("parkTransactionId", utilityService.getParkReferenceByReference(approveCancelDto.getReference()));
            requestBodyJson.put("comments", approveCancelDto.getComments());
            requestBodyJson.put("transactionType", "LPRKT");
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("REVERSAL APPROVAL REQUEST : {}", requestBodyJson);

            String requestBody = requestBodyJson.toString();

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            // Make the HTTP POST request
            ResponseEntity<String> responseEntity = restTemplate.exchange(SAHAY_API, HttpMethod.POST, requestEntity, String.class);
            // Print the response
            var responseBody = new JSONObject(responseEntity.getBody());
            String response = responseBody.getString("response");

            log.info("PARK REVERSAL RESPONSE : {}", responseBody);

            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", responseBody.getString("error_data"));
                return customResponse;
            }

            customResponse.put("response", "000");
            customResponse.put("responseDescription", "Loan application cancelled , 20% refunded");
            customResponse.put("reference", approveCancelDto.getReference());
            return customResponse;

        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors (e.g., 4xx status codes)
            customResponse.put("response", "999");
            customResponse.put("responseDescription", "Client error: " + e.getRawStatusCode() + " " + e.getStatusText());
            return customResponse;
        } catch (Exception e) {
            // Handle other exceptions
            customResponse.put("response", "999");
            customResponse.put("responseDescription", "An error occurred: " + e.getMessage());
            return customResponse;
        }

    }

    public JSONObject calculateLoan(LoanCalculationDto loanCalculationDto) {

        var customResponse = new JSONObject();
        if (loanCalculationDto.getAmount() > LOAN_LIMIT) {
            customResponse.put("response", "003");
            customResponse.put("responseDescription", "Loan amount can't exceed 100K !");
            return customResponse;
        }

        double markUp = loanCalculationDto.getAmount() * 0.02;

        customResponse.put("response", "000");
        customResponse.put("responseDescription", "successful");
        customResponse.put("period", loanCalculationDto.getPeriod() + " months");
        customResponse.put("markUp", markUp);
        customResponse.put("amountBefore", loanCalculationDto.getAmount());
        customResponse.put("amountAfter", loanCalculationDto.getAmount() + markUp);

        log.info("LOAN AFTER CALCULATION : {}", customResponse);

        return customResponse;
    }


    // todo : perform GL to GL
    public JSONObject postToCbsGl(TxRequest txRequest) throws ApiException {
        var customResponse = new JSONObject();

        PostGLToGLTransferResponse postGLToGLTransferResponse = cbsClient.postGlToGl(txRequest);
        JSONObject postGlResponse = new JSONObject(postGLToGLTransferResponse).getJSONObject("return");
        String result = postGlResponse.getString("result");
        if (!result.equals("SUCCESS")) {
            customResponse.put("response", "004");
            customResponse.put("responseDescription", result);
            return customResponse;
        }
        log.info("CBS POST GL RESPONSE : {}", postGlResponse);
        return postGlResponse;
    }


    // todo : LOAN RELATED UTILITIES


}
