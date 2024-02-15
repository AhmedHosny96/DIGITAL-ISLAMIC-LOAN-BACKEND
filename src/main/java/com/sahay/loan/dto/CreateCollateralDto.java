package com.sahay.loan.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class CreateCollateralDto {
    private Long customerId;
    private String collateralType;
    private String collateralNumber;
    private String collateralDescription;
    private double value;
    private LocalDate appraisalDate;
    private String appraisalAuthority;
    private String proofOfOwnership;
    private boolean isInsured;
    private String insurancePolicy;
    private String insuranceStatus;
    private String remark;
    private String createdBy;
//    private int status;

}
