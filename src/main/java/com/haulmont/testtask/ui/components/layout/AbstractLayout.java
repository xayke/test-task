package com.haulmont.testtask.ui.components.layout;

import com.haulmont.testtask.service.AbstractService;
import com.haulmont.testtask.service.BankService;
import com.haulmont.testtask.ui.components.ActionButtonLayout;
import com.haulmont.testtask.ui.components.ActionLayout;
import com.haulmont.testtask.ui.components.table.AbstractTable;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public abstract class AbstractLayout<T> extends VerticalLayout {
    protected AbstractTable<T> table;

    protected BankService bankService;
    protected AbstractService<T> service;
    protected T selected;

    public AbstractLayout() {
        bankService = BankService.getInstance();
        addActionLayout();
        disableButtons();
    }

    protected abstract void addActionLayout();

    protected abstract void addGrid();

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    public AbstractService<T> getService() {
        return service;
    }

    public BankService getBankService() {
        return bankService;
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    public void updateTable(List<T> data) {
        table.update(data);
        selected = null;
        disableButtons();
    }

    protected void enableButtons() {
        ActionLayout actionLayout = (ActionLayout) getComponent(0);
        ActionButtonLayout buttonLayout = (ActionButtonLayout) actionLayout.getComponent(1);
        Button editButton = (Button) buttonLayout.getComponent(1);
        Button deleteButton = (Button) buttonLayout.getComponent(2);
        editButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    protected void disableButtons() {
        ActionLayout actionLayout = (ActionLayout) getComponent(0);
        ActionButtonLayout buttonLayout = (ActionButtonLayout) actionLayout.getComponent(1);
        Button editButton = (Button) buttonLayout.getComponent(1);
        Button deleteButton = (Button) buttonLayout.getComponent(2);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
