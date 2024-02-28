package com.sahay.loan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CollateralImages")
@Builder
public class CollateralImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CollateralId")
    private Integer collateralId;

    @Column(name = "Document")
    private String document;

    @Column(name = "Description")
    private String description;


}
