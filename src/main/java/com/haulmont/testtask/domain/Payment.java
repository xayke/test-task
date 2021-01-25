package com.haulmont.testtask.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Payment {
    private UUID id;
    private Date date;
    private String fullAmount;
    private String principalAmount;
    private String interestAmount;
    private UUID suggestionId;

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy");

    public Payment() {
    }

    public Payment(Date date, String fullAmount, String principalAmount, String interestAmount,
                   UUID suggestionId) {
        id = UUID.randomUUID();
        this.date = date;
        this.fullAmount = fullAmount;
        this.principalAmount = principalAmount;
        this.interestAmount = interestAmount;
        this.suggestionId = suggestionId;
    }

    public Payment(UUID id, Date date, String fullAmount,
                   String principalAmount, String interestAmount, UUID suggestionId) {
        this.id = id;
        this.date = date;
        this.fullAmount = fullAmount;
        this.principalAmount = principalAmount;
        this.interestAmount = interestAmount;
        this.suggestionId = suggestionId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedStringDate() {
        return dateFormatter.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) throws ParseException {
        this.date = dateFormatter.parse(date);
    }

    public String getFullAmount() {
        return fullAmount;
    }

    public void setFullAmount(String fullAmount) {
        this.fullAmount = fullAmount;
    }

    public String getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }

    public UUID getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(UUID suggestionId) {
        this.suggestionId = suggestionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id.equals(payment.id) && date.equals(payment.date) && fullAmount.equals(payment.fullAmount)
                && principalAmount.equals(payment.principalAmount) && interestAmount.equals(payment.interestAmount);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "\n\t'id': '" + id + '\'' +
                ",\n\t'date': '" + dateFormatter.format(date) + '\'' +
                ",\n\t'full_amount': '" + fullAmount + '\'' +
                ",\n\t'principal_amount': ''" + principalAmount + '\'' +
                ",\n\t'interest_amount': '" + interestAmount + '\'' +
                ",\n\t'credit_suggestion_id': '" + suggestionId + '\'' +
                "\n}";
    }
}
