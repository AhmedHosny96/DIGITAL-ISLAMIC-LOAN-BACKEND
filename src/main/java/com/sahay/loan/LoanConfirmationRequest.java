package com.sahay.loan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanConfirmationRequest {

    private String accountNumber;
    private int otp;

}
