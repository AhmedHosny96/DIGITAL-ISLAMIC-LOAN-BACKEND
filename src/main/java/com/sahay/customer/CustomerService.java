package com.sahay.customer;

import com.sahay.cbs.AcQuery;
import com.sahay.cbs.QueryDepositAccountResponse;
import com.sahay.cbs.QueryDepositBalanceResponse;
import com.sahay.config.AsyncHttpConfig;
import com.sahay.config.CbsClient;
import com.sahay.customer.dto.ApproveOnBoardDto;
import com.sahay.customer.dto.CreateCustomerDocument;
import com.sahay.customer.dto.OnBoardDto;
import com.sahay.customer.model.Customer;
import com.sahay.customer.model.CustomerBranch;
import com.sahay.customer.model.CustomerDocument;
import com.sahay.customer.repo.BranchRepository;
import com.sahay.customer.repo.CustomerDocumentRepository;
import com.sahay.customer.repo.CustomerRepository;
import com.sahay.dto.CustomResponse;
import com.sahay.exception.ApiException;
import com.sahay.exception.CustomException;
import com.sahay.loan.entity.Collateral;
import com.sahay.loan.entity.Guarantor;
import com.sahay.loan.entity.Request;
import com.sahay.loan.repo.OtpRepository;
import com.sahay.loan.service.CollateralService;
import com.sahay.loan.service.GuarantorService;
import com.sahay.loan.service.UtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.asynchttpclient.RequestBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final JdbcTemplate jdbcTemplate;

    private final CustomerRepository customerRepository;

    private final BranchRepository branchRepository;


    private final CustomerDocumentRepository customerDocumentRepository;


    private final GuarantorService guarantorService;

    private final CollateralService collateralService;

