package com.sahay.loan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String productName;
    private int productRate;
    private String productPeriod;
    private int productDeposit;
    private String productDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING) // Prevent scientific notation
    private Double productBudget;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Double utilizedAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Double runningBalance;
    private double nonColateralLimit;
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date createdDate;

    // getters and setters
}
