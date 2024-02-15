package com.sahay.loan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApproveGuarantorDto {

    private Long guarantorId;
    private int status;
    private int verifiedBy;
}
