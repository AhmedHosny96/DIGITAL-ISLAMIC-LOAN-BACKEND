package com.sahay.customer.model;

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
@Table(name = "Customers")
public class Customer {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "CustomerAccount")
    private String customerAccount;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "MiddleName")
    private String middleName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "BranchCode")
    private String BranchCode;

    @Column(name = "AggregateCredits")
    private double aggregateCredits;

    @Column(name = "AggregateDebits")
    private double aggregateDebits;

    @Column(name = "ScoreLevel")
    private String scoreLevel;

    @Column(name = "AppraisedAmount")
    private double appraisedAmount;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "RegisteredStation")
    private String registeredStation;

    @Column(name = "RegisteredDate")
    private LocalDate registeredDate;

    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "CreatedDate")
    private LocalDate createdDate;

    @Column(name = "CreatorComment")
    private String creatorComment;

    @Column(name = "VerifiedBy")
    private String verifiedBy;

    @Column(name = "VerifiedDate")
    private LocalDateTime verifiedDate;

    @Column(name = "VerifiedComment")
    private String verifiedComment;
}
