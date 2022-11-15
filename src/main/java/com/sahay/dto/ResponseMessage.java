package com.sahay.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    public String status;
    public String message;
    public String accountNumber;
    public String customerNumber;
}
