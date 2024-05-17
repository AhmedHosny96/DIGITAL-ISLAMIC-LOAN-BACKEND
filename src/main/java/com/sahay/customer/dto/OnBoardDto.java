package com.sahay.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnBoardDto {

    private String accountNumber;
    private String branchCode;
    private String comment;
    private String createdBy;
    private Double appraisedAmount;
}
