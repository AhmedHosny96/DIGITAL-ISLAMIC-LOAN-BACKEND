package com.sahay.loan.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class GuarantorDto {

    private String customerAccount;
    private String guarantorAccount;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String city;
    private String relationship;
    private String occupation;
    private boolean isEmployed;
    private String employmentDetails;
    private double salary;
    private String incomeDetails;
    private String financialObligation;
    private double incomeAmount;
    private String creditHistory;
    private String remark;
    private int createdBy;
    private LocalDate createdDate;
    private int verifiedBy;
    private LocalDateTime verifiedDate;
    private boolean hasLoanAttached;
    private String loanAccountNumber;
}
