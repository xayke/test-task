package com.haulmont.testtask.ui.components.search;

import com.haulmont.testtask.domain.CreditSuggestion;
import com.haulmont.testtask.domain.fields.impls.CreditSuggestionField;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.ui.components.layout.CreditSuggestionLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class CreditSuggestionSearchLayout extends SearchLayout {

    public CreditSuggestionSearchLayout() {
        super();
        field.setWidth(280, Unit.PIXELS);
    }

    @Override
    protected void addComboBox() {
        List<String> elements = new ArrayList<>();
        elements.add("по ФИО клиента");
        elements.add("по размеру займа");
        elements.add("по процентной ставке");

        comboBox = new ComboBox<>("Искать:", elements);
        comboBox.setWidth(240, Unit.PIXELS);
        comboBox.setSelectedItem(elements.get(0));
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setTextInputAllowed(false);

        addComponent(comboBox);
        setComponentAlignment(comboBox, Alignment.BOTTOM_RIGHT);
    }

    @Override
    protected Field<CreditSuggestionField> getFieldForSearch() {
        CreditSuggestionField result = null;
        String value = (String) comboBox.getSelectedItem().get();
        switch (value) {
            case "по ФИО клиента":
                result = CreditSuggestionField.CLIENT;
                break;
            case "по размеру займа":
                result = CreditSuggestionField.AMOUNT;
                break;
            case "по процентной ставке":
                result = CreditSuggestionField.CREDIT;
                break;
        }
        return result;
    }

    @Override
    protected void setButtonClickListener() {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                CreditSuggestionLayout layout = (CreditSuggestionLayout) getParent().getParent();
                String input = field.getValue();
                List<CreditSuggestion> searchResult;

                if (!input.trim().isEmpty()) {
                    searchResult = layout.getService()
                            .searchByField(layout.getService().getAll(), input, getFieldForSearch());
                } else {
                    searchResult = layout.getService().getAll();
                }
                layout.updateTable(searchResult);
            }
        });
    }
}
