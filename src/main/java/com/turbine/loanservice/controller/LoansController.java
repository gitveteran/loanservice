package com.turbine.loanservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turbine.loanservice.model.Loan;
import com.turbine.loanservice.repository.LoansRepository;
import com.turbine.loanservice.service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoansController {

    private final LoansRepository loansRepository;
    private final LoanService loanService;

    @Autowired
    public LoansController(LoansRepository loansRepository, LoanService loanService) {
        this.loansRepository = loansRepository;
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody Loan loanRequest) {
        try {
            String thirdPartyLoanId = loanService.processLoan(loanRequest);
            loanRequest.setExternalLoanId(thirdPartyLoanId);
            Loan savedLoan = loansRepository.save(loanRequest);
            return ResponseEntity.ok(savedLoan);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}