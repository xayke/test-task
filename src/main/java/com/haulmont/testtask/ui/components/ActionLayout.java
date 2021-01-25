package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.ui.components.search.*;
import com.haulmont.testtask.ui.MainLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

public class ActionLayout extends HorizontalLayout {

    private ActionButtonLayout actionButtonLayout;

    public ActionLayout(Class<?> cls) {
        addBackButton();
        addActionButtonLayout(cls);
        addSearchLayout(cls);
        setWidth(100, Unit.PERCENTAGE);
    }

    private void addBackButton() {
        Button backBtn = new Button("Назад");
        backBtn.addClickListener((e) -> getUI().setContent(new MainLayout()));
        addComponent(backBtn);
        setComponentAlignment(backBtn, Alignment.BOTTOM_LEFT);
    }

    private void addSearchLayout(Class<?> cls) {
        SearchLayout search = null;
        switch (cls.getSimpleName()) {
            case "Bank":
                search = new BankSearchLayout();
                break;
            case "Client":
                search = new ClientSearchLayout();
                break;
            case "Credit":
                search = new CreditSearchLayout();
                break;
            case "CreditSuggestion":
                search = new CreditSuggestionSearchLayout();
                break;
        }
        addComponent(search);
        setComponentAlignment(search, Alignment.BOTTOM_CENTER);
    }

    private void addActionButtonLayout(Class<?> cls) {
        actionButtonLayout = new ActionButtonLayout(cls);
        addComponent(actionButtonLayout);
        setComponentAlignment(actionButtonLayout, Alignment.BOTTOM_RIGHT);
    }

    public void enableButtons() {
        Button editButton = (Button) actionButtonLayout.getComponent(1);
        Button deleteButton = (Button) actionButtonLayout.getComponent(2);
        editButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    public void disableButtons() {
        Button editButton = (Button) actionButtonLayout.getComponent(1);
        Button deleteButton = (Button) actionButtonLayout.getComponent(2);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

}
