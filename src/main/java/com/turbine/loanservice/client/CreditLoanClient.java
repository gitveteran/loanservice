package com.turbine.loanservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;

import com.turbine.loanservice.model.Loan;

@Component("credit")
public class CreditLoanClient implements ThirdPartyLoanClient {

    private final RestTemplate thirdpartyLoanApiClient;

    @Value("${loanservice.thirdparty.credit.url}")
    private String loanUrl;

    @Autowired
    public CreditLoanClient(RestTemplate thirdpartyLoanApiClient) {
        this.thirdpartyLoanApiClient = thirdpartyLoanApiClient;
    }

    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public String createLoan(Loan loanRequest) {
        ResponseEntity<String> response = thirdpartyLoanApiClient.postForEntity(loanUrl, loanRequest, String.class);
        return response.getBody();
    }

    @Recover
    public String fallbackCreateLoan(Loan loanRequest, Exception ex) throws Exception {
        throw new RuntimeException("Fallback Loan ID due to: Credit Service unavailable");
    }
}
