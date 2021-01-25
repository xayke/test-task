package com.haulmont.testtask;

import com.haulmont.testtask.ui.MainLayout;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        addMainLayout();
    }

    private void addMainLayout() {
        MainLayout layout = new MainLayout();
        setContent(layout);
    }
}