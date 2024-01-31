package com.sahay.account;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponse {
    private String response;
    private String responseDescription;
    private String accountName;
    private String accountNumber;
    private boolean isEligible;
}
