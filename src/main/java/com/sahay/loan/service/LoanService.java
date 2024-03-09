package com.sahay.loan.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sahay.cbs.PostGLToGLTransferResponse;
import com.sahay.cbs.TxRequest;
import com.sahay.config.AsyncHttpConfig;
import com.sahay.config.CbsClient;
import com.sahay.customer.CustomerService;
import com.sahay.customer.model.Customer;
import com.sahay.customer.model.CustomerBranch;
import com.sahay.customer.repo.BranchRepository;
import com.sahay.customer.repo.CustomerRepository;
import com.sahay.exception.ApiException;
import com.sahay.loan.dto.*;
import com.sahay.loan.entity.Collateral;
import com.sahay.loan.entity.CbsPosting;
import com.sahay.loan.entity.Product;
import com.sahay.loan.entity.Request;
import com.sahay.loan.repo.CollateralRepository;
import com.sahay.loan.repo.CbsPostingRepo;
import com.sahay.loan.repo.OtpRepository;
import com.sahay.loan.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.asynchttpclient.RequestBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

//    dbo.GetProductSetupAsJSON()

    private final String SAHAY_API = "http://172.16.1.17:8013/channel/request";

    private final Double LOAN_LIMIT = 100_000.0;

    private final OtpRepository otpRepository;

    private final ProductRepository productRepository;

    private final CollateralRepository collateralRepository;

    private final CbsPostingRepo cbsPostingRepo;

    private final UtilityService utilityService;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    private final BranchRepository branchRepository;

    private final AsyncHttpConfig asyncHttp;

    @Value("${cbs.murabaha-principal-ledger}")
    private String E_MURABAHA_PRINCIPAL_LEDGER;

    @Value("${cbs.murabaha-pool-account}")
    private String E_MURABAHA_POOL_ACCOUNT;

    @Value("${cbs.murabaha-profit-ledger}")
    private String E_MURABAHA_PROFIT_LEDGER;

    @Value("${cbs.murabaha-profitable-recievable-ledger}")
    private String E_MURABAHA_PROFIT_RECEIVABLE;

    @Value("${cbs.currency}")
    private String CURRENCY;


    @Autowired
    private CbsClient cbsClient;

    private final JdbcTemplate jdbcTemplate;
    private final CollateralService collateralService;


