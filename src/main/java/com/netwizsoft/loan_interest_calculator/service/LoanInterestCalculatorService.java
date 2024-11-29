package com.netwizsoft.loan_interest_calculator.service;

import com.netwizsoft.loan_interest_calculator.domain.LoanDetailsRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface LoanInterestCalculatorService {

    /**
     * calculate loan rate yearly
     *
     * @param loanDetailsRequest
     */
    void calculateLoanInterestYearly(LoanDetailsRequest loanDetailsRequest);
}
