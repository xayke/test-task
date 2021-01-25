package com.haulmont.testtask.ui.components.table;

import com.haulmont.testtask.domain.Bank;

import java.util.List;

public class BankTable extends AbstractTable<Bank> {
    public BankTable(List<Bank> data) {
        super(data);
    }

    @Override
    protected void initColumns() {
        addColumn(Bank::getName).setCaption("Название");
        addColumn(bank -> bank.getClients().size()).setCaption("Количество клиентов");
        addColumn(bank -> bank.getCredits().size()).setCaption("Количествово видов кредитов");
    }
}
