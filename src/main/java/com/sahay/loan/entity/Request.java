package com.sahay.loan.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;
    @Column(name = "Otp")
    private int otp;
    @Column(name = "AccountNumber")
    private String accountNumber;
    @Column(name = "PrincipleAmount")
    private double principalAmount;
    @Column(name = "DepositAmount")
    private double depositAmount; // 20%
    @Column(name = "Reference")
    private String reference;
    @Column(name = "Status")
    private int status = 0;
    @Column(name = "ParkReference")
    private String parkReference;
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

}
