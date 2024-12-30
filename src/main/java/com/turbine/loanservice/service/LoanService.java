package com.turbine.loanservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turbine.loanservice.client.ThirdPartyLoanClientFactory;
import com.turbine.loanservice.model.Loan;
import com.turbine.loanservice.client.ThirdPartyLoanClient;

@Service
public class LoanService {

    private final ThirdPartyLoanClientFactory loanClientFactory;

    @Autowired
    public LoanService(ThirdPartyLoanClientFactory loanClientFactory) {
        this.loanClientFactory = loanClientFactory;
    }

    public String processLoan(Loan loanRequest) throws Exception {
        ThirdPartyLoanClient client = loanClientFactory.getClient(loanRequest.getLoanMethod());
        return client.createLoan(loanRequest);
    }
}
