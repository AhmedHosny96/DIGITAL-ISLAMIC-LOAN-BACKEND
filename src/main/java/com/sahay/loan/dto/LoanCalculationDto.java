package com.sahay.loan.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationDto {

    @Min(value = 1, message = "Period must be non-negative")
    @Max(value = 12, message = "Period cannot be greater than 12 ")
    private int period;
    @Min(value = 1000, message = "Amount must be acceptable amount")
    private double amount;
}
