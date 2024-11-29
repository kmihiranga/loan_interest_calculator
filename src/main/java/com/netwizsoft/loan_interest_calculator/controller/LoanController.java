package com.netwizsoft.loan_interest_calculator.controller;

import com.netwizsoft.loan_interest_calculator.domain.LoanDetailsRequest;
import com.netwizsoft.loan_interest_calculator.service.LoanInterestCalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan")
@AllArgsConstructor
public class LoanController {

    private final LoanInterestCalculatorService loanInterestCalculatorService;

    @PostMapping("/calculate")
    public void calculateLoanDetails(@RequestBody LoanDetailsRequest loanDetailsRequest) {

        this.loanInterestCalculatorService.calculateLoanInterestYearly(loanDetailsRequest);
    }
}
