package com.haulmont.testtask.ui.components.dialog;

import com.haulmont.testtask.domain.Bank;
import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.service.AbstractService;
import com.haulmont.testtask.ui.components.layout.CreditLayout;
import com.haulmont.testtask.validator.ValidatorException;
import com.haulmont.testtask.validator.impls.CreditValidator;
import com.vaadin.ui.*;

import java.util.List;
import java.util.UUID;

public class CreditDialog extends AbstractDialog {
    private TextField nameField;
    private TextField minLimitField;
    private TextField maxLimitField;
    private TextField percentField;

    private Credit credit;
    private CreditValidator validator;

    public CreditDialog(DialogMode mode) {
        super(mode);
        validator = CreditValidator.getInstance();
    }

    public CreditDialog(Credit credit, DialogMode mode) {
        this(mode);
        this.credit = credit;
        initFields();
    }

    @Override
    protected void addFields() {
        nameField = addTextField("Наименование");
        minLimitField = addTextField("Минимальная ставка");
        maxLimitField = addTextField("Максимальная ставка");
        percentField = addTextField("Процент кредитования");
    }

    @Override
    protected void initFields() {
        nameField.setValue(credit.getName());
        minLimitField.setValue(credit.getMinLimit());
        maxLimitField.setValue(credit.getMaxLimit());
        percentField.setValue(credit.getPercent());
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
                    createCredit();
                    if (validator.isValid(credit)) {
                        switch (mode) {
                            case SAVE:
                                getService().save(credit, selectedBank.getId());
                                break;
                            case EDIT:
                                getService().update(credit, selectedBank.getId());
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
            bankComboBox.setSelectedItem(getLayout().getBankService().getBankByCredit(credit));
        }
    }

    private void createCredit() {
        if (credit == null) {
            credit = new Credit();
            credit.setId(UUID.randomUUID());
        }
        credit.setName((!nameField.isEmpty()) ? nameField.getValue().trim() : "");
        credit.setMinLimit((!minLimitField.isEmpty()) ? minLimitField.getValue().trim() : "");
        credit.setMaxLimit((!maxLimitField.isEmpty()) ? maxLimitField.getValue().trim() : "");
        credit.setPercent((!percentField.isEmpty()) ? percentField.getValue().trim() : "");
    }

    private CreditLayout getLayout() {
        return (CreditLayout) getUI().getContent();
    }

    private AbstractService<Credit> getService() {
        return getLayout().getService();
    }
}
