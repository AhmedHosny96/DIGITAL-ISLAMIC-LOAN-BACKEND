package com.sahay.customer.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateCustomerDocument {

    private String documentType;
    private String documentNumber;
    private String documentDescription;
    private MultipartFile file;
    private Integer createdBy;
    private String creatorComment;
    private Integer customerId;

}
