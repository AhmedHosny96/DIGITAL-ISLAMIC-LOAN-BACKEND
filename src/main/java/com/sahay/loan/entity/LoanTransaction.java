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
@Table(name = "LoanTransaction") // AKA. loan repayment transactions record
public class LoanTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LoanAccountNumber")
    private String loanAccountNumber;

    @Column(name = "TransactionType")
    private String transactionType;

    @Column(name = "AccountFrom")
    private String accountFrom;

    @Column(name = "Amount")
    private Double amount;

    @Column(name = "TransactionReference")
    private String transactionReference;

    @Column(name = "Narration")
    private String narration;

    @Column(name = "ESBReference")
    private String esbReference;

    @Column(name = "ESBResponse")
    private String esbResponse;

    @Column(name = "CBSResponse")
    private String cbsResponse;

    @Column(name = "CBSReference")
    private String cbsReference;

    @Column(name = "ReversalFlag")
    private Boolean reversalFlag;

    @Column(name = "TransactionDate")
    private LocalDateTime transactionDate;
}
