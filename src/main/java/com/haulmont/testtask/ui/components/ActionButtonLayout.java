package com.haulmont.testtask.ui.components;

import com.haulmont.testtask.domain.*;
import com.haulmont.testtask.service.ServiceException;
import com.haulmont.testtask.ui.components.dialog.*;
import com.haulmont.testtask.ui.components.layout.BankLayout;
import com.haulmont.testtask.ui.components.layout.ClientLayout;
import com.haulmont.testtask.ui.components.layout.CreditLayout;
import com.haulmont.testtask.ui.components.layout.CreditSuggestionLayout;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class ActionButtonLayout extends HorizontalLayout {
    protected Button addButton;
    private Button editButton;
    protected Button deleteButton;

    public ActionButtonLayout(Class<?> cls) {
        addButtons();
        setAddButtonClickListeners(cls);
    }

    private void addButtons() {
        addButton = new Button("Добавить");
        editButton = new Button("Редактировать");
        deleteButton = new Button("Удалить");

        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        editButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

        addComponents(addButton, editButton, deleteButton);
        setComponentAlignment(addButton, Alignment.BOTTOM_RIGHT);
        setComponentAlignment(editButton, Alignment.BOTTOM_RIGHT);
        setComponentAlignment(deleteButton, Alignment.BOTTOM_RIGHT);
    }

    private void setAddButtonClickListeners(Class<?> cls) {
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                openDialog(cls, DialogMode.SAVE);
            }
        });
        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                openDialog(cls, DialogMode.EDIT);
            }
        });
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    switch (cls.getSimpleName()) {
                        case "Bank":
                            BankLayout bLayout = (BankLayout) getUI().getContent();
                            Bank selectedB = bLayout.getSelected();
                            if (selectedB != null) {
                                bLayout.getService().delete(selectedB);
                                bLayout.updateTable(bLayout.getService().getAll());
                            }
                        case "Client":
                            ClientLayout layoutCl = (ClientLayout) getUI().getContent();
                            Client selectedCl = layoutCl.getSelected();
                            if (selectedCl != null) {
                                layoutCl.getService().delete(selectedCl);
                                layoutCl.updateTable(layoutCl.getService().getAll());
                            }
                            break;
                        case "Credit":
                            CreditLayout layoutCr = (CreditLayout) getUI().getContent();
                            Credit selectedCr = layoutCr.getSelected();
                            if (selectedCr != null) {
                                layoutCr.getService().delete(selectedCr);
                                layoutCr.updateTable(layoutCr.getService().getAll());
                            }
                            break;
                        case "CreditSuggestion":
                            CreditSuggestionLayout crSuggLayout = (CreditSuggestionLayout) getUI().getContent();
                            CreditSuggestion selectedCrSugg = crSuggLayout.getSelected();
                            if (selectedCrSugg != null) {
                                for (Payment payment : selectedCrSugg.getPaymentPlan()) {
                                    crSuggLayout.getPaymentService().delete(payment);
                                }
                                crSuggLayout.getService().delete(selectedCrSugg);
                                crSuggLayout.updateTable(crSuggLayout.getService().getAll());
                            }
                            break;
                    }
                } catch (ServiceException e) {
                    Notification.show("Невозможно удалить запись, т.к. она связана с другими таблицами." +
                            "\nСперва удалите зависимые элементы из них");
                }
            }
        });
    }

    private void openDialog(Class<?> cls, DialogMode mode) {
        Window dialog = new Window();
        dialog.center();
        dialog.setHeight(400, Unit.PIXELS);
        dialog.setWidth(380, Unit.PIXELS);
        dialog.setModal(true);
        switch (cls.getSimpleName()) {
            case "Bank":
                openBankDialog(dialog, mode);
                break;
            case "Client":
                openClientDialog(dialog, mode);
                break;
            case "Credit":
                openCreditDialog(dialog, mode);
                break;
            case "CreditSuggestion":
                openCreditSuggestionDialog(dialog, mode);
                break;
        }
    }

    private void openBankDialog(Window dialog, DialogMode mode) {
        BankDialog cl = null;
        dialog.setHeight(120, Unit.PIXELS);
        dialog.setWidth(380, Unit.PIXELS);
        if (mode == DialogMode.SAVE) {
            cl = new BankDialog(mode);
        }
        if (mode == DialogMode.EDIT) {
            BankLayout layout = (BankLayout) getUI().getContent();
            Bank selected = layout.getSelected();
            if (selected != null) {
                cl = new BankDialog(selected, mode);
            } else {
                return;
            }
        }
        dialog.setContent(cl);
        getUI().addWindow(dialog);
    }

    private void openClientDialog(Window dialog, DialogMode mode) {
        ClientDialog cl = null;
        dialog.setHeight(440, Unit.PIXELS);
        dialog.setWidth(380, Unit.PIXELS);
        if (mode == DialogMode.SAVE) {
            cl = new ClientDialog(mode);
        }
        if (mode == DialogMode.EDIT) {
            ClientLayout layout = (ClientLayout) getUI().getContent();
            Client selected = layout.getSelected();
            if (selected != null) {
                cl = new ClientDialog(selected, mode);
            } else {
                return;
            }
        }
        dialog.setContent(cl);
        getUI().addWindow(dialog);
        cl.initBankComboBox();
    }

    private void openCreditDialog(Window dialog, DialogMode mode) {
        CreditDialog cred = null;
        dialog.setHeight(380, Unit.PIXELS);
        dialog.setWidth(380, Unit.PIXELS);
        if (mode == DialogMode.SAVE) {
            cred = new CreditDialog(mode);
            dialog.setContent(cred);
        }
        if (mode == DialogMode.EDIT) {
            CreditLayout layout = (CreditLayout) getUI().getContent();
            Credit selected = layout.getSelected();
            if (selected != null) {
                cred = new CreditDialog(selected, mode);
            } else {
                return;
            }
        }
        dialog.setContent(cred);
        getUI().addWindow(dialog);
        cred.initBankComboBox();
    }

    private void openCreditSuggestionDialog(Window dialog, DialogMode mode) {
        CreditSuggestionDialog sugg = null;
        dialog.setSizeFull();
        if (mode == DialogMode.SAVE) {
            sugg = new CreditSuggestionDialog(mode);
            dialog.setContent(sugg);
        }
        if (mode == DialogMode.EDIT) {
            CreditSuggestionLayout layout = (CreditSuggestionLayout) getUI().getContent();
            CreditSuggestion selected = layout.getSelected();
            if (selected != null) {
                sugg = new CreditSuggestionDialog(mode, selected);
            } else {
                return;
            }
        }
        dialog.setContent(sugg);
        getUI().addWindow(dialog);
        sugg.initBankComboBox();
    }
}
