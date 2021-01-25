package com.haulmont.testtask.ui.components.search;

import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.domain.fields.impls.ClientField;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.ui.components.layout.ClientLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class ClientSearchLayout extends SearchLayout {

    public ClientSearchLayout() {
        super();
    }

    @Override
    protected void addComboBox() {
        List<String> elements = new ArrayList<>();
        elements.add("по имени");
        elements.add("по фамилии");
        elements.add("по отчеству");
        elements.add("по телефону");
        elements.add("по email");
        elements.add("по паспорту");

        comboBox = new ComboBox<>("Искать:", elements);
        comboBox.setSelectedItem(elements.get(0));
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setTextInputAllowed(false);

        addComponent(comboBox);
        setComponentAlignment(comboBox, Alignment.BOTTOM_RIGHT);
    }

    @Override
    protected Field<ClientField> getFieldForSearch() {
        ClientField result = null;
        String value = (String) comboBox.getSelectedItem().get();
        switch (value) {
            case "по имени":
                result = ClientField.FIRSTNAME;
                break;
            case "по фамилии":
                result = ClientField.LASTNAME;
                break;
            case "по отчеству":
                result = ClientField.PATRONYMIC;
                break;
            case "по телефону":
                result = ClientField.PHONE;
                break;
            case "по email":
                result = ClientField.EMAIL;
                break;
            case "по паспорту":
                result = ClientField.PASSPORT;
                break;
        }
        return result;
    }

    @Override
    protected void setButtonClickListener() {
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ClientLayout layout = (ClientLayout) getParent().getParent();
                String input = field.getValue();
                List<Client> searchResult;
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
