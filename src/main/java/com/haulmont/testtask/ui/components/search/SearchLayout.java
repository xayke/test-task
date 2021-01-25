package com.haulmont.testtask.ui.components.search;

import com.haulmont.testtask.domain.fields.Field;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

public abstract class SearchLayout extends HorizontalLayout {
    protected TextField field;
    protected ComboBox<String> comboBox;
    protected Button button;

    protected SearchLayout() {
        addSearchField();
        addComboBox();
        addSearchButton();
        setButtonClickListener();
    }

    protected void addSearchField() {
        field = new TextField();
        field.setWidth(280, Unit.PIXELS);
        field.setPlaceholder("Поиск");
        addComponent(field);
        setComponentAlignment(getComponent(0), Alignment.BOTTOM_RIGHT);
    }

    protected void addSearchButton() {
        button = new Button();
        button.setIcon(VaadinIcons.SEARCH);
        button.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        addComponent(button);
        setComponentAlignment(button, Alignment.BOTTOM_RIGHT);
    }

    protected abstract void addComboBox();

    protected abstract Field getFieldForSearch();

    protected abstract void setButtonClickListener();
}
