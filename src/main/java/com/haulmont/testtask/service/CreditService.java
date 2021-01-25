package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.dao.impls.BankDependentDaoService;
import com.haulmont.testtask.dao.impls.CreditDAOService;
import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.domain.fields.impls.CreditField;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.validator.Validator;
import com.haulmont.testtask.validator.ValidatorException;
import com.haulmont.testtask.validator.impls.CreditValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreditService extends AbstractService<Credit> {
    private static CreditService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(CreditService.class);
    private static BankDependentDaoService<Credit> daoService;
    private final Validator<Credit> validator;

    private CreditService() {
        daoService = CreditDAOService.getInstance();
        validator = CreditValidator.getInstance();
    }

    public static CreditService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditService();
        }
        return INSTANCE;
    }

    public List<Credit> getAll() throws ServiceException {
        List<Credit> result = null;
        try {
            result = daoService.findAll();
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get credits list", e);
            throw new ServiceException("Can't get credits list", e);
        }
        return result;
    }

    public Credit getById(UUID id) throws ServiceException {
        Credit result = null;
        try {
            result = daoService.findById(id);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get credit", e);
            throw new ServiceException("Can't get credit", e);
        }
        return result;
    }

    public List<Credit> getAlWhereBankIdl(UUID id) throws ServiceException {
        List<Credit> result = null;
        try {
            result = daoService.findAllWhereBankId(id);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get credits list", e);
            throw new ServiceException("Can't get credits list", e);
        }
        return result;
    }

    public void save(Credit credit, UUID bankId) throws ServiceException {
        try {
            if (validator.isValid(credit)) {
                daoService.save(credit, bankId);
            }
        } catch (DaoServiceException e) {
            LOGGER.error("Can't save credit", e);
            throw new ServiceException("Can't save credit", e);
        } catch (ValidatorException e) {
            LOGGER.error("Credit isn't valid", e);
            throw new ServiceException("Credit isn't valid", e);
        }
    }

    public void update(Credit credit, UUID bankId) throws ServiceException {
        try {
            if (validator.isValid(credit)) {
                daoService.update(credit, bankId);
            }
        } catch (DaoServiceException e) {
            LOGGER.error("Can't update credit", e);
            throw new ServiceException("Can't update credit", e);
        } catch (ValidatorException e) {
            LOGGER.error("Credit isn't valid", e);
            throw new ServiceException("Credit isn't valid", e);
        }
    }

    public void delete(Credit credit) throws ServiceException {
        try {
            daoService.delete(credit);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't delete credit", e);
            throw new ServiceException("Can't delete credit", e);
        }
    }

    public List<Credit> searchByField(List<Credit> list, String input, Field field) {
        List<Credit> result = null;
        switch ((CreditField) field.getValue()) {
            case NAME:
                result = list.stream()
                        .filter(credit -> credit.getName().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case MIN_LIMIT:
                result = list.stream()
                        .filter(credit -> credit.getMinLimit().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case MAX_LIMIT:
                result = list.stream()
                        .filter(credit -> credit.getMaxLimit().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case PERCENT:
                result = list.stream()
                        .filter(credit -> credit.getPercent().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
        }

        return result;
    }
}
