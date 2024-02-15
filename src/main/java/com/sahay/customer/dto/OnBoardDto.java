package com.sahay.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnBoardDto {

    private String accountNumber;
    private String comment;
    private int createdBy;
}
