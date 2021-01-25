package com.haulmont.testtask.ui.components.search;

import com.haulmont.testtask.domain.Bank;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.domain.fields.impls.BankField;
import com.haulmont.testtask.ui.components.layout.BankLayout;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class BankSearchLayout extends SearchLayout {

    public BankSearchLayout() {
        super();
    }

    @Override
    protected void addComboBox() {
        List<String> elements = new ArrayList<>();
        elements.add("по наименованию");

        comboBox = new ComboBox<>("Искать:", elements);
        comboBox.setWidth(240, Unit.PIXELS);
        comboBox.setSelectedItem("по наименованию");
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setTextInputAllowed(false);

        addComponent(comboBox);
        setComponentAlignment(comboBox, Alignment.BOTTOM_RIGHT);
    }

    @Override
    protected Field getFieldForSearch() {
        BankField result = null;
        String value = comboBox.getSelectedItem().get();
        switch (value) {
            case "по наименованию":
                result = BankField.NAME;
                break;
        }
        return result;
    }

    @Override
    protected void setButtonClickListener() {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                BankLayout layout = (BankLayout) getParent().getParent();
                String input = field.getValue().trim();
                List<Bank> searchResult;
                if (!input.trim().isEmpty()) {
                    searchResult = layout.getService()
                            .searchByField(layout.getService().getAll(), input, getFieldForSearch());
                } else {
                    searchResult = layout.getService().getAll();
                }
                layout.updateTable(searchResult);
            }
        });
        button.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }
}
