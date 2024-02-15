package com.sahay.customer;

import lombok.Data;

@Data
public class EligibilityRequest {

    private String accountNumber;
    private int productId;
    private double amount;
}
