package com.sahay.loan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ProductSetup")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ProductName")
    private String productName;
    @Column(name = "ProductRate")
    private int productRate;
    @Column(name = "ProductPeriod")
    private String productPeriod;
    @Column(name = "ProductDeposit")
    private int productDeposit;
    @Column(name = "ProductDescription")
    private String productDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING) // Prevent scientific notation
    @Column(name = "ProductBudget")
    private Double productBudget;

    @Column(name = "UtilizedAmount")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Double utilizedAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "RunningBalance")
    private Double runningBalance;
    @Column(name = "NonColaretalLimit")
    private double nonColateralLimit;
    @Column(name = "CreatedBy")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "CreatedDate")
    private Date createdDate;


    // getters and setters
}
