package com.sahay.loan.service;

import com.sahay.loan.repo.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilityService {

    private final String SAHAY_API = "http://172.16.1.17:8013/channel/request";

    private final RestTemplate restTemplate;

    private final OtpRepository otpRepository;


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
