package com.sahay.customer;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EligibilityResponse {
    private String response;
    private String responseDescription;
    private String accountName;
    private String accountNumber;
    private boolean isEligible;
    private String reference;
}
