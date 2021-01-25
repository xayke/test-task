package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.dao.impls.BankDAOService;
import com.haulmont.testtask.domain.Bank;
import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.domain.Credit;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.domain.fields.impls.BankField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankService extends AbstractService<Bank> {
    private static BankService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(BankService.class);
    private BankDAOService daoService;

    private BankService() {
        daoService = BankDAOService.getInstance();
    }

    public static BankService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankService();
        }
        return INSTANCE;
    }

    @Override
    public List<Bank> getAll() throws ServiceException {
        List<Bank> result = null;
        try {
            result = daoService.findAll();
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get banks list", e);
            throw new ServiceException("Can't get banks list", e);
        }
        return result;
    }

    @Override
    public Bank getById(UUID id) {
        Bank result = null;
        try {
            result = daoService.findById(id);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get bank", e);
            throw new ServiceException("Can't get bank", e);
        }
        return result;
    }

    public Bank getBankByClient(Client client) {
        Bank result = null;
        try {
            result = daoService.findBankByClient(client);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get bank", e);
            throw new ServiceException("Can't get bank", e);
        }
        return result;
    }

    public Bank getBankByCredit(Credit credit) {
        Bank result = null;
        try {
            result = daoService.findBankByCredit(credit);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get bank", e);
            throw new ServiceException("Can't get bank", e);
        }
        return result;
    }

    @Override
    public void save(Bank bank, UUID bankId) {
        try {
            daoService.save(bank);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't save bank", e);
            throw new ServiceException("Can't save bank", e);
        }
    }

    @Override
    public void delete(Bank bank) {
        try {
            daoService.delete(bank);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't delete bank", e);
            throw new ServiceException("Can't delete bank", e);
        }
    }

    @Override
    public void update(Bank bank, UUID bankId) {
        try {
            daoService.update(bank);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't update bank", e);
            throw new ServiceException("Can't update bank", e);
        }
    }

    @Override
    public List<Bank> searchByField(List<Bank> list, String input, Field field) {
        List<Bank> result = null;
        switch ((BankField) field.getValue()) {
            case NAME:
                result = list.stream()
                        .filter(bank -> bank.getName().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }
}
