package com.haulmont.testtask.calculator;

import com.haulmont.testtask.domain.Credit;
import org.junit.jupiter.api.*;

import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreditSuggestionCalculatorTest {

    private static CreditSuggestionCalculator calculator;
    private Credit credit;
    private String amount;
    private String term;

    @BeforeAll
    static void initCalculator() {
        calculator = CreditSuggestionCalculator.getInstance();
    }

    @BeforeEach
    void createNewCredit() {
        credit = new Credit();
        credit.setId(UUID.randomUUID());
        credit.setName("Тестовый кредит");
        credit.setMinLimit("10000");
        credit.setMaxLimit("20000000");
    }

    @Test
    @Order(1)
    void calculatePaymentPlan_CorrectData_TestPass() {
        amount = "10000";
        term = "24";
        credit.setPercent("3");
        Assertions.assertDoesNotThrow(() -> calculator.calculatePaymentPlan(amount, term, credit, UUID.randomUUID()));
    }

    @Test
    @Order(2)
    void calculatePaymentPlan_IncorrectAmount_ThrowsCalculatorException() {
        amount = "1000z0";
        term = "24";
        credit.setPercent("3");
        Assertions.assertThrows(CalculatorException.class,
                () -> calculator.calculatePaymentPlan(amount, term, credit, UUID.randomUUID()));
    }

    @Test
    @Order(2)
    void calculatePaymentPlan_IncorrectTerm_ThrowsCalculatorException() {
        amount = "10000";
        term = "24b";
        credit.setPercent("3");
        Assertions.assertThrows(CalculatorException.class,
                () -> calculator.calculatePaymentPlan(amount, term, credit, UUID.randomUUID()));
    }

    @Test
    @Order(3)
    void calculatePaymentPlan_IncorrectPercent_ThrowsCalculatorException() {
        amount = "10000";
        term = "24";
        credit.setPercent("3o");
        Assertions.assertThrows(CalculatorException.class,
                () -> calculator.calculatePaymentPlan(amount, term, credit, UUID.randomUUID()));
    }

    @Test
    @Order(4)
    void calculateMonthlyPayment_CorrectData_TestPass() {
        amount = "10000";
        term = "24";
        credit.setPercent("3");
        Assertions.assertEquals("430", calculator.calculateMonthlyPayment(amount, term, credit.getPercent()));
    }

    @Test
    @Order(5)
    void calculateSummaryInterestAmount_CorrectData_TestPass() {
        amount = "10000";
        term = "24";
        credit.setPercent("3");
        String monthlyPayment = calculator.calculateMonthlyPayment(amount, term, credit.getPercent());
        Assertions.assertEquals("320", calculator.calculateSummaryInterestAmount(amount, term, monthlyPayment));
    }

    @Test
    @Order(6)
    void calculateSummaryFullAmount_CorrectData_TestPass() {
        amount = "10000";
        term = "24";
        credit.setPercent("3");
        String monthlyPayment = calculator.calculateMonthlyPayment(amount, term, credit.getPercent());
        String interestAmount = calculator.calculateSummaryInterestAmount(amount, term, monthlyPayment);
        Assertions.assertEquals("10320", calculator.calculateSummaryFullAmount(amount, interestAmount));
    }
}
