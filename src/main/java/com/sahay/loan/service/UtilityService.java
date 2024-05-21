package com.sahay.loan.service;

import com.sahay.config.AsyncHttpConfig;
import com.sahay.loan.repo.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.RequestBuilder;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilityService {

    private final String SMS_API = "http://172.16.3.25:8013/channel/request";


    private final String SAHAY_API = "http://172.16.1.17:8013/channel/request";

    private final RestTemplate restTemplate;

    private final OtpRepository otpRepository;

    private final AsyncHttpConfig asyncHttp;

    // send sms

    public void sendConfirmationMessage(String phoneNumber, String message) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put("username", "channel");
            jsonPayload.put("password", "$_@C0NNEKT");
            jsonPayload.put("messageType", "1200");
            jsonPayload.put("serviceCode", "290");
            jsonPayload.put("transactionType", "NOT");
            jsonPayload.put("transactionId", "Digital-loan-" + UUID.randomUUID());
            jsonPayload.put("institution", "");
            jsonPayload.put("businessCode", "");
            jsonPayload.put("customerName", "");
            jsonPayload.put("alertType", "GEN");
            jsonPayload.put("user", "");
            jsonPayload.put("accessPassword", "");
            jsonPayload.put("toMsisdn", phoneNumber);
            jsonPayload.put("message", message);
            jsonPayload.put("timestamp", Timestamp.from(Instant.now()));
            jsonPayload.put("channel", "PORTAL");

            RequestBuilder requestBuilder = new RequestBuilder("POST")
                    .setUrl(SMS_API)
                    .setBody(jsonPayload.toString());

            JSONObject smsResponse = asyncHttp.sendRequest(requestBuilder);
            log.info("SMS RESPONSE : {}", smsResponse);

        } catch (Exception e) {
            // Handle any exceptions that might occur during the API call
            e.printStackTrace();
        }
    }


    // hold the amount


    public String generateReference() {
        // Prefix for the unique reference
        String prefix = "SCDL";
        // Generate a random 6-digit number
        int randomNumber = new Random().nextInt(900000) + 100000;
        // Combine the prefix and the random number to form the unique reference
        String uniqueReference = prefix + randomNumber;
        return uniqueReference;
    }


    public String getParkReferenceByReference(String reference) {
        return otpRepository.findParkReferenceByReference(reference).get();
    }

    public String generateOTP() {
        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        int length = 6;

        StringBuilder otp = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());

            otp.append(characters.charAt(index));
        }

        return otp.toString();
    }


}
