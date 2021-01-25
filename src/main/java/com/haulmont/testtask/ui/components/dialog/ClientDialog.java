package com.haulmont.testtask.ui.components.dialog;

import com.haulmont.testtask.domain.Bank;
import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.service.AbstractService;
import com.haulmont.testtask.ui.components.layout.ClientLayout;
import com.haulmont.testtask.validator.ValidatorException;
import com.haulmont.testtask.validator.impls.ClientValidator;
import com.vaadin.ui.*;

import java.util.List;
import java.util.UUID;

public class ClientDialog extends AbstractDialog {

    private TextField firstnameField;
    private TextField lastnameField;
    private TextField patronymicField;
    private TextField phoneField;
    private TextField emailField;
    private TextField passportField;

    private Client client;
    private ClientValidator validator;

    public ClientDialog(DialogMode mode) {
        super(mode);
        validator = ClientValidator.getInstance();
    }

    public ClientDialog(Client client, DialogMode mode) {
        this(mode);
        this.client = client;
        initFields();
    }

    @Override
    protected void setConfirmOnClickListener() {
        confirm.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    Bank selectedBank;
                    if (bankComboBox.getSelectedItem().isPresent()) {
                        selectedBank = bankComboBox.getSelectedItem().get();
                    } else {
                        Notification.show("Выберите банк");
                        return;
                    }
                    createClient();
                    if (validator.isValid(client)) {
                        switch (mode) {
                            case SAVE:
                                getService().save(client, selectedBank.getId());
                                break;
                            case EDIT:
                                getService().update(client, selectedBank.getId());
                                break;
                        }
                    }
                    Window dialog = (Window) getParent();
                    getLayout().updateTable(getLayout().getService().getAll());
                    getLayout().setSelected(null);
                    dialog.close();
                } catch (ValidatorException e) {
                    Notification.show(e.getMessage());
                }
            }
        });
    }

    public void initBankComboBox() {
        List<Bank> banks = getLayout().getBankService().getAll();
        bankComboBox.setItems(banks);

        if (mode == DialogMode.EDIT) {
            bankComboBox.setSelectedItem(getLayout().getBankService().getBankByClient(client));
        }
    }

    @Override
    protected void addFields() {
        lastnameField = addTextField("Фамилия");
        firstnameField = addTextField("Имя");
        patronymicField = addTextField("Отчество");
        passportField = addTextField("Паспорт");
        phoneField = addTextField("Телефон");
        emailField = addTextField("Email");
    }

    @Override
    protected void initFields() {
        lastnameField.setValue(client.getLastname());
        firstnameField.setValue(client.getFirstname());
        patronymicField.setValue((client.getPatronymic() != null) ? client.getPatronymic() : "");
        passportField.setValue(client.getPassport());
        phoneField.setValue(client.getPhone());
        emailField.setValue((client.getEmail() != null) ? client.getEmail() : "");
    }

    private void createClient() {
        if (client == null) {
            client = new Client();
            client.setId(UUID.randomUUID());
        }
        client.setFirstname((!firstnameField.isEmpty()) ? firstnameField.getValue().trim() : "");
        client.setLastname((!lastnameField.isEmpty()) ? lastnameField.getValue().trim() : "");
        client.setPatronymic((!patronymicField.isEmpty()) ? patronymicField.getValue().trim() : null);
        client.setPhone((!phoneField.isEmpty()) ? phoneField.getValue().trim() : "");
        client.setPassport((!passportField.isEmpty()) ? passportField.getValue().trim() : "");
        client.setEmail((!emailField.isEmpty()) ? emailField.getValue().trim() : null);
    }

    private ClientLayout getLayout() {
        return (ClientLayout) getUI().getContent();
    }

    private AbstractService<Client> getService() {
        return getLayout().getService();
    }
}
