package com.sahay.account;

import com.sahay.cbs.AcQuery;
import com.sahay.cbs.QueryDepositAccountResponse;
import com.sahay.cbs.QueryDepositBalanceResponse;
import com.sahay.config.CbsClient;
import com.sahay.exception.ApiException;
import com.sahay.loan.OtpModel;
import com.sahay.loan.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final RestTemplate restTemplate;

    @Autowired
    private CbsClient cbsClient;

    private final OtpRepository otpRepository;

    private final Double PERCENTAGE = 20.0;
    private final String SAHAY_API = "http://172.16.1.17:8013/channel/request";

    //    @Value("${sahay.sms-endpoint}")
    private final String SMS_API = "http://172.16.3.25:8013/channel/request";

    public JSONObject getSahayAccount(String accountNumber) {
        JSONObject customResponse = new JSONObject();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject requestBody = new JSONObject();
            requestBody.put("username", "channel");
            requestBody.put("password", "$_@C0NNEKT");
            requestBody.put("messageType", "1200");
            requestBody.put("serviceCode", "180");
            requestBody.put("transactionType", "NAM");
            requestBody.put("transactionId", "Digital-loan-" + UUID.randomUUID());
            requestBody.put("detType", "1");
            requestBody.put("accountNumber", accountNumber);
            requestBody.put("timestamp", Timestamp.from(Instant.now()));
            requestBody.put("channel", "DIGITAL-LOAN");

            // Create the request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
            log.info("request : {}", requestEntity);

            // Make the API call
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    SAHAY_API,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            log.info("ACCOUNT LOOKUP RESPONSE : {}", responseEntity);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Parse the response body as a JSONObject
                JSONObject response = new JSONObject(responseEntity.getBody());

                log.info("ACCOUNT LOOKUP RESPONSE : {}", response);

                // Check if the "name" key is present in the response
                if (response.has("name")) {
                    // Account exists
                    return response;
                } else {
                    customResponse.put("response", "004");
                    customResponse.put("responseDescription", "Account doesn't exist!");
                }
            } else {
                customResponse.put("response", "999");
                customResponse.put("responseDescription", "Error in account lookup. HTTP status: " + responseEntity.getStatusCodeValue());
            }

            // Return the custom response
            return customResponse;
        } catch (Exception e) {
            customResponse.put("response", "999");
            customResponse.put("responseDescription", e.getMessage());

            // Handle exceptions
            return customResponse;
        }
    }


    public JSONObject getSahayBalance(String accountNumber) {
        JSONObject customResponse = new JSONObject();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject requestBody = new JSONObject();
            requestBody.put("username", "channel");
            requestBody.put("password", "$_@C0NNEKT");
            requestBody.put("messageType", "1200");
            requestBody.put("serviceCode", "150");
            requestBody.put("transactionType", "BAI");
            requestBody.put("transactionId", "murabaha-" + UUID.randomUUID());
            requestBody.put("msisdn", accountNumber);
            requestBody.put("accountNumber", accountNumber);
            requestBody.put("timestamp", Timestamp.from(Instant.now()));
            requestBody.put("channel", "DIGITAL-LOAN");

            // Create the request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

            // Make the API call
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    SAHAY_API,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Parse the response body as a JSONObject
                JSONObject response = new JSONObject(responseEntity.getBody());

                // Return the API response
                return response;
            } else {
                customResponse.put("response", "999");
                customResponse.put("responseDescription", "Error in account balance. HTTP status: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            customResponse.put("response", "999");
            customResponse.put("responseDescription", e.getMessage());
        }

        // Handle exceptions and return the custom response
        return customResponse;
    }

    // rays

    public JSONObject getCBSAccount(String accountNumber) throws ApiException {


        JSONObject customResponse = new JSONObject();

        AcQuery accountQuery = new AcQuery();
        accountQuery.setAccountNumber(accountNumber);
        accountQuery.setReference(UUID.randomUUID().toString());

        QueryDepositAccountResponse accountResponse = cbsClient.getCBSAccount(accountQuery);

        JSONObject response = new JSONObject(accountResponse).getJSONObject("return");

        String result = response.getString("result");

        if (!result.equals("SUCCESS")) {

            customResponse.put("response", "004");
            customResponse.put("responseDescription", result);
            return customResponse;
        }


        log.info("CBS ACCOUNT RESPONSE : {}", response);
        return response;

    }

    // get cbs balance
    public JSONObject getCBSBalance(String accountNumber) throws ApiException {


        var customResponse = new JSONObject();

        AcQuery accountQuery = new AcQuery();
        accountQuery.setAccountNumber(accountNumber);
        accountQuery.setReference(UUID.randomUUID().toString());

        QueryDepositBalanceResponse accountResponse = cbsClient.getAccountBalance(accountQuery);

        JSONObject response = new JSONObject(accountResponse).getJSONObject("return");

        String result = response.getString("result");

        if (!result.equals("SUCCESS")) {

            customResponse.put("response", "004");
            customResponse.put("responseDescription", result);
            return customResponse;
        }

        log.info("CBS ACCOUNT RESPONSE : {}", response);
        return response;

    }


    // todo eligibility test

    public AccountResponse checkAccountEligibility(AccountRequest accountRequest) throws Exception {

        if (!accountRequest.getAccountNumber().startsWith("2519")) {

            JSONObject cbsAccountResponse = getCBSAccount(accountRequest.getAccountNumber());
            log.info("CBS ACCOUNT RESPONSE : {}", cbsAccountResponse);
            // call balance api now
            JSONObject cbsBalanceResponse = getCBSBalance(accountRequest.getAccountNumber());

            log.info("CBS BALANCE RESPONSE :{}", cbsBalanceResponse);

            JSONObject accountObject = cbsAccountResponse.getJSONObject("account");

            log.info("ACCOUNT OBJECT : {}", accountObject);

            log.info("CBS BALANCE : {}", cbsBalanceResponse.getDouble("availableBalance"));

            boolean eligible = isEligible(accountRequest.getAmount(), cbsBalanceResponse.getDouble("availableBalance"));


            AccountResponse accountResponse = new AccountResponse("000", "success",
                    accountObject.getString("accountName"), accountRequest.getAccountNumber(), eligible);

            return accountResponse;
        }


        JSONObject accountLookupResponse = getSahayAccount(accountRequest.getAccountNumber());

        JSONObject accountBalanceResponse = getSahayBalance(accountRequest.getAccountNumber());
//
        String balance = accountBalanceResponse.getString("accountBalance").substring(3);

        boolean eligible = isEligible(accountRequest.getAmount(), Double.valueOf(balance));

        if (eligible) {

            // otp
            var random = new Random();
            int OTP = 1000 + random.nextInt(9000);

            var otpModel = new OtpModel();
            otpModel.setOtp(OTP);
            otpModel.setAccountNumber(accountRequest.getAccountNumber());
            otpModel.setCreatedAt(LocalDateTime.now());
            otpRepository.save(otpModel);
            // send confirmation message to client
            String message = "Dear customer! You are eligible to apply for digital loan. Please use the following OTP for confirmation: " + OTP;
            sendConfirmationMessage(accountRequest.getAccountNumber(), message);

        }

        AccountResponse accountResponse = new AccountResponse("000", "success",
                accountLookupResponse.getString("name"), accountRequest.getAccountNumber(), eligible);
        return accountResponse;
    }

    // calculate 20% percentage

    public boolean isEligible(double requestAmount, double balance) {
        return balance >= requestAmount * PERCENTAGE / 100;
    }

    // todo : SEND confirmation with OTP

    public void sendConfirmationMessage(String phoneNumber, String message) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put("username", "channel");
            jsonPayload.put("password", "$_@C0NNEKT");
            jsonPayload.put("messageType", "1200");
            jsonPayload.put("serviceCode", "290");
            jsonPayload.put("transactionType", "NOT");
            jsonPayload.put("transactionId", "Digital-loan-" + UUID.randomUUID());
            jsonPayload.put("institution", "");
            jsonPayload.put("businessCode", "");
            jsonPayload.put("customerName", "");
            jsonPayload.put("alertType", "GEN");
            jsonPayload.put("user", "");
            jsonPayload.put("accessPassword", "");
            jsonPayload.put("toMsisdn", phoneNumber);
            jsonPayload.put("message", message);
            jsonPayload.put("timestamp", "20200101120000");
            jsonPayload.put("channel", "PORTAL");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload.toString(), headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(SMS_API, requestEntity, String.class);

            // Handle the response as needed
            String responseBody = responseEntity.getBody();


            System.out.println("API Response: " + responseBody);
        } catch (Exception e) {
            // Handle any exceptions that might occur during the API call
            e.printStackTrace();
        }


    }


}
