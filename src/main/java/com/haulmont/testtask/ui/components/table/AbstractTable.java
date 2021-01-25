package com.haulmont.testtask.ui.components.table;

import com.vaadin.ui.Grid;

import java.util.List;

public abstract class AbstractTable<T> extends Grid<T> {
    protected AbstractTable(List<T> data) {
        setItems(data);
        initColumns();
        setSelectionMode(SelectionMode.SINGLE);
        setSizeFull();
    }

    protected abstract void initColumns();

    public void update(List<T> data) {
        setItems(data);
    }
}
