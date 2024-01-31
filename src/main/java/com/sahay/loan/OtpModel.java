package com.sahay.loan;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Otp")
public class OtpModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int otp;
    private String accountNumber;
    private LocalDateTime createdAt;
}
