package com.sahay.customer;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class EligibilityRequest {

    private String accountNumber;
    private int productId;

    @Min(1)
    @Max(12)
    private int period;
    private double amount;
}
