package com.haulmont.testtask.ui.components.dialog;

import com.haulmont.testtask.domain.*;
import com.haulmont.testtask.service.*;
import com.haulmont.testtask.calculator.CreditSuggestionCalculator;
import com.haulmont.testtask.ui.components.layout.CreditSuggestionLayout;
import com.haulmont.testtask.ui.components.table.PaymentTable;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CreditSuggestionDialog extends AbstractDialog {

    private Label amountLimitLabel;
    private Label interestRateLabel;
    private Label monthlyPaymentLabel;
    private Label interestAmountLabel;
    private Label totalAmountLabel;

    private Button calculateButton;
    private Button paymentPlanButton;

    private ComboBox<Credit> creditComboBox;
    private ComboBox<Client> clientComboBox;
    private TextField creditAmountField;
    private TextField termField;

    private CreditSuggestionCalculator calculator;
    private CreditSuggestion suggestion;

    public CreditSuggestionDialog(DialogMode mode) {
        super(mode);
        calculator = CreditSuggestionCalculator.getInstance();
        addCalculateButton();
        addLabels();
        addPaymentPlanButton();
        initListeners();
        hideCalculationsAndDisableConfirmButton();
    }

    public CreditSuggestionDialog(DialogMode mode, CreditSuggestion suggestion) {
        this(mode);
        this.suggestion = suggestion;
    }

    private void addCalculateButton() {
        calculateButton = new Button("Рассчитать");
        calculateButton.setWidth(220, Unit.PIXELS);
        calculateButton.setIcon(VaadinIcons.CALC);
        calculateButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

        addComponent(calculateButton);
        setComponentAlignment(calculateButton, Alignment.MIDDLE_CENTER);

        calculateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (isAllFieldsPresentedAndCorrect()) {
                    makeCalculations();

                    showCalculationsAndActivateConfirmButton();

                }
            }
        });
    }

    private void addPaymentPlanButton() {
        paymentPlanButton = new Button("График платежей");
        paymentPlanButton.setWidth(220, Unit.PIXELS);
        paymentPlanButton.setIcon(VaadinIcons.CALENDAR);
        paymentPlanButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

        addComponent(paymentPlanButton);
        setComponentAlignment(paymentPlanButton, Alignment.MIDDLE_CENTER);

        paymentPlanButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window window = new Window();
                window.setHeight(600, Unit.PIXELS);
                window.setWidth(800, Unit.PIXELS);
                window.setModal(true);
                VerticalLayout layout = new VerticalLayout();
                layout.setSizeFull();
                layout.setMargin(true);
                layout.addComponent(new PaymentTable(suggestion.getPaymentPlan()));
                layout.addComponent(new Button("Закрыть", clickEvent1 -> window.close()));
                layout.getComponent(1).setStyleName(ValoTheme.BUTTON_DANGER);
                layout.getComponent(0).setHeight(420, Unit.PIXELS);
                layout.setComponentAlignment(layout.getComponent(0), Alignment.MIDDLE_CENTER);
                layout.setComponentAlignment(layout.getComponent(1), Alignment.BOTTOM_CENTER);
                window.setContent(layout);
                window.center();
                getUI().addWindow(window);
            }
        });
    }

    @Override
    protected void addComboBox() {
        bankComboBox = newComboBox(bankComboBox, "Банк");
        bankComboBox.setItemCaptionGenerator(Bank::getName);
        creditComboBox = newComboBox(creditComboBox, "Кредит");
        creditComboBox.setItemCaptionGenerator(Credit::getName);
        creditComboBox.setEnabled(false);
        clientComboBox = newComboBox(clientComboBox, "Клиент");
        clientComboBox.setItemCaptionGenerator(Client::getFullName);
        clientComboBox.setEnabled(false);
    }

    private ComboBox newComboBox(ComboBox comboBox, String caption) {
        comboBox = new ComboBox<>();
        comboBox.setCaption(caption);
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setTextInputAllowed(false);
        comboBox.setWidth(220, Unit.PIXELS);
        addComponent(comboBox);
        setComponentAlignment(comboBox, Alignment.MIDDLE_CENTER);
        return comboBox;
    }

    private void addLabels() {
        interestRateLabel = newLabel();
        monthlyPaymentLabel = newLabel();
        interestAmountLabel = newLabel();
        totalAmountLabel = newLabel();
    }

    private Label newLabel() {
        Label label = new Label();
        label.setWidth(220, Unit.PIXELS);
        label.setVisible(false);
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        return label;
    }

    public void initBankComboBox() {
        List<Bank> banks = getLayout().getBankService().getAll();
        bankComboBox.setItems(banks);

        if (mode == DialogMode.EDIT) {
            bankComboBox.setSelectedItem(getLayout().getBankService().getBankByClient(suggestion.getClient()));

            List<Credit> credits = CreditService.getInstance().getAlWhereBankIdl(bankComboBox.getSelectedItem().get().getId());
            List<Client> clients = ClientService.getInstance().getAllWhereBankId(bankComboBox.getSelectedItem().get().getId());

            clientComboBox.setItems(clients);
            clientComboBox.setSelectedItem(getLayout().getSelected().getClient());

            creditComboBox.setItems(credits);
            creditComboBox.setSelectedItem(getLayout().getSelected().getCredit());

            creditAmountField.setValue(suggestion.getAmount());
            termField.setValue(String.valueOf(suggestion.getPaymentPlan().size()));
            makeCalculations();
            showCalculationsAndActivateConfirmButton();
        }
    }

    @Override
    protected void addFields() {
        creditAmountField = addTextField("Размер займа");
        amountLimitLabel = new Label("");
        amountLimitLabel.setWidth(220, Unit.PIXELS);
        amountLimitLabel.setVisible(false);
        addComponent(amountLimitLabel);
        setComponentAlignment(amountLimitLabel, Alignment.MIDDLE_CENTER);
        termField = addTextField("Срок (в месяцах)");
        creditAmountField.setEnabled(false);
    }

    @Override
    protected void initFields() {
        termField.setValue(String.valueOf(suggestion.getPaymentPlan().size()));
        creditAmountField.setValue(suggestion.getAmount());
    }


    private void initListeners() {
        bankComboBox.addValueChangeListener(new HasValue.ValueChangeListener<Bank>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Bank> valueChangeEvent) {
                if (suggestion == null) {
                    suggestion = new CreditSuggestion();
                    suggestion.setId(UUID.randomUUID());
                }
                Bank newBank = valueChangeEvent.getValue();
                if ((valueChangeEvent.getOldValue() != null) && !valueChangeEvent.getOldValue().equals(newBank)) {
                    creditAmountField.setEnabled(false);
                    creditComboBox.setSelectedItem(null);
                    clientComboBox.setSelectedItem(null);

                    hideCalculationsAndDisableConfirmButton();
                }

                if (newBank == null) return;
                List<Credit> credits = CreditService.getInstance().getAlWhereBankIdl(newBank.getId());
                List<Client> clients = ClientService.getInstance().getAllWhereBankId(newBank.getId());

                creditComboBox.setItems(credits);
                clientComboBox.setItems(clients);

                creditComboBox.setEnabled(true);
                clientComboBox.setEnabled(true);
            }
        });
        creditComboBox.addValueChangeListener(new HasValue.ValueChangeListener<Credit>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Credit> valueChangeEvent) {
                Credit newCredit = valueChangeEvent.getValue();
                if ((valueChangeEvent.getOldValue() != null) && !valueChangeEvent.getOldValue().equals(newCredit)) {
                    hideCalculationsAndDisableConfirmButton();
                }
                if (newCredit == null) return;
                suggestion.setCredit(newCredit);
                creditAmountField.setEnabled(true);
                creditAmountField.setValue(suggestion.getCredit().getMinLimit());
                amountLimitLabel.setValue(String.format("(%s - %s)", newCredit.getMinLimit(), newCredit.getMaxLimit()));
                amountLimitLabel.setVisible(true);
            }
        });
        clientComboBox.addValueChangeListener(new HasValue.ValueChangeListener<Client>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Client> valueChangeEvent) {
                Client newClient = valueChangeEvent.getValue();
                if (newClient == null) return;
                suggestion.setClient(newClient);
            }
        });
        creditAmountField.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                hideCalculationsAndDisableConfirmButton();
            }
        });
    }

    private void makeCalculations() {
        if (suggestion.getPaymentPlan() == null) {
            suggestion.setPaymentPlan(calculator.calculatePaymentPlan(creditAmountField.getValue(),
                    termField.getValue(), suggestion.getCredit(), suggestion.getId()));
        } else {
            suggestion.setPaymentPlan(calculator.recalculatePaymentPlan(suggestion.getPaymentPlan(),
                    creditAmountField.getValue(), termField.getValue(),
                    suggestion.getCredit(), suggestion.getId()));
        }
        String monthlyPayment = calculator.calculateMonthlyPayment(creditAmountField.getValue().trim(),
                termField.getValue().trim(), suggestion.getCredit().getPercent());
        String interestAmount = calculator.calculateSummaryInterestAmount(creditAmountField.getValue().trim(),
                termField.getValue().trim(), monthlyPayment);
        String total = calculator.calculateSummaryFullAmount(creditAmountField.getValue().trim(),
                interestAmount);
        interestRateLabel.setValue("Ставка: " + suggestion.getCreditPercents() + "%");
        monthlyPaymentLabel.setValue("Ежемесячный платёж: " + monthlyPayment);
        interestAmountLabel.setValue("Переплата по кредиту: " + interestAmount);
        totalAmountLabel.setValue("Общая выплата: " + total);
    }

    private void showCalculationsAndActivateConfirmButton() {
        interestRateLabel.setVisible(true);
        monthlyPaymentLabel.setVisible(true);
        interestAmountLabel.setVisible(true);
        totalAmountLabel.setVisible(true);

        paymentPlanButton.setVisible(true);
        confirm.setEnabled(true);
        setHeight(800, Unit.PIXELS);
    }

    private void hideCalculationsAndDisableConfirmButton() {
        interestRateLabel.setVisible(false);
        interestAmountLabel.setVisible(false);
        totalAmountLabel.setVisible(false);
        monthlyPaymentLabel.setVisible(false);
        confirm.setEnabled(false);
        paymentPlanButton.setVisible(false);
        setHeight(600, Unit.PIXELS);
    }

    @Override
    protected void setConfirmOnClickListener() {
        confirm.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (!isAllFieldsPresentedAndCorrect()) {
                    return;
                }
                createSuggestion();
                switch (mode) {
                    case SAVE:
                        getService().save(suggestion, null);
                        for (Payment payment : suggestion.getPaymentPlan()) {
                            getPaymentService().save(payment, null);
                        }
                        break;
                    case EDIT:
                        deleteUnusedPayments(suggestion.getPaymentPlan(), termField.getValue());
                        for (Payment payment : suggestion.getPaymentPlan()) {
                            getPaymentService().update(payment, null);
                        }
                        getService().update(suggestion, null);
                        break;
                }
                Window dialog = (Window) getParent();
                getLayout().updateTable(getLayout().getService().getAll());
                getLayout().setSelected(null);
                dialog.close();
            }
        });
    }

    private boolean isAmountCorrect(BigDecimal amount, BigDecimal min, BigDecimal max) {
        return (amount.compareTo(min) == max.compareTo(amount)) ||
                ((amount.compareTo(min) == 0) || (amount.compareTo(max) == 0));
    }

    private boolean isAllDataPresented() {
        return bankComboBox.getSelectedItem().isPresent()
                && creditComboBox.getSelectedItem().isPresent() && clientComboBox.getSelectedItem().isPresent()
                && !creditAmountField.getValue().isEmpty() && !termField.getValue().isEmpty();
    }

    private boolean isAllFieldsPresentedAndCorrect() {
        try {
            boolean isPresented = isAllDataPresented();
            if (!isPresented) {
                Notification.show("Некоторые данные не заполнены");
                return false;
            }
            BigDecimal amount = new BigDecimal(creditAmountField.getValue());
            BigDecimal minLimit = new BigDecimal(suggestion.getCredit().getMinLimit());
            BigDecimal maxLimit = new BigDecimal(suggestion.getCredit().getMaxLimit());
            Integer.parseInt(termField.getValue());
            boolean isAmountCorrect = isAmountCorrect(amount, minLimit, maxLimit);
            if (!isAmountCorrect) {
                Notification.show("Размер займа должен находиться в рамках лимита");
                return false;
            }
        } catch (NumberFormatException e) {
            Notification.show("Срок и размер займа должны быть числами");
            return false;
        }
        return true;
    }

    private void createSuggestion() {
        if (suggestion == null) {
            suggestion = new CreditSuggestion();
            suggestion.setId(UUID.randomUUID());
        }
        suggestion.setAmount(creditAmountField.getValue());
    }

    private CreditSuggestionLayout getLayout() {
        return (CreditSuggestionLayout) getUI().getContent();
    }

    private AbstractService<CreditSuggestion> getService() {
        return getLayout().getService();
    }

    private PaymentService getPaymentService() {
        return getLayout().getPaymentService();
    }

    private void deleteUnusedPayments(List<Payment> paymentPlan, String term) {
        int termInt = Integer.parseInt(term);
        while (termInt < paymentPlan.size()) {
            getPaymentService().delete(paymentPlan.get(paymentPlan.size() - 1));
        }
    }

}
