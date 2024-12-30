package com.turbine.loanservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

import com.turbine.loanservice.model.Loan;
import com.turbine.loanservice.repository.LoansRepository;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class LoansControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate thirdpartyLoanApiClient;

    @Autowired
    private LoansRepository loansRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void testCreateLoan() throws JsonProcessingException {
        // Prepare the loan request payload
        Loan loanRequest = new Loan();
        loanRequest.setLoanAmount(15000.00);
        loanRequest.setLoanType("entity");
        loanRequest.setLoanMethod("stripe");
        loanRequest.setBorrowerName("John Doe");
        loanRequest.setTaxId("123456789");
        loanRequest.setAddress("123 Main St");

        String baseUrl = getBaseUrl();
        ResponseEntity<String> response = thirdpartyLoanApiClient.postForEntity(baseUrl + "/loans", loanRequest, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Loan createdLoan = objectMapper.readValue(response.getBody(), Loan.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(createdLoan);
        assertNotNull(createdLoan.getId());

        // Assert database persistence
        Optional<Loan> loanFromDb = loansRepository.findById(createdLoan.getId());
        assertTrue(loanFromDb.isPresent());
        assertEquals(loanRequest.getLoanAmount(), loanFromDb.get().getLoanAmount());
    }

    @Test
    public void testCreateLoanFailedWithUnsupportedLoanMethod() {
        Loan loanRequest = new Loan();
        loanRequest.setLoanAmount(15000.00);
        loanRequest.setLoanMethod("paypal");
        loanRequest.setLoanType("individual");
        loanRequest.setBorrowerName("John Doe");
        loanRequest.setTaxId("123456789");
        loanRequest.setAddress("123 Main St");

        String baseUrl = getBaseUrl();

        try {
            ResponseEntity<String> response = thirdpartyLoanApiClient.postForEntity(baseUrl + "/loans", loanRequest, String.class);
        } catch (HttpServerErrorException ex) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
        }
    }
}
