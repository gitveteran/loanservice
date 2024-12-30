package com.turbine.loanservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ThirdPartyLoanClientFactory {

    private final Map<String, ThirdPartyLoanClient> clientMap;

    @Autowired
    public ThirdPartyLoanClientFactory(Map<String, ThirdPartyLoanClient> clientMap) {
        this.clientMap = clientMap;
    }

    public ThirdPartyLoanClient getClient(String loanMethod) {
        ThirdPartyLoanClient client = clientMap.get(loanMethod.toLowerCase());
        if (client == null) {
            throw new IllegalArgumentException("Unsupported loan method: " + loanMethod);
        }
        return client;
    }
}
