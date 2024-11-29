# Loan Interest Calculator

### sample request object

```angular2html
    {
        "loanInterestPercentage": 25.0,
        "loanAmount": 500000.0,
        "loanInterestTerm": "MONTHLY",
        "loanDuration": 18,
        "monthlyPremium": 30000,
        "paymentDates": ["2023-03-28", "2023-04-24", "2023-05-30", "2023-06-30", "2023-07-27"]
    }
```

You need to pass this kind of object to calculate each month interest.

`paymentDates` are the dates where you submit the loan monthly payment dates.