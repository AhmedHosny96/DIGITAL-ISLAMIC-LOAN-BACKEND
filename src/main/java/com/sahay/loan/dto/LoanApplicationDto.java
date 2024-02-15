package com.sahay.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationDto {


    private String customerName;
    private String reference; // can be park
    private int productId;
    private int period;
    private LocalDate createDate;

}


//    "username": "channel",
//            "password": "$_@C0NNEKT",
//            "messageType": "1200",
//            "serviceCode": "3002",
//            "transactionId": "FRA89EK9C47A20",
//            "parkTransactionId": "FRA89EK9C474",
//            "msisdn": "251945421279",
//            "accountNumber": "251945421279",
//            "amount":"10000",
//            "period":"2",
//            "productId":"1",
//            "accountName":"Abdi Kamal",
//            "createDate":"2024-01-30",
//            "transactionType": "DLP",
//            "timestamp": "20200101120000",
//            "channel": "LMS"
//            }