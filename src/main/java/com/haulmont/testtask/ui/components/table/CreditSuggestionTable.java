package com.haulmont.testtask.ui.components.table;

import com.haulmont.testtask.domain.CreditSuggestion;

import java.util.List;

public class CreditSuggestionTable extends AbstractTable<CreditSuggestion> {

    public CreditSuggestionTable(List<CreditSuggestion> suggestions) {
        super(suggestions);
    }

    @Override
    protected void initColumns() {
        addColumn(CreditSuggestion::getClientFullName).setCaption("Клиент");
        addColumn(CreditSuggestion::getAmount).setCaption("Размер займа");
        addColumn(CreditSuggestion::getCreditPercents).setCaption("Ставка по кредиту (%)");
    }
}
