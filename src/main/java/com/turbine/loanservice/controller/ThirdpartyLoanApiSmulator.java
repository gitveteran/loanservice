package com.turbine.loanservice.controller;

import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/thirdparty/api")
@RestController
public class ThirdpartyLoanApiSmulator {

    @PostMapping(path = "/credit", consumes = "application/json", produces = "application/json")
    public String createCreditLoan(@RequestBody Map<String, Object> loan) throws Exception {
        return UUID.randomUUID().toString();
    }

    @PostMapping(path = "/stripe", consumes = "application/json", produces = "application/json")
    public String createStripeLoan(@RequestBody Map<String, Object> loan) throws Exception {
        return UUID.randomUUID().toString();
    }
}
