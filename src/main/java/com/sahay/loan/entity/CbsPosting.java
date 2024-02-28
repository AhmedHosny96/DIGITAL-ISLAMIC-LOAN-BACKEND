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
@Table(name = "CBSPosting") // AKA. loan repayment transactions record
public class CbsPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LoanAccount")
    private String loanAccount;

    @Column(name = "CustomerAccount")
    private String customerAccount;

    @Column(name = "Branch")
    private String branch;

    @Column(name = "PaidPrincipal")
    private Double paidPrincipal;

    @Column(name = "PaidMarkUp")
    private Double paidMarkUp;

    @Column(name = "UnpaidPrincipal")
    private Double unpaidPrincipal;

    @Column(name = "UnpaidMarkUp")
    private Double unpaidMarkUp;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

}
