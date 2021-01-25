package com.haulmont.testtask.ui.components.dialog;

import com.haulmont.testtask.domain.Bank;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public abstract class AbstractDialog extends VerticalLayout {

    protected Button confirm;
    protected Button cancel;

    protected DialogMode mode;

    protected ComboBox<Bank> bankComboBox;

    protected AbstractDialog(DialogMode mode) {
        this.mode = mode;
        addComboBox();
        addFields();
        addButtons();
    }

    protected void addComboBox() {
        bankComboBox = new ComboBox<>();
        bankComboBox.setCaption("Банк");
        bankComboBox.setItemCaptionGenerator(Bank::getName);
        bankComboBox.setEmptySelectionAllowed(false);
        bankComboBox.setTextInputAllowed(false);
        bankComboBox.setWidth(220, Unit.PIXELS);
        addComponent(bankComboBox);
        setComponentAlignment(bankComboBox, Alignment.MIDDLE_CENTER);
    }

    protected abstract void addFields();

    protected abstract void initFields();

    protected TextField addTextField(String placeholder) {
        TextField field = new TextField();
        field.setWidth(220, Unit.PIXELS);
        field.setPlaceholder(placeholder);
        addComponent(field);
        setComponentAlignment(field, Alignment.MIDDLE_CENTER);
        return field;
    }

    protected void addButtons() {
        HorizontalLayout btnLayout = new HorizontalLayout();
        confirm = new Button("Сохранить");
        cancel = new Button("Отмена");

        confirm.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        confirm.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        cancel.setStyleName(ValoTheme.BUTTON_DANGER);

        setConfirmOnClickListener();
        setCancelOnClickListener();

        btnLayout.addComponents(confirm, cancel);
        addComponent(btnLayout);
        setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
    }

    protected abstract void setConfirmOnClickListener();

    protected void setCancelOnClickListener() {
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window dialog = (Window) getParent();
                dialog.close();
            }
        });
    }
}
