package com.haulmont.testtask.ui.components.layout;

import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.service.ClientService;
import com.haulmont.testtask.service.ServiceException;
import com.haulmont.testtask.ui.components.ActionLayout;
import com.haulmont.testtask.ui.components.table.ClientTable;
import com.vaadin.ui.Notification;

import java.util.List;

public class ClientLayout extends AbstractLayout<Client> {

    public ClientLayout() {
        super();
        service = ClientService.getInstance();
        addGrid();
    }

    @Override
    public void addActionLayout() {
        addComponent(new ActionLayout(Client.class));
    }

    @Override
    protected void addGrid() {
        try {
            List<Client> clients = service.getAll();
            table = new ClientTable(clients);
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
