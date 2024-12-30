package com.turbine.loanservice.client;

import com.turbine.loanservice.model.Loan;

public interface ThirdPartyLoanClient {
    String createLoan(Loan loanRequest) throws Exception;
}
