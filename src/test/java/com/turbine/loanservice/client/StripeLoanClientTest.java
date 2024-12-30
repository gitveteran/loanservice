package com.turbine.loanservice.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.turbine.loanservice.model.Loan;

@SpringBootTest
class StripeLoanClientTest {

    @MockBean
    private RestTemplate mockRestTemplate; // Mocked RestTemplate bean

    @Autowired
    private StripeLoanClient stripeLoanClient; // Spring-managed CreditLoanClient bean

    @Test
    void testCreateLoan_Success() {
        // Mock the RestTemplate response
        ResponseEntity<String> mockResponse = ResponseEntity.ok("LoanId123");
        when(mockRestTemplate.postForEntity(anyString(), any(Loan.class), eq(String.class)))
                .thenReturn(mockResponse);

        // Create loan request
        Loan loanRequest = new Loan();
        loanRequest.setLoanMethod("credit");
        loanRequest.setLoanAmount(1000.0);
        loanRequest.setBorrowerName("John Doe");

        // Call the method
        String response = stripeLoanClient.createLoan(loanRequest);

        // Verify the result and interaction with the mock
        assertEquals("LoanId123", response);
        verify(mockRestTemplate, times(1)).postForEntity(anyString(), any(Loan.class), eq(String.class));
    }

    @Test
    void testCreateLoan_RetryOnFailure() {
        // Simulate two failures followed by success
        when(mockRestTemplate.postForEntity(anyString(), any(Loan.class), eq(String.class)))
                .thenThrow(new RuntimeException("Temporary failure"))
                .thenThrow(new RuntimeException("Temporary failure"))
                .thenReturn(ResponseEntity.ok("LoanId123"));

        // Create loan request
        Loan loanRequest = new Loan();
        loanRequest.setLoanMethod("credit");
        loanRequest.setLoanAmount(1000.0);

        // Call the method
        String response = stripeLoanClient.createLoan(loanRequest);

        // Verify retries and final success
        assertEquals("LoanId123", response);
        verify(mockRestTemplate, times(3)).postForEntity(anyString(), any(Loan.class), eq(String.class));
    }

    @Test
    void testCreateLoan_Fallback() {
        // Simulate persistent failure
        when(mockRestTemplate.postForEntity(anyString(), any(Loan.class), eq(String.class)))
                .thenThrow(new RuntimeException("Service unavailable"))
                .thenThrow(new RuntimeException("Service unavailable"))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Create loan request
        Loan loanRequest = new Loan();
        loanRequest.setLoanMethod("credit");
        loanRequest.setLoanAmount(1000.0);

        // Call the method and verify fallback
        Exception exception = assertThrows(Exception.class, () -> {
            stripeLoanClient.createLoan(loanRequest);
        });

        // Verify the exception message
        assertEquals("Fallback Loan ID due to: Stripe Service unavailable", exception.getMessage());

        // Verify retries before fallback
        verify(mockRestTemplate, times(3)).postForEntity(anyString(), any(Loan.class), eq(String.class));
    }
}
