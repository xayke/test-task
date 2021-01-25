package com.haulmont.testtask.ui;

import com.haulmont.testtask.ui.components.layout.BankLayout;
import com.haulmont.testtask.ui.components.layout.ClientLayout;
import com.haulmont.testtask.ui.components.layout.CreditLayout;
import com.haulmont.testtask.ui.components.layout.CreditSuggestionLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class MainLayout extends VerticalLayout {

    public MainLayout() {
        setMargin(true);

        addHeader();
        addButton(new Button("Банки"), (e) -> getUI().setContent(new BankLayout()));
        addButton(new Button("Клиенты"), (e) -> getUI().setContent(new ClientLayout()));
        addButton(new Button("Кредиты"), (e) -> getUI().setContent(new CreditLayout()));
        addButton(new Button("Кредитные предложения"), (e) -> getUI().setContent(new CreditSuggestionLayout()));
    }

    private void addHeader() {
        Label header = new Label("Добро пожаловать в банк:");
        addComponent(header);
        setComponentAlignment(header, Alignment.MIDDLE_CENTER);
    }

    private void addButton(Button button, Button.ClickListener listener) {
        button.addClickListener(listener);
        button.setWidth(220, Unit.PIXELS);
        addComponent(button);
        setComponentAlignment(button, Alignment.MIDDLE_CENTER);
    }
}
