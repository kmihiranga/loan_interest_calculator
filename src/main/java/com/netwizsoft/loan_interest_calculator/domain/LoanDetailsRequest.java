package com.netwizsoft.loan_interest_calculator.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class LoanDetailsRequest {

    private Double loanInterestPercentage;

    private Double loanAmount;

    /** Yearly or Monthly loan **/
    private String loanInterestTerm;

    private Integer loanDuration;

    /** the amount we plan to pay for a month if available*/
    private Double monthlyPremium;

    /** if single date, then it gets as a loan date that pays every month **/
    private List<Date> paymentDates;
}
