package com.sahay.loan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "[LoanAccount]")

public class LoanEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String loanAccountNumber;
    private double principalAmount; // the exact amount
    private double markUpAmount; // principal/2% * productPeriod
    private double totalAmount;
    private double paidAmount;
    private double unPaidAmount;
    private LocalDateTime eventDate;
    private String transactionType;
    private LocalDateTime transactionDate;

}
