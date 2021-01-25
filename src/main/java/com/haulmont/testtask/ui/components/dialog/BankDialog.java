package com.haulmont.testtask.ui.components.dialog;


import com.haulmont.testtask.domain.Bank;
import com.haulmont.testtask.service.AbstractService;
import com.haulmont.testtask.ui.components.layout.BankLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.util.UUID;

public class BankDialog extends AbstractDialog {

    private TextField nameField;
    private Bank bank;

    public BankDialog(DialogMode mode) {
        super(mode);
        removeComponent(getComponent(0));
    }

    public BankDialog(Bank bank, DialogMode mode) {
        this(mode);
        this.bank = bank;
        initFields();
    }


    @Override
    protected void addFields() {
        nameField = addTextField("Наименование");
    }

    @Override
    protected void initFields() {
        nameField.setValue(bank.getName());
    }

    @Override
    protected void setConfirmOnClickListener() {
        confirm.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                createBank();
                if (bank.getName() == null) {
                    Notification.show("Введите корректное наименование банка");
                    return;
                }
                switch (mode) {
                    case SAVE:
                        getService().save(bank, null);
                        break;
                    case EDIT:
                        getService().update(bank, null);
                        break;
                }
                Window dialog = (Window) getParent();
                getLayout().updateTable(getLayout().getService().getAll());
                getLayout().setSelected(null);
                dialog.close();
            }
        });
    }

    private void createBank() {
        if (bank == null) {
            bank = new Bank();
            bank.setId(UUID.randomUUID());
        }
        if (!nameField.getValue().isEmpty() && nameField.getValue().trim().matches("^[A-ZА-Я]{1}[A-ZА-Яa-zа-я ()-]{1,99}$")) {
            bank.setName(nameField.getValue().trim());
            System.out.println("name changed");
        }
    }

    private BankLayout getLayout() {
        return (BankLayout) getUI().getContent();
    }

    private AbstractService<Bank> getService() {
        return getLayout().getService();
    }
}
