package com.turbine.loanservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.turbine.loanservice.model.Loan;
import com.turbine.loanservice.repository.LoansRepository;
import com.turbine.loanservice.service.LoanService;

class LoansControllerTest {

    private LoansController loansController;

    @Mock
    private LoansRepository loansRepository;

    @Mock
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loansController = new LoansController(loansRepository, loanService);
    }

    @Test
    void testCreateLoan_Success() throws Exception {
        Loan loanRequest = new Loan();
        loanRequest.setLoanMethod("stripe");
        loanRequest.setLoanType("individual");
        loanRequest.setBorrowerName("John Doe");
        loanRequest.setTaxId("123-45-6789");
        loanRequest.setAddress("123 Main St");
        loanRequest.setLoanAmount(250000.0);

        String thirdPartyLoanId = "external-123";
        Loan savedLoan = new Loan();
        savedLoan.setId(1L);
        savedLoan.setLoanMethod("stripe");
        loanRequest.setLoanType("individual");
        savedLoan.setBorrowerName("John Doe");
        savedLoan.setTaxId("123-45-6789");
        savedLoan.setAddress("123 Main St");
        savedLoan.setLoanAmount(250000.0);
        savedLoan.setExternalLoanId(thirdPartyLoanId);

        when(loanService.processLoan(loanRequest)).thenReturn(thirdPartyLoanId);
        when(loansRepository.save(loanRequest)).thenReturn(savedLoan);

        ResponseEntity<?> response = loansController.createLoan(loanRequest);

        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200");
        assertEquals(savedLoan, response.getBody(), "Response body should contain the saved loan");

        verify(loanService, times(1)).processLoan(loanRequest);
        verify(loansRepository, times(1)).save(loanRequest);
    }

    @Test
    void testCreateLoan_Error() throws Exception {
        Loan loanRequest = new Loan();
        loanRequest.setLoanMethod("stripe");
        loanRequest.setLoanType("individual");
        loanRequest.setBorrowerName("John Doe");
        loanRequest.setTaxId("123-45-6789");
        loanRequest.setAddress("123 Main St");
        loanRequest.setLoanAmount(250000.0);

        when(loanService.processLoan(loanRequest)).thenThrow(new RuntimeException("Processing failed"));

        ResponseEntity<?> response = loansController.createLoan(loanRequest);

        assertEquals(500, response.getStatusCodeValue(), "Expected HTTP status 500");
        assertTrue(response.getBody().toString().contains("Processing failed"), "Response body should contain error message");

        verify(loanService, times(1)).processLoan(loanRequest);
        verify(loansRepository, never()).save(any(Loan.class));
    }
}
