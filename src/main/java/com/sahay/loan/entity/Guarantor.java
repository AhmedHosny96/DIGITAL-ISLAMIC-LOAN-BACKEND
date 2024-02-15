package com.sahay.loan.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Guarantor")
@Builder
public class Guarantor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CustomerAccount")
    private String customerAccount;

    @Column(name = "GuarantorAccount")
    private String guarantorAccount;

    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "MiddleName")
    private String middleName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Address")
    private String address;

    @Column(name = "City")
    private String city;

    @Column(name = "Relationship")
    private String relationship;

    @Column(name = "Occupation")
    private String occupation;

    @Column(name = "IsEmployed")
    private boolean isEmployed;

    @Column(name = "EmployementDetails")
    private String employmentDetails;

    @Column(name = "Salary")
    private double salary;

    @Column(name = "CreditHistory")
    private String creditHistory;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "Remark")
    private String remark;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "CreatedDate")
    private LocalDate createdDate;

    @Column(name = "VerifiedBy")
    private Integer verifiedBy;


    @Column(name = "VerifiedDate")
    private LocalDateTime verifiedDate;

}
