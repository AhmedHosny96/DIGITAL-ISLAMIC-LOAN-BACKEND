package com.sahay.account;

import lombok.Data;

@Data
public class AccountRequest {

    private String accountNumber;
    private int productId;
    private double amount;
}