//    private final LoanService loanService;

    private final AsyncHttpConfig asyncHttp;
    private final DataSource dataSource;

    private final UtilityService utilityService;


    @Autowired
    private CbsClient cbsClient;

    private final OtpRepository otpRepository;

    private final Double PERCENTAGE = 20.0;


    private final String SAHAY_API = "http://172.16.1.17:8013/channel/request";

    //    @Value("${sahay.sms-endpoint}")
    private final String SMS_API = "http://172.16.3.25:8013/channel/request";


    public List<CustomerDocument> getCustomerDocumentById(int customerId) {
        return customerDocumentRepository.findCustomerDocumentByCustomerId(customerId);
    }

    //TODO: CUSTOMER ONBOARDING


    public JSONObject customerOnboard(OnBoardDto request) {
        JSONObject response = new JSONObject();
        log.info("CUSTOMER ONBOARD REQUEST : {}", request);
        try {
            jdbcTemplate.execute((ConnectionCallback<Object>) con -> {
                try (CallableStatement call = con.prepareCall("{CALL CustomerOnboarding(?, ?, ?, ? , ? , ?)}")) {
                    call.setString(1, request.getAccountNumber().trim());
                    call.setString(2, request.getBranchCode().trim());
                    call.setString(3, request.getCreatedBy().trim());
                    call.setString(4, request.getComment().trim());
                    call.setString(5, String.valueOf(request.getAppraisedAmount()));
//                    call.setString(, request.getComment().trim());
                    call.registerOutParameter(6, Types.INTEGER); // Assuming @MsgId is a VARCHAR
                    call.execute();
                    // You may retrieve output parameters here if needed

                    int msgId = call.getInt(6);

//                    ResultSet resultSet = call.getResultSet();
                    log.info("RESULT SET : {}", msgId);

                    if (msgId == 0) {
                        response.put("response", "000");
                        response.put("responseDescription", "Customer onboarded initiated");
                        return response;
                    } else {
                        response.put("response", "004");
                        response.put("responseDescription", "Customer already onboarded");
                        return response;
                    }
                }
            });
        } catch (DataAccessException e) {

            log.warn("ERROR OCCURED WHILE ONBOARDING CUSTOMER");

            response.put("response", "999");
            response.put("responseDescription", e.getMessage());
            // Handle exception as appropriate
        }
        return response;
    }

    //TODO : APPROVE ONBOARD

    public JSONObject approveOnboard(ApproveOnBoardDto request) {
        JSONObject response = new JSONObject();
        request.setVerifiedDate(Date.valueOf(LocalDate.now()));
        request.setStatus(1); // Assuming 2 for verified status

        try {
            jdbcTemplate.execute((ConnectionCallback<Object>) con -> {
                try (CallableStatement call = con.prepareCall("{CALL VerifyCustomer(?, ?, ?, ?, ?, ?)}")) {
                    call.setString(1, request.getAccountNumber().trim());
                    call.setDouble(2, request.getAppraisedAmount());
                    call.setString(3, String.valueOf(request.getVerifiedBy()).trim());
                    call.setDate(4, (Date) request.getVerifiedDate());
                    call.setString(5, request.getComment().trim());
                    call.setInt(6, request.getStatus());
                    call.execute();

                    response.put("response", "000");
                    response.put("responseDescription", "Customer verified successfully");

                    return response; // return value not used
                }
            });
        } catch (DataAccessException e) {
            response.put("response", "999");
            response.put("responseDescription", e.getMessage());
        }
        return response;
    }

    // todo : GET ONBOARDED CUSTOMERS BY STATUS AND ACCOUNT

    public List<Map<String, Object>> getOnboardedCustomer(String accountNumber, Integer status) {
        String sql = "{CALL GetOnboardedCustomer(?, ?)}";
        Object[] params = {accountNumber, status};

        Map<String, Object> response = new HashMap<>();

        try {
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, params);

            log.info("maps : {}", maps);

            if (maps.isEmpty()) {
                response.put("response", "004");
                response.put("responseDescription", "No customer data found");
                return Collections.singletonList(response);
            }

            return maps;
        } catch (EmptyResultDataAccessException e) {
            // If no customer found, return a predefined message
            response.put("response", "004");
            response.put("responseDescription", "Customer not found");
            return Collections.singletonList(response);
        }
    }


    // GET ALL SAHAY CUSTOMERS
    public String getCustomer(String accountNumber) {
        String sql = "{CALL GetCustomer(?, ?)}";
        String customerJson = null;

        try (CallableStatement cs = jdbcTemplate.getDataSource().getConnection().prepareCall(sql)) {
            cs.setString(1, accountNumber);
            cs.registerOutParameter(2, Types.NVARCHAR);
            cs.execute();
            customerJson = cs.getString(2);
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }

        return customerJson;
    }

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

            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SAHAY_API)
                    .setBody(requestBody.toString());

            JSONObject accountLookupResponse = asyncHttp.sendRequest(requestBuilder);

            log.info("ACCOUNT LOOKUP RESPONSE : {}", accountLookupResponse);

            if (accountLookupResponse.getString("name") == null) {
                customResponse.put("response", "004");
                customResponse.put("responseDescription", "Account doesn't exist!");
                return customResponse;
            }
            return accountLookupResponse;
        } catch (Exception e) {
            customResponse.put("response", "999");
            customResponse.put("responseDescription", e.getMessage());

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
            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SAHAY_API)
                    .setBody(requestBody.toString());

            JSONObject accountBalanceResponse = asyncHttp.sendRequest(requestBuilder);

            return accountBalanceResponse;

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

    public EligibilityResponse checkAccountEligibility(EligibilityRequest eligibilityRequest) throws Exception {

        if (!eligibilityRequest.getAccountNumber().startsWith("2519")) {

            JSONObject cbsAccountResponse = getCBSAccount(eligibilityRequest.getAccountNumber());
            log.info("CBS ACCOUNT RESPONSE : {}", cbsAccountResponse);
            // call balance api now
            JSONObject cbsBalanceResponse = getCBSBalance(eligibilityRequest.getAccountNumber());

            log.info("CBS BALANCE RESPONSE :{}", cbsBalanceResponse);

            JSONObject accountObject = cbsAccountResponse.getJSONObject("account");

            log.info("ACCOUNT OBJECT : {}", accountObject);

            log.info("CBS BALANCE : {}", cbsBalanceResponse.getDouble("availableBalance"));

            boolean eligible = calculateTwentyPercent(eligibilityRequest.getAmount(), cbsBalanceResponse.getDouble("availableBalance"));

            EligibilityResponse eligibilityResponse = new EligibilityResponse("000", "success",
                    accountObject.getString("accountName"), eligibilityRequest.getAccountNumber(), eligible, null);

            return eligibilityResponse;
        }


        JSONObject accountLookupResponse = getSahayAccount(eligibilityRequest.getAccountNumber());

        JSONObject accountBalanceResponse = getSahayBalance(eligibilityRequest.getAccountNumber());

        String balanceString = accountBalanceResponse.getString("accountBalance").substring(3);
        balanceString = balanceString.replace(",", ""); // Remove commas
        double balance = Double.valueOf(balanceString);
//
//        Product productById = loanService.getProductById(eligibilityRequest.getProductId());
//
//        double nonCollateralLimit = productById.getNonColateralLimit();


        // customer is eligible , if
        // ✅ 1. customer exists
        // ✅ 2. customer 20% of principal amount
        // ✅ 3. customer is onboarded and apraisalAmount equals or greater than the amount customer is applying
        // ✅ 4. customer has collateral or guarantor


        boolean eligible = false;

        boolean onBoarded = onBoarded(eligibilityRequest.getAccountNumber());

        boolean hasTwentyPercent = calculateTwentyPercent(eligibilityRequest.getAmount(), balance);

        boolean customerHasCollateral = hasCollateral(eligibilityRequest.getAccountNumber(), eligibilityRequest.getAmount());


        boolean hasActiveGuarantor = hasActiveGuarantor(eligibilityRequest.getAccountNumber());

        boolean guarantorInUse = isGuarantorInUse(eligibilityRequest.getAccountNumber());


        if (onBoarded && customerHasCollateral && hasTwentyPercent && hasActiveGuarantor && !guarantorInUse) {

            eligible = true;
        }

        String uniqueReference = "";
        String responseDescription = "";
        String response = "";


        if (eligible) {
            // otp

            String OTP = utilityService.generateOTP();
            var otpModel = new Request();

            uniqueReference = utilityService.generateReference();
            otpModel.setOtp(OTP);
            otpModel.setPrincipalAmount(eligibilityRequest.getAmount());
            otpModel.setAccountNumber(eligibilityRequest.getAccountNumber());
            otpModel.setReference(uniqueReference);
            otpModel.setCreatedAt(LocalDateTime.now());
            otpModel.setProductId(eligibilityRequest.getProductId());
            otpModel.setPeriod(eligibilityRequest.getPeriod());
            otpRepository.save(otpModel);
            // send confirmation message to client
//            String message = " " + OTP;

            LocalTime currentTime = LocalTime.now();

            String loanMessagePattern = "Dear customer, you have initiated a loan application for ETB {0} as at {1} - {2} from CommercePal. To proceed with processing it, we will withhold ETB {3}. Enter this OTP {4} to confirm.";
            String message = MessageFormat.format(loanMessagePattern, eligibilityRequest.getAmount(), LocalDate.now(), String.format("%02d:%02d", currentTime.getHour(), currentTime.getMinute()), eligibilityRequest.getAmount() * 0.2, String.valueOf(OTP));

            sendConfirmationMessage(eligibilityRequest.getAccountNumber(), message);

            responseDescription = "success";
            response = "000";

        }

        if (!onBoarded) {
            responseDescription = "Customer is not onboarded";
            response = "004";
        }
        if (!customerHasCollateral) {
            responseDescription = "Customer has no linked collateral";
            response = "004";
        }
        if (!hasActiveGuarantor) {
            responseDescription = "Customer has no linked guarantor";
            response = "004";
        }
        if (guarantorInUse) {
            responseDescription = "Guarantor is in use";
            response = "004";
        }

        if (!hasTwentyPercent) {
            responseDescription = "Customer has no twenty percent ";
            response = "004";
        }


        EligibilityResponse eligibilityResponse = new EligibilityResponse(response, responseDescription,
                accountLookupResponse.get("name").toString(), eligibilityRequest.getAccountNumber(), eligible, uniqueReference);
        return eligibilityResponse;
    }


    // calculate 20% percentage
    public boolean calculateTwentyPercent(double requestAmount, double balance) {
        return balance >= requestAmount * PERCENTAGE / 100;
    }

    public boolean onBoarded(String customerAccount) throws CustomException, ApiException {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerAccount(customerAccount);

        if (!optionalCustomer.isPresent()) {
            throw new CustomException("Customer not onboarded ,  Kindly visit nearest Rays MFI branch");
        }

        return optionalCustomer.isPresent();
    }

    // loan > 30K

    public boolean hasCollateral(String customerAccount, Double principalAmount) throws CustomException {

        Optional<Customer> byCustomerAccount = customerRepository.findByCustomerAccount(customerAccount);

        Collateral collateralByNumber = collateralService.getCollateralByCustomerId(byCustomerAccount.get().getId());

        if (collateralByNumber == null) {
            throw new CustomException("Customer has no attached collateral , Kindly visit nearest Rays MFI branch");
        }

        return collateralByNumber != null && principalAmount <= collateralByNumber.getValue() ? true : false;

    }


    public boolean hasActiveGuarantor(String customerAccount) {

        Optional<Guarantor> guarantorByCustomerAccount = guarantorService.getGuarantorByCustomerAccount(customerAccount);

        if (!guarantorByCustomerAccount.isPresent()) return false;

        return true;
    }

    public boolean isGuarantorInUse(String customerAccount) {
        Optional<Guarantor> guarantorByCustomerAccount = guarantorService.getGuarantorByCustomerAccount(customerAccount);

        Guarantor guarantorDto = guarantorByCustomerAccount.get();
        return guarantorDto.isHasLoanAttached();
    }


