package com.haulmont.testtask.ui.components.table;

import com.haulmont.testtask.domain.Client;

import java.util.List;

public class ClientTable extends AbstractTable<Client> {

    public ClientTable(List<Client> clients) {
        super(clients);
    }

    @Override
    protected void initColumns() {
        addColumn(Client::getLastname).setCaption("Фамилия");
        addColumn(Client::getFirstname).setCaption("Имя");
        addColumn(client -> (client.getPatronymic() != null) ? client.getPatronymic() : "").setCaption("Отчество");
        addColumn(Client::getPhone).setCaption("Телефон");
        addColumn(client -> (client.getEmail() != null) ? client.getEmail() : "").setCaption("Email");
        addColumn(Client::getPassport).setCaption("Паспорт");
    }
}
