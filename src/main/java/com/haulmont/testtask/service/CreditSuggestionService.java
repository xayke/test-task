package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DAOService;
import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.dao.impls.CreditSuggestionDAOService;
import com.haulmont.testtask.domain.CreditSuggestion;
import com.haulmont.testtask.domain.fields.impls.CreditSuggestionField;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.validator.Validator;
import com.haulmont.testtask.validator.ValidatorException;
import com.haulmont.testtask.validator.impls.CreditSuggestionValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreditSuggestionService extends AbstractService<CreditSuggestion> {
    private static CreditSuggestionService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(CreditSuggestionService.class);
    private static DAOService<CreditSuggestion> daoService;
    private final Validator<CreditSuggestion> validator;

    private CreditSuggestionService() {
        daoService = CreditSuggestionDAOService.getInstance();
        validator = CreditSuggestionValidator.getInstance();
    }

    public static CreditSuggestionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreditSuggestionService();
        }
        return INSTANCE;
    }

    public List<CreditSuggestion> getAll() throws ServiceException {
        List<CreditSuggestion> result = null;
        try {
            result = daoService.findAll();
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get credit suggestions list", e);
            throw new ServiceException("Can't get credit suggestions list", e);
        }
        return result;
    }

    public CreditSuggestion getById(UUID id) throws ServiceException {
        CreditSuggestion result = null;
        try {
            result = daoService.findById(id);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get credit suggestion", e);
            throw new ServiceException("Can't get credit suggestion", e);
        }
        return result;
    }

    public void save(CreditSuggestion suggestion, UUID id) throws ServiceException {
        try {
            if (validator.isValid(suggestion)) {
                daoService.save(suggestion);
            }
        } catch (DaoServiceException e) {
            LOGGER.error("Can't save credit suggestion", e);
            throw new ServiceException("Can't save credit suggestion", e);
        } catch (ValidatorException e) {
            LOGGER.error("Credit suggestion isn't valid", e);
            throw new ServiceException("Credit suggestion isn't valid", e);
        }
    }

    public void delete(CreditSuggestion suggestion) throws ServiceException {
        try {
            daoService.delete(suggestion);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't delete credit suggestion", e);
            throw new ServiceException("Can't delete credit suggestion", e);
        }
    }

    public void update(CreditSuggestion suggestion, UUID id) throws ServiceException {
        try {
            if (validator.isValid(suggestion)) {
                daoService.update(suggestion);
            }
        } catch (DaoServiceException e) {
            LOGGER.error("Can't update credit suggestion", e);
            throw new ServiceException("Can't update credit suggestion", e);
        } catch (ValidatorException e) {
            LOGGER.error("Credit suggestion isn't valid", e);
            throw new ServiceException("Credit suggestion isn't valid", e);
        }
    }

    public List<CreditSuggestion> searchByField(List<CreditSuggestion> list, String input, Field field) {
        List<CreditSuggestion> result = null;
        switch ((CreditSuggestionField) field.getValue()) {
            case CLIENT:
                result = list.stream()
                        .filter(suggestion -> suggestion.getClientFullName().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case CREDIT:
                result = list.stream()
                        .filter(suggestion -> suggestion.getCreditPercents().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case AMOUNT:
                result = list.stream()
                        .filter(credit -> credit.getAmount().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }
}
