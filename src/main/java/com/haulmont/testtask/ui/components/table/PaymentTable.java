package com.haulmont.testtask.ui.components.table;

import com.haulmont.testtask.domain.Payment;

import java.util.List;

public class PaymentTable extends AbstractTable<Payment> {
    public PaymentTable(List<Payment> payments) {
        super(payments);
    }

    @Override
    protected void initColumns() {
        addColumn(Payment::getFormattedStringDate).setCaption("Дата платежа");
        addColumn(Payment::getPrincipalAmount).setCaption("Размер гашения тела платежа");
        addColumn(Payment::getInterestAmount).setCaption("Размер гашения процентов");
        addColumn(Payment::getFullAmount).setCaption("Размер платежа");
    }
}