//    Optional<GuarantorDto> guarantorByCustomerAccount = guarantorService.getGuarantorByCustomerAccount(customerAccount);
//
//        if (!guarantorByCustomerAccount.isPresent()) {
//        return CustomResponse.builder()
//                .response("004")
//                .responseDescription("Customer has no guarantor")
//                .build();
//    }
//
//    GuarantorDto guarantorDto = guarantorByCustomerAccount.get();
//
//        if (guarantorDto.isHasLoanAttached()) {
//        return CustomResponse.builder()
//                .response("004")
//                .responseDescription("Guarantor is in use")
//                .build();
//    }

//    public boolean hasGuarantor(String customerAccount) {
//        return guarantorService.getGuarantorByCustomerAccount(customerAccount)
//                .map(guarantor -> true)
//                .orElse(false);
//    }

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
            jsonPayload.put("timestamp", Timestamp.from(Instant.now()));
            jsonPayload.put("channel", "PORTAL");

            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SMS_API)
                    .setBody(jsonPayload.toString());

            JSONObject smsResponse = asyncHttp.sendRequest(requestBuilder);
            log.info("SMS RESPONSE : {}", smsResponse);

        } catch (Exception e) {
            // Handle any exceptions that might occur during the API call
            e.printStackTrace();
        }
    }

    // get customer branch

    public CustomerBranch getCustomerBranchByCode(String branchCode) throws CustomException {
        Optional<CustomerBranch> byBranchCode = branchRepository.findByBranchCode(branchCode);

        if (!byBranchCode.isPresent()) {
            throw new CustomException("Branch code does not exits");
        }
        return byBranchCode.get();
    }

    public List<CustomerBranch> getAllBranches() {
        List<CustomerBranch> all = branchRepository.findAll();
        return all;
    }


    public Customer getCustomerByAccountNumber(String accountNumber) throws CustomException {

        Optional<Customer> byCustomerAccount = customerRepository.findByCustomerAccount(accountNumber);

        if (!byCustomerAccount.isPresent()) {
            throw new CustomException("Customer doesnt exist");
        }

        return byCustomerAccount.get();
    }


    public CustomResponse uploadCustomerDocuments(CreateCustomerDocument documentDto) {
        try {
            // Check if collateral exists
            Optional<Customer> byId = customerRepository.findById(documentDto.getCustomerId());
            if (!byId.isPresent()) {
                return CustomResponse.builder()
                        .response("004")
                        .responseDescription("Customer not found")
                        .build();
            }

            Customer customer = byId.get();
            // Save file to the file system
            String uploadDir = "E:\\Apps\\loan-docs"; // Specify your upload directory
            String fileName = documentDto.getDocumentType() + "_" + customer.getCustomerAccount();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(documentDto.getFile().getInputStream(), filePath);

            // Save file path in the database
            CustomerDocument customerDocument = CustomerDocument.builder()
                    .customerId(documentDto.getCustomerId())
                    .documentDescription(documentDto.getDocumentDescription())
                    .documentType(documentDto.getDocumentType())
                    .documentPath("/" + uploadDir + "/" + fileName + "_" + System.currentTimeMillis()) // Adjust the path to start with a slash
                    .createdBy(String.valueOf(documentDto.getCreatedBy()))
                    .createdDate(LocalDateTime.now())
                    .customerAccount(customer.getCustomerAccount())
                    .documentNumber(documentDto.getDocumentNumber())
                    .status(0)
                    .build();
            customerDocumentRepository.save(customerDocument);

            return CustomResponse.builder()
                    .response("000")
                    .responseDescription("Customer documents uploaded successfully")
                    .build();
        } catch (IOException e) {
            return CustomResponse.builder()
                    .response("999")
                    .responseDescription("Failed to upload customer documents : " + e.getMessage())
                    .build();
        }
    }


    public List<CustomerDocument> getPendingDocuments(int status) {
        List<CustomerDocument> customerDocumentByStatus = customerDocumentRepository.findCustomerDocumentByStatus(status);

        log.info("document list : {}", customerDocumentByStatus);

        return customerDocumentByStatus;


    }


}
