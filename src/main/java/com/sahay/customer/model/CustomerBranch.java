package com.sahay.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Branch")
public class CustomerBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "BranchName")

    private String branchName;
    @Column(name = "BranchCode")

    private String branchCode;
    @Column(name = "Status")

    private int status;
    @Column(name = "Prefix")
    private String prefix;

    @Column(name = "CreatedDate")

    private LocalDateTime createdDate;
}
