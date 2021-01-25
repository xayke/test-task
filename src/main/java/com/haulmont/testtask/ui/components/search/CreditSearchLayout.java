package com.haulmont.testtask.ui.components.search;

import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.domain.fields.impls.CreditField;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.ui.components.layout.CreditLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class CreditSearchLayout extends SearchLayout {

    public CreditSearchLayout() {
        super();
        field.setWidth(280, Unit.PIXELS);
    }

    @Override
    protected void addComboBox() {
        List<String> elements = new ArrayList<>();
        elements.add("по наименованию");
        elements.add("по минимальной ставке");
        elements.add("по максимальной ставке");
        elements.add("по проценту");

        comboBox = new ComboBox<>("Искать:", elements);
        comboBox.setWidth(240, Unit.PIXELS);
        comboBox.setItems(elements);
        comboBox.setSelectedItem(elements.get(0));
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setTextInputAllowed(false);

        addComponent(comboBox);
        setComponentAlignment(comboBox, Alignment.BOTTOM_RIGHT);
    }

    @Override
    protected Field<CreditField> getFieldForSearch() {
        CreditField result = null;
        String value = (String) comboBox.getSelectedItem().get();
        switch (value) {
            case "по наименованию":
                result = CreditField.NAME;
                break;
            case "по минимальной ставке":
                result = CreditField.MIN_LIMIT;
                break;
            case "по максимальной ставке":
                result = CreditField.MAX_LIMIT;
                break;
            case "по проценту":
                result = CreditField.PERCENT;
                break;
        }
        return result;
    }

    @Override
    protected void setButtonClickListener() {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                CreditLayout layout = (CreditLayout) getParent().getParent();
                String input = field.getValue();
                List<Credit> searchResult;
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
