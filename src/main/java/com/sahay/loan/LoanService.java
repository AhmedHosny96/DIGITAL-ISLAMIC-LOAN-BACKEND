package com.sahay.loan;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

//    dbo.GetProductSetupAsJSON()

    private final OtpRepository otpRepository;

    private final JdbcTemplate jdbcTemplate;

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
    public JSONObject confirmLoanApplication(LoanConfirmationRequest confirmationRequest) {
        var customResponse = new JSONObject();
        return otpRepository.findByAccountNumberAndOtp(
                confirmationRequest.getAccountNumber(), confirmationRequest.getOtp())
                .map(otpEntity -> {
                    customResponse.put("response", "000");
                    customResponse.put("responseDescription", "Loan application confirmed successfully.");
                    otpRepository.delete(otpEntity);
                    return customResponse;
                })
                .orElseGet(() -> {
                    customResponse.put("response", "001"); // Assuming "001" represents an error code
                    customResponse.put("responseDescription", "Confirmation failed");
                    return customResponse;
                });
    }
}
