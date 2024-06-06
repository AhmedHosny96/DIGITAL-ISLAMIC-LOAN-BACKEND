package com.sahay.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomResponse extends JSONObject {

    private String response;
    private String responseDescription;
}
