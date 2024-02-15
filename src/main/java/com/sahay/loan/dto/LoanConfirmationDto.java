package com.sahay.loan.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanConfirmationDto {

    private int otp;
    private String reference;
//    private double depositAmount; // 20%


}
