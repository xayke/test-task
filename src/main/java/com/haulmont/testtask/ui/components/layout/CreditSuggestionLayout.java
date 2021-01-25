package com.haulmont.testtask.ui.components.layout;

import com.haulmont.testtask.domain.CreditSuggestion;
import com.haulmont.testtask.service.CreditSuggestionService;
import com.haulmont.testtask.service.PaymentService;
import com.haulmont.testtask.service.ServiceException;
import com.haulmont.testtask.ui.components.ActionLayout;
import com.haulmont.testtask.ui.components.table.CreditSuggestionTable;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Notification;

import java.util.List;

public class CreditSuggestionLayout extends AbstractLayout<CreditSuggestion> {

    private PaymentService paymentService;

    public CreditSuggestionLayout() {
        super();
        service = CreditSuggestionService.getInstance();
        paymentService = PaymentService.getInstance();
        addGrid();
    }

    public void addActionLayout() {
        addComponent(new ActionLayout(CreditSuggestion.class));
    }

    protected void addGrid() {
        try {
            List<CreditSuggestion> suggestions = service.getAll();
            table = new CreditSuggestionTable(suggestions);
            table.setHeight(800, Sizeable.Unit.PIXELS);
            table.addSelectionListener((e) -> {
                selected = table.asSingleSelect().getValue();
                enableButtons();
            });
            addComponent(table);

        } catch (ServiceException e) {
            Notification.show(e.getMessage());
        }
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
