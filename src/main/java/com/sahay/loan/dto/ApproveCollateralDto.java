package com.sahay.loan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApproveCollateralDto {

    private int status;
    private String approvedBy;
    private String verifiedComment;
}
