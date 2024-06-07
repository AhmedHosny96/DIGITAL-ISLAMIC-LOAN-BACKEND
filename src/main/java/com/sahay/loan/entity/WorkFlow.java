package com.sahay.loan.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "WorkFlow")
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlow {

    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Stage")
    private String stage;

    @Column(name = "Description")
    private String description;

    @Column(name = "CreatedAt")
    private LocalDate createdAt;
}
