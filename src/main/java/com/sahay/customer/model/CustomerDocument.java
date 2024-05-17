package com.sahay.customer.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity(name = "CustomerDocuments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "CustomerId")
    private Integer customerId;

    @Column(name = "CustomerAccount")
    private String customerAccount;

    @Column(name = "DocumentType")
    private String documentType;

    @Column(name = "DocumentDescription")
    private String documentDescription;

    @Column(name = "DocumentNumber")
    private String documentNumber;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "DocumentPath")
    private String documentPath;

    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "CreatorComment")
    private String creatorComment;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @Column(name = "VerifiedBy")
    private String verifiedBy;

    @Column(name = "VerifierComment")
    private String verifierComment;

    @Column(name = "VerifiedDate")
    private LocalDateTime verifiedDate;
}