package com.haulmont.testtask.ui.components.table;

import com.haulmont.testtask.domain.Credit;

import java.util.List;

public class CreditTable extends AbstractTable<Credit> {

    public CreditTable(List<Credit> credits) {
        super(credits);
    }

    @Override
    protected void initColumns() {
        addColumn(Credit::getName).setCaption("Наименование");
        addColumn(Credit::getMinLimit).setCaption("Минимальная ставка");
        addColumn(Credit::getMaxLimit).setCaption("Максимальная ставка");
        addColumn(Credit::getPercent).setCaption("Процент кредитования");
    }
}
