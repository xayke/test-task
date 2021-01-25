package com.haulmont.testtask.domain;

import java.util.List;
import java.util.UUID;

public class CreditSuggestion {
    private UUID id;
    private Client client;
    private Credit credit;
    private String amount;
    private List<Payment> paymentPlan;

    public CreditSuggestion() {
    }

    public CreditSuggestion(UUID id, Client client, Credit credit, String amount, List<Payment> paymentPlan) {
        this.id = id;
        this.client = client;
        this.credit = credit;
        this.amount = amount;
        this.paymentPlan = paymentPlan;
    }

    public CreditSuggestion(Client client, Credit credit, String amount, List<Payment> paymentPlan) {
        id = UUID.randomUUID();
        this.client = client;
        this.credit = credit;
        this.amount = amount;
        this.paymentPlan = paymentPlan;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getClientFullName() {
        return String.format("%s %s %s",
                client.getLastname(), client.getFirstname(),
                (client.getPatronymic() != null) ? client.getPatronymic() : "");
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public String getCreditPercents() {
        return credit.getPercent();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Payment> getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(List<Payment> paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditSuggestion that = (CreditSuggestion) o;
        return id.equals(that.id) && client.equals(that.client) && credit.equals(that.credit)
                && amount.equals(that.amount) && paymentPlan.equals(that.paymentPlan);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "\n\t'id': '" + id + '\'' +
                ",\n\t'client': " + client +
                ",\n\t'credit': " + credit +
                ",\n\t'amount': '" + amount + '\'' +
                ",\n\t'payment_plan': " + paymentPlan +
                "\n}";
    }
}
