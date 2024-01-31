package com.sahay.account;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/account-eligibility", produces = "application/json")
    public ResponseEntity<?> getAccountEligibility(@RequestBody AccountRequest accountRequest) throws Exception {

        AccountResponse accountEligibility = accountService.checkAccountEligibility(accountRequest);

        return new ResponseEntity<>(accountEligibility, HttpStatus.OK);

    }
}
