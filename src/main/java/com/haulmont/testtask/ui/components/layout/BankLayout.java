package com.haulmont.testtask.ui.components.layout;

import com.haulmont.testtask.domain.Bank;
import com.haulmont.testtask.service.BankService;
import com.haulmont.testtask.service.ServiceException;
import com.haulmont.testtask.ui.components.ActionLayout;
import com.haulmont.testtask.ui.components.table.BankTable;
import com.vaadin.ui.Notification;

import java.util.List;

public class BankLayout extends AbstractLayout<Bank> {

    public BankLayout() {
        super();
        service = BankService.getInstance();
        addGrid();
    }

    @Override
    protected void addActionLayout() {
        addComponent(new ActionLayout(Bank.class));
    }

    @Override
    protected void addGrid() {
        try {
            List<Bank> banks = service.getAll();
            table = new BankTable(banks);
            table.setHeight(800, Unit.PIXELS);
            table.addSelectionListener((e) -> {
                selected = table.asSingleSelect().getValue();
                enableButtons();
            });

            addComponent(table);

        } catch (ServiceException e) {
            Notification.show(e.getMessage());
        }
    }
}
