package com.haulmont.testtask.calculator;

import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.domain.Payment;
import com.haulmont.testtask.service.PaymentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

public class CreditSuggestionCalculator {
    private static CreditSuggestionCalculator INSTANCE;

    private HashMap<Integer, Integer> monthToDays;

    private PaymentService service;

    private CreditSuggestionCalculator() {
        monthToDays = fillMonthToDayHashMap();
        service = PaymentService.getInstance();
    }

    public static CreditSuggestionCalculator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditSuggestionCalculator();
        }
        return INSTANCE;
    }

    public List<Payment> calculatePaymentPlan(String amountValue, String termValue,
                                              Credit credit, UUID suggestion_id) throws CalculatorException {
        if ((amountValue == null) || (termValue == null) || (credit == null))
            throw new CalculatorException("Один из пунктов не заполнен");
        List<Payment> paymentPlan = null;
        try {
            int term = Integer.parseInt(termValue);
            BigDecimal amount = new BigDecimal(amountValue);
            BigDecimal percents = new BigDecimal(credit.getPercent());
            paymentPlan = new ArrayList<>();

            BigDecimal rateOfInterest = percents
                    .divide(new BigDecimal("100"), 7, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("12"), 7, RoundingMode.HALF_UP);
            BigDecimal annuity = calculateAnnuity(rateOfInterest, term);
            BigDecimal monthlyFullAmount = amount.multiply(annuity).setScale(0, RoundingMode.HALF_UP);
            for (int month = 0; month < term; month++) {
                Payment payment = new Payment();
                payment.setId(UUID.randomUUID());
                payment.setSuggestionId(suggestion_id);
                if (month == 0) {
                    payment.setDate(new Date());
                } else {
                    String[] lastDateSplit = paymentPlan.get(month - 1).getFormattedStringDate().split("-");
                    payment.setDate(lastDateSplit[0] + "-" + (Integer.parseInt(lastDateSplit[1]) + 1) + "-" + lastDateSplit[2]);
                }
                payment.setFullAmount(monthlyFullAmount.toString());
                BigDecimal interestAmount = calculateInterestAmount(payment, amount, percents);
                payment.setInterestAmount(interestAmount.toString());
                payment.setPrincipalAmount(monthlyFullAmount.subtract(interestAmount).setScale(2, RoundingMode.HALF_UP).toString());
                amount = amount.subtract(monthlyFullAmount).multiply(BigDecimal.ONE.add(rateOfInterest));
                paymentPlan.add(payment);
            }
        } catch (NumberFormatException e) {
            throw new CalculatorException("В полях \"Срок кредитования\" и \"Размер займа\" должны быть числа");
        } catch (ParseException e) {
            throw new CalculatorException("Ошибка при парсинге дат");
        }
        return paymentPlan;
    }

    public List<Payment> recalculatePaymentPlan(List<Payment> paymentPlan, String amountValue, String termValue,
                                                Credit credit, UUID suggestion_id) throws CalculatorException {
        if ((amountValue == null) || (termValue == null) || (credit == null))
            throw new CalculatorException("Один из пунктов не заполнен");
        try {
            int paymentPlanSize = paymentPlan.size();
            int term = Integer.parseInt(termValue);
            BigDecimal amount = new BigDecimal(amountValue);
            BigDecimal percents = new BigDecimal(credit.getPercent());
            paymentPlan = new ArrayList<>();

            BigDecimal rateOfInterest = percents
                    .divide(new BigDecimal("100"), 7, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("12"), 7, RoundingMode.HALF_UP);
            BigDecimal annuity = calculateAnnuity(rateOfInterest, term);
            BigDecimal monthlyFullAmount = amount.multiply(annuity).setScale(0, RoundingMode.HALF_UP);
            for (int month = 0; month < term; month++) {
                Payment payment;
                if (month >= paymentPlan.size()) {
                    payment = new Payment();
                    payment.setId(UUID.randomUUID());
                } else {
                    payment = paymentPlan.get(month);
                }
                payment.setSuggestionId(suggestion_id);
                if (month == 0) {
                    payment.setDate(new Date());
                } else {
                    String[] lastDateSplit = paymentPlan.get(month - 1).getFormattedStringDate().split("-");
                    payment.setDate(lastDateSplit[0] + "-" + (Integer.parseInt(lastDateSplit[1]) + 1) + "-" + lastDateSplit[2]);
                }
                payment.setFullAmount(monthlyFullAmount.toString());
                BigDecimal interestAmount = calculateInterestAmount(payment, amount, percents);
                payment.setInterestAmount(interestAmount.toString());
                payment.setPrincipalAmount(monthlyFullAmount.subtract(interestAmount).setScale(2, RoundingMode.HALF_UP).toString());
                amount = amount.subtract(monthlyFullAmount).multiply(BigDecimal.ONE.add(rateOfInterest));
                if (month >= paymentPlan.size()) {
                    paymentPlan.add(payment);
                } else {
                    paymentPlan.set(month, payment);
                }
            }
            while (term < paymentPlan.size()) {
                paymentPlan.remove(paymentPlan.size() - 1);
            }
        } catch (NumberFormatException e) {
            throw new CalculatorException("В полях \"Срок кредитования\" и \"Размер займа\" должны быть числа");
        } catch (ParseException e) {
            throw new CalculatorException("Ошибка при парсинге дат");
        }
        return paymentPlan;
    }

    public String calculateMonthlyPayment(String amountValue, String termValue, String percentValue) {
        if ((amountValue == null) || (termValue == null) || (percentValue == null))
            return BigDecimal.ZERO.toString();
        BigDecimal monthlyFullAmount = null;
        try {
            int term = Integer.parseInt(termValue);
            BigDecimal amount = new BigDecimal(amountValue);
            BigDecimal percents = new BigDecimal(percentValue);

            BigDecimal rateOfInterest = percents
                    .divide(new BigDecimal("100"), 7, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("12"), 7, RoundingMode.HALF_UP);
            BigDecimal annuity = calculateAnnuity(rateOfInterest, term);
            monthlyFullAmount = amount.multiply(annuity).setScale(0, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new CalculatorException("В полях \"Срок кредитования\" и \"Размер займа\" должны быть числа");
        }
        return monthlyFullAmount.toString();
    }

    public String calculateSummaryInterestAmount(String amountValue, String termValue, String monthlyPaymentValue) {
        if ((amountValue == null) || (termValue == null) || (monthlyPaymentValue == null))
            return BigDecimal.ZERO.toString();
        BigDecimal summaryInterestAmount = null;
        try {
            int term = Integer.parseInt(termValue);
            BigDecimal amount = new BigDecimal(amountValue);
            BigDecimal monthlyPayment = new BigDecimal(monthlyPaymentValue);

            summaryInterestAmount = monthlyPayment.multiply(new BigDecimal(term)).subtract(amount);
        } catch (NumberFormatException e) {
            throw new CalculatorException("В полях \"Срок кредитования\" и \"Размер займа\" должны быть числа");
        }
        return summaryInterestAmount.toString();
    }

    public String calculateSummaryFullAmount(String amountValue, String interestAmountValue) {
        if ((amountValue == null) || (interestAmountValue == null))
            return BigDecimal.ZERO.toString();
        BigDecimal summaryFullAmount = null;
        try {
            BigDecimal amount = new BigDecimal(amountValue);
            BigDecimal interestAmount = new BigDecimal(interestAmountValue);

            summaryFullAmount = amount.add(interestAmount);
        } catch (NumberFormatException e) {
            throw new CalculatorException("В полях \"Срок кредитования\" и \"Размер займа\" должны быть числа");
        }
        return summaryFullAmount.toString();
    }

    private BigDecimal calculateAnnuity(BigDecimal rateOfInterest, int term) {
        BigDecimal somePart = BigDecimal.ONE.add(rateOfInterest).pow(term);
        BigDecimal result = rateOfInterest.multiply(somePart).divide(somePart.subtract(BigDecimal.ONE), RoundingMode.HALF_UP);
        return result;
    }

    private BigDecimal calculateInterestAmount(Payment payment, BigDecimal amount, BigDecimal percents) {
        return amount.multiply(percents.divide(new BigDecimal("100"), 7, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(monthToDays.get(payment.getDate().getMonth())))
                .divide(((payment.getDate().getYear() % 4) == 0) ?
                        BigDecimal.valueOf(366) : BigDecimal.valueOf(365), 2, RoundingMode.HALF_UP);
    }

    private HashMap<Integer, Integer> fillMonthToDayHashMap() {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 31);
        map.put(1, 28);
        map.put(2, 31);
        map.put(3, 30);
        map.put(4, 31);
        map.put(5, 30);
        map.put(6, 31);
        map.put(7, 31);
        map.put(8, 30);
        map.put(9, 31);
        map.put(10, 30);
        map.put(11, 31);
        return map;
    }


}
