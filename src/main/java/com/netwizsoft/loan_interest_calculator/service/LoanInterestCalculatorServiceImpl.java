package com.netwizsoft.loan_interest_calculator.service;

import com.netwizsoft.loan_interest_calculator.domain.LoanDetailsRequest;
import com.netwizsoft.loan_interest_calculator.utils.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoanInterestCalculatorServiceImpl implements LoanInterestCalculatorService {

    @Value("${loan.report.path}")
    private String reportPath;

    @Override
    public void calculateLoanInterestYearly(LoanDetailsRequest loanDetailsRequest) {

        double finalPrinciple = loanDetailsRequest.getLoanAmount();

        // check date list is correctly listed
        boolean strictlyIncreasing = isStrictlyIncreasing(loanDetailsRequest.getPaymentDates());

        if (!strictlyIncreasing) {
            throw new RuntimeException("Not strictly increasing provided dates.");
        }

        // delete file if exists
        this.deleteFile(reportPath);

        // calculate monthly interest
        for (int i = 1; i < loanDetailsRequest.getPaymentDates().size(); i++) {
            long dateDifference = dateDifference(loanDetailsRequest.getPaymentDates().get(i - 1),
                    loanDetailsRequest.getPaymentDates().get(i));

            // first calculate the total interest
            double dailyInterest = calculateDailyInterest(loanDetailsRequest.getLoanInterestPercentage());
            double totalInterestValue = calculateTotalInterest(dailyInterest, dateDifference, finalPrinciple);

            // reduce the total interest from monthly premium
            // get the rest value after reduce the total interest
            double principleReduction = loanDetailsRequest.getMonthlyPremium() - totalInterestValue;

            String content = "=====================================\n" + "date difference between two dates " + dateDifference + "\n" + "Total interest value " + totalInterestValue + "\n"
                    + "Reduced value without interest " + principleReduction + "\n" + "Loan interest for the start date " + DateUtils.formatDateToHumanReadableString(loanDetailsRequest.getPaymentDates().get(i - 1)) + " " + "from end date "
                    + DateUtils.formatDateToHumanReadableString(loanDetailsRequest.getPaymentDates().get(i)) + " is " + calculateTotalInterest(dailyInterest, dateDifference, finalPrinciple) + "\n"
                    + "=====================================" + "\n\n";

            this.saveToFile(content);

            // reduce the rest value from the loan amount
            finalPrinciple -= principleReduction;
        }
    }

    /**
     * save content to a file
     *
     * @param content
     */
    private void saveToFile(String content) {

        try {

            // creating an instance of FileWrite class
            FileWriter f = new FileWriter("loan-details.txt", true);

            // using write method to write the content into the file
            f.write(content);

            // closing a file
            f.close();

            log.info("Successfully write to the file");
        }
        catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * delete file if exists
     *
     * @param fileName
     */
    private void deleteFile(String fileName) {

        try {

            // delete if file exists
            boolean deleteFile = Files.deleteIfExists(Paths.get(reportPath));

            if (deleteFile) {
                log.info("File deleted successfully before generate report");
            } else {
                log.error("File not found for specified location.");
            }
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
    }

    private double calculateDailyInterest(double loanInterestPercentage) {

        // get the loan interest
        double loanInterest = loanInterestPercentage / 100.0;

        // calculate daily interest
        return loanInterest / 365.0;
    }

    private long dateDifference(Date startDate, Date endDate) {

        // get the difference in milliseconds
        long diffInMillis = endDate.getTime() - startDate.getTime();
        // convert milliseconds to days
        return TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }

    private double calculateTotalInterest(double dailyInterest, double dateDifference, double principal) {

        return Math.round(dateDifference * dailyInterest * principal);
    }

    public boolean isStrictlyIncreasing(List<Date> dates) {

        for (int i = 1; i < dates.size(); i++) {
            if (dates.get(i).compareTo(dates.get(i - 1)) <= 0) {
                log.info("Current date {} is not greater than the previous date {}", dates.get(i), dates.get(i - 1));
                return false;
            }
        }
        return true; // All dates are strictly increasing
    }
}
