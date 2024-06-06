package com.sahay.loan.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanConfirmationDto {

    private String otp;
    private String reference;


}
