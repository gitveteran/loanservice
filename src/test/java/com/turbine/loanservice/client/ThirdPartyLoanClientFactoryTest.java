package com.turbine.loanservice.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ThirdPartyLoanClientFactoryTest {

    private ThirdPartyLoanClientFactory factory;

    @Mock
    private ThirdPartyLoanClient mockClient1;

    @Mock
    private ThirdPartyLoanClient mockClient2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the client map
        Map<String, ThirdPartyLoanClient> clientMap = new HashMap<>();
        clientMap.put("stripe", mockClient1);
        clientMap.put("credit", mockClient2);

        factory = new ThirdPartyLoanClientFactory(clientMap);
    }

    @Test
    void testGetClient_Success() {
        ThirdPartyLoanClient result = factory.getClient("stripe");
        assertNotNull(result, "Client should not be null");
        assertEquals(mockClient1, result, "Returned client should match the expected client");

        result = factory.getClient("credit");
        assertNotNull(result, "Client should not be null");
        assertEquals(mockClient2, result, "Returned client should match the expected client");
    }

    @Test
    void testGetClient_UnsupportedLoanMethod() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> factory.getClient("paypal"));
        assertEquals("Unsupported loan method: paypal", exception.getMessage(), "Exception message should match expected");
    }

    @Test
    void testGetClient_EmptyLoanMethod() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> factory.getClient(""));
        assertEquals("Unsupported loan method: ", exception.getMessage(), "Exception message should match expected");
    }
}
