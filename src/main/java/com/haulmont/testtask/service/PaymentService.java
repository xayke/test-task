package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DAOService;
import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.dao.impls.PaymentDAOService;
import com.haulmont.testtask.domain.Payment;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.domain.fields.impls.PaymentField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaymentService extends AbstractService<Payment> {
    private static PaymentService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(PaymentService.class);
    private DAOService<Payment> daoService;

    public PaymentService() {
        daoService = PaymentDAOService.getInstance();
    }

    public static PaymentService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PaymentService();
        }
        return INSTANCE;
    }

    @Override
    public List<Payment> getAll() throws ServiceException {
        List<Payment> result = null;
        try {
            result = daoService.findAll();
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get payments list", e);
            throw new ServiceException("Can't get payments list", e);
        }
        return result;
    }

    @Override
    public Payment getById(UUID id) {
        Payment result = null;
        try {
            result = daoService.findById(id);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get payment", e);
            throw new ServiceException("Can't get payment", e);
        }
        return result;
    }

    @Override
    public void save(Payment payment, UUID id) {
        try {
            daoService.save(payment);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't save payment", e);
            throw new ServiceException("Can't save payment", e);
        }
    }

    @Override
    public void delete(Payment payment) {
        try {
            daoService.delete(payment);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't delete payment", e);
            throw new ServiceException("Can't delete payment", e);
        }
    }

    @Override
    public void update(Payment payment, UUID id) {
        try {
            daoService.update(payment);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't update payment", e);
            throw new ServiceException("Can't update payment", e);
        }
    }

    @Override
    public List<Payment> searchByField(List<Payment> list, String input, Field field) {
        List<Payment> result = null;
        switch ((PaymentField) field.getValue()) {
            case DATE:
                result = list.stream()
                        .filter(payment -> payment.getFormattedStringDate().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case FULL_AMOUNT:
                result = list.stream()
                        .filter(payment -> payment.getFullAmount().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case PRINCIPAL_AMOUNT:
                result = list.stream()
                        .filter(payment -> payment.getPrincipalAmount().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case INTEREST_AMOUNT:
                result = list.stream()
                        .filter(payment -> payment.getInterestAmount().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }
}
