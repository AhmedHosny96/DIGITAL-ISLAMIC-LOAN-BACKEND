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
@Table(name = "Collateral") // AKA. loan repayment transactions record

public class Collateral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CustomerId")
    private Long customerId;

    @Column(name = "CollateralType")
    private String collateralType;

    @Column(name = "CollateralNumber")
    private String collateralNumber;

    @Column(name = "CollateralDescription")
    private String collateralDescription;

    @Column(name = "Value")
    private double value;

    @Column(name = "AppraisalDate")
    private LocalDate appraisalDate;

    @Column(name = "AppraisalAuthority")
    private String appraisalAuthority;

    @Column(name = "Status")
    private int status;

    @Column(name = "ProofOfOwnership")
    private String proofOfOwnership;

    @Column(name = "IsInsured")
    private boolean isInsured;

    @Column(name = "InsuarancePolicy")
    private String insurancePolicy;

    @Column(name = "InsuaranceStatus")
    private String insuranceStatus;
    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "CreatorComment")
    private String creatorComment;

    @Column(name = "VerifiedBy")
    private String verifiedBy;

    @Column(name = "VerifierComment")
    private String verifierComment;

    @Column(name = "VerifiedDate")
    private LocalDateTime verifiedDate;

    @Column(name = "Remark")
    private String remark;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;


//   /// ,[CreatedBy]
//            ,[CreatorComment]
//  //          ,[CreatedDate]
//            ,[VerifiedBy]
//            ,[VerifierComment]
//            ,[VerifiedDate]
}
