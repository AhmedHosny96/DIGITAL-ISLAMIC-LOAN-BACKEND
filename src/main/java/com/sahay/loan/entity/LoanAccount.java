package com.sahay.loan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[LoanAccount]")

public class LoanAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int productId;
    private String accountName;
    private String loanAccountNumber;
    private int productPeriod;
    private double principalAmount; // the exact amount
    private double markUpAmount; // principal/2% * productPeriod
    private double totalLoanAmount; // principal + markup
    private double outstandingBalance; // full amount
    private double dueBalance; // to be paid in this month
    private LocalDateTime createdAt;
    private int createdBy;
    private LocalDateTime approvedDate;
    private int approvedBy;
    private LocalDate closingDate;
    private String status;


}