//    private final AccountService accountService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // TODO : LOAN PRODUCTS
//    public List<Product> getLoanProducts() {
//        String procedureCall = "{CALL GetProductSetupAsJSON}";
//
//        log.info(procedureCall);
//
//        return jdbcTemplate.query(procedureCall, (resultSet, rowNum) -> {
//            try {
//                String jsonResult = resultSet.getString(1); // Assuming the JSON result is in the first column
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                // Configure ObjectMapper to use BigDecimal for floating-point numbers
//                objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
//                objectMapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
//
//                Product[] products = objectMapper.readValue(jsonResult, Object[].class);
//
//                List<Product> productList = Arrays.asList(products);
//
//                log.info("PRODUCTS: {}", productList);
//                return productList;
//            } catch (JsonProcessingException e) {
//                log.error("Error parsing JSON result", e);
//                throw new RuntimeException("Error parsing JSON result", e);
//            }
//        });
//    }

    // get loan product by id
    public Product getProductById(int id) {
        return productRepository.findById(id).get();
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

                    double twentyPercent = principalAmount * 0.2;

                    log.info("20% : {}", twentyPercent);
//                    String reference = generateReference();

                    JSONObject heldResponse = holdTheAmount(accountNumber, principalAmount, twentyPercent, confirmationRequest.getReference());
                    log.info("NEW HOLD RESPONSE : {}", heldResponse);
                    String parkTransactionId = heldResponse.optString("parkReference");
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
            request.setStatus(2);
            otpRepository.save(request);
            // get collateral by id

            String accountNumber = request.getAccountNumber();
            double principalAmount = request.getPrincipalAmount();

            Customer customerByAccountNumber = customerService.getCustomerByAccountNumber(accountNumber);


            String customerName = customerByAccountNumber.getFirstName() + " " + customerByAccountNumber.getMiddleName() + " " + customerByAccountNumber.getLastName();
            Product productById = getProductById(request.getProductId());


            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", "channel");
            requestBodyJson.put("password", "$_@C0NNEKT");
            requestBodyJson.put("messageType", "1200");
            requestBodyJson.put("serviceCode", "3002");
            requestBodyJson.put("transactionId", "digital-loan-" + UUID.randomUUID());
            requestBodyJson.put("parkTransactionId", request.getParkReference());
            requestBodyJson.put("msisdn", accountNumber);
            requestBodyJson.put("accountNumber", accountNumber);
            requestBodyJson.put("amount", String.valueOf(principalAmount));
            requestBodyJson.put("period", String.valueOf(productById.getProductRate()));
            requestBodyJson.put("productId", String.valueOf(productById.getId()));
            requestBodyJson.put("accountName", customerName);
            requestBodyJson.put("createDate", LocalDate.now());
            requestBodyJson.put("transactionType", "DLP");
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("LOAN APPLICATION REQUEST : {}", requestBodyJson);

            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SAHAY_API)
                    .setBody(requestBodyJson.toString());

            JSONObject createLoanResponse = asyncHttp.sendRequest(requestBuilder);

            log.info("LOAN APPLICATION RESPONSE : {}", createLoanResponse);

            String response = createLoanResponse.getString("response");

            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", createLoanResponse.getString("responseDescription"));
                return customResponse;
            }

            customResponse.put("response", "000");
            customResponse.put("responseDescription", "Loan application completed successfully");
            customResponse.put("accountNumber", accountNumber);
            customResponse.put("principalAmount", principalAmount);
            customResponse.put("reference", loanApplicationDto.getReference());

            // GET THE BRANCH PREFIX

            String PREFIX = getGlPrefix(request.getAccountNumber());

            log.info("BRANCH PREFIX : {}", PREFIX);

            log.info("GL ACCOUNT : {}", PREFIX + E_MURABAHA_POOL_ACCOUNT);

            // post to CBS GL
            TxRequest txRequest = new TxRequest();
            txRequest.setAmount(BigDecimal.valueOf(principalAmount));
            txRequest.setCreditAccount(E_MURABAHA_POOL_ACCOUNT);
            txRequest.setDebitAccount(E_MURABAHA_PRINCIPAL_LEDGER);
            txRequest.setCurrency(CURRENCY);
            txRequest.setNarration("FROM DIGITAL LOAN SERVICE");
            txRequest.setReference(UUID.randomUUID().toString());

            JSONObject cbsPostResponse = postToCbsGl(txRequest);

            log.info("CBS POST RESPONSE : {}", cbsPostResponse);
            // pick request with status 0 and update the collater

            otpRepository.findByStatusAndReference(1, loanApplicationDto.getReference());
            Collateral collateralByPhone = collateralService.getCollateralByPhone(request.getAccountNumber());

            collateralByPhone.setStatus(3);

            log.info("COLLATERAL STATUS : {}", collateralByPhone.getStatus());

            collateralRepository.save(collateralByPhone);

            return customResponse;

        } catch (HttpClientErrorException e) {
            customResponse.put("response", "999"); // or another appropriate error code
            customResponse.put("responseDescription", e.getMessage());
            return customResponse;

        } catch (Exception e) {
            // Handle other exceptions
            // Handle other exceptions
            customResponse.put("response", "999"); // or another appropriate error code
            customResponse.put("responseDescription", e.getMessage());
            return customResponse;
        }

    }


    public JSONObject holdTheAmount(String phoneNumber, double loanAmount, double parkAmount, String loanTransactionId) {

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
            requestBodyJson.put("parkAmount", String.valueOf(parkAmount));
            requestBodyJson.put("loanAmount", String.valueOf(loanAmount));
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("PARK REQUEST : {}", requestBodyJson);

            // Convert JSON object to string

            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SAHAY_API)
                    .setBody(requestBodyJson.toString());

            JSONObject parkResponse = asyncHttp.sendRequest(requestBuilder);
            // Print the response
            log.info("PARK RESPONSE : {}", parkResponse);

            String response = parkResponse.getString("response");

            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", parkResponse.getString("responseDescription"));
                return customResponse;
            }

            customResponse.put("response", "000");
            customResponse.put("responseDescription", "Amount successfully held on the account.");
            customResponse.put("holdAmount", parkAmount);
            customResponse.put("loanAmount", loanAmount);
            customResponse.put("accountNumber", phoneNumber);
            customResponse.put("reference", loanTransactionId);
            customResponse.put("parkReference", parkResponse.optString("transactionEntryId"));
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

            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", "channel");
            requestBodyJson.put("password", "$_@C0NNEKT");
            requestBodyJson.put("messageType", "1200");
            requestBodyJson.put("serviceCode", "3003");
            requestBodyJson.put("transactionId", "digital-loan-" + UUID.randomUUID());
            requestBodyJson.put("parkTransactionId", utilityService.getParkReferenceByReference(cancelLoanDto.getReference()));
            requestBodyJson.put("comments", cancelLoanDto.getReason());
            requestBodyJson.put("transactionType", "LPRKT");
            requestBodyJson.put("isCustomerInitiated", "1");
            requestBodyJson.put("msisdn", request.getAccountNumber());
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("UNPARK REQUEST : {}", requestBodyJson);

            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SAHAY_API)
                    .setBody(requestBodyJson.toString());

            JSONObject unParkResponse = asyncHttp.sendRequest(requestBuilder);

            // Print the response
            String response = unParkResponse.getString("response");

            log.info("UNPARK RESPONSE : {}", unParkResponse);

            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", unParkResponse.getString("responseDescription"));
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
            requestBodyJson.put("serviceCode", "3001");
            requestBodyJson.put("transactionId", "digital-loan-" + UUID.randomUUID());
            requestBodyJson.put("parkTransactionId", utilityService.getParkReferenceByReference(approveCancelDto.getReference()));
            requestBodyJson.put("comments", approveCancelDto.getComments());
            requestBodyJson.put("transactionType", "LPRKT");
            requestBodyJson.put("timestamp", Timestamp.from(Instant.now()));
            requestBodyJson.put("channel", "LMS");

            log.info("REVERSAL APPROVAL REQUEST : {}", requestBodyJson);

            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SAHAY_API)
                    .setBody(requestBodyJson.toString());

            JSONObject reversalResponse = asyncHttp.sendRequest(requestBuilder);
            String response = reversalResponse.getString("response");

            log.info(" REVERSAL APPROVAL RESPONSE : {}", reversalResponse);

            if (!response.equals("000")) {
                customResponse.put("response", response);
                customResponse.put("responseDescription", reversalResponse.getString("error_data"));
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


    // todo : perform GL to GL for loan creation and loan repayment

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

    // todo : GET BRANCH PREFIX BY CODE

    public String getGlPrefix(String accountNumber) {
        Optional<Customer> byCustomerAccount =
                customerRepository.findByCustomerAccount(accountNumber);

        Customer customer = byCustomerAccount.get();

        String branchCode = customer.getBranchCode();

        CustomerBranch customerBranch = branchRepository.findByBranchCode(branchCode).get();

        return customerBranch.getPrefix();
    }

    // TODO REPAYMENT POST TO CBS


    @Scheduled(fixedRate = 1800000)
    public void repaymentPostToCbs() throws ApiException {

        log.info("REPAYMENT POST TO CBS KICK OFF ========================== :{}");

        Optional<CbsPosting> topByStatus = cbsPostingRepo.findTopByStatus(0);

        if (topByStatus.isPresent()) {

            CbsPosting cbsPosting = topByStatus.get();

            log.info("CBS POSTING RECORD : {}", topByStatus.get());
            // first post
            TxRequest repaymentFirstPostRequest = new TxRequest();
            repaymentFirstPostRequest.setReference(UUID.randomUUID().toString());

            repaymentFirstPostRequest.setNarration("REPAYMENT FROM DIGITAL LOAN");
            repaymentFirstPostRequest.setAmount(BigDecimal.valueOf(cbsPosting.getPaidPrincipal()));
            repaymentFirstPostRequest.setCurrency(CURRENCY);
            repaymentFirstPostRequest.setCreditAccount(E_MURABAHA_PRINCIPAL_LEDGER);
            repaymentFirstPostRequest.setDebitAccount(E_MURABAHA_POOL_ACCOUNT);

            JSONObject firstGLPostResponse = postToCbsGl(repaymentFirstPostRequest);

            // second
            TxRequest repaymentSecondPostRequest = new TxRequest();
            repaymentFirstPostRequest.setReference(UUID.randomUUID().toString());

            repaymentSecondPostRequest.setNarration("REPAYMENT FROM DIGITAL LOAN");
            repaymentSecondPostRequest.setAmount(BigDecimal.valueOf(cbsPosting.getPaidPrincipal()));
            repaymentSecondPostRequest.setCurrency(CURRENCY);
            repaymentSecondPostRequest.setCreditAccount(E_MURABAHA_PROFIT_RECEIVABLE);
            repaymentSecondPostRequest.setDebitAccount(E_MURABAHA_PROFIT_LEDGER);

            JSONObject secondGLPostResponse = postToCbsGl(repaymentFirstPostRequest);

            // if both success update the status to 1
            if (firstGLPostResponse.get("result").equals("SUCCESS") && secondGLPostResponse.get("result").equals("SUCCESS")) {
                cbsPosting.setStatus(1);
                cbsPostingRepo.save(cbsPosting);
            }

        }
    }


}
