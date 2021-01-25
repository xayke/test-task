package com.haulmont.testtask.ui.components.layout;

import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.service.CreditService;
import com.haulmont.testtask.service.ServiceException;
import com.haulmont.testtask.ui.components.ActionLayout;
import com.haulmont.testtask.ui.components.table.CreditTable;
import com.vaadin.ui.Notification;

import java.util.List;

public class CreditLayout extends AbstractLayout<Credit> {

    public CreditLayout() {
        super();
        service = CreditService.getInstance();
        addGrid();
    }

    public void addActionLayout() {
        addComponent(new ActionLayout(Credit.class));
    }

    protected void addGrid() {
        try {
            List<Credit> credits = service.getAll();
            table = new CreditTable(credits);
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
