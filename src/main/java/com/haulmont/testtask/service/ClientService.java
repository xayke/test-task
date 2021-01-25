package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DaoServiceException;
import com.haulmont.testtask.dao.impls.BankDependentDaoService;
import com.haulmont.testtask.dao.impls.ClientDAOService;
import com.haulmont.testtask.domain.Client;
import com.haulmont.testtask.domain.fields.impls.ClientField;
import com.haulmont.testtask.domain.fields.Field;
import com.haulmont.testtask.validator.Validator;
import com.haulmont.testtask.validator.ValidatorException;
import com.haulmont.testtask.validator.impls.ClientValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientService extends AbstractService<Client> {
    private static ClientService INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(ClientService.class);
    private static BankDependentDaoService<Client> daoService;
    private final Validator<Client> validator;

    private ClientService() {
        daoService = ClientDAOService.getInstance();
        validator = ClientValidator.getInstance();
    }

    public static ClientService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientService();
        }
        return INSTANCE;
    }

    public List<Client> getAll() throws ServiceException {
        List<Client> result = null;
        try {
            result = daoService.findAll();
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get clients list", e);
            throw new ServiceException("Can't get clients list", e);
        }
        return result;
    }

    public Client getById(UUID id) throws ServiceException {
        Client result = null;
        try {
            result = daoService.findById(id);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get client", e);
            throw new ServiceException("Can't get client", e);
        }
        return result;
    }

    public List<Client> getAllWhereBankId(UUID id) throws ServiceException {
        List<Client> result = null;
        try {
            result = daoService.findAllWhereBankId(id);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't get clients list", e);
            throw new ServiceException("Can't get clients list", e);
        }
        return result;
    }

    public void save(Client client, UUID bankId) throws ServiceException {
        try {
            if (validator.isValid(client)) {
                daoService.save(client, bankId);
            }
        } catch (DaoServiceException e) {
            LOGGER.error("Can't save client", e);
            throw new ServiceException("Can't save client", e);
        } catch (ValidatorException e) {
            LOGGER.error("Client isn't valid", e);
            throw new ServiceException("Client isn't valid", e);
        }
    }

    public void update(Client client, UUID bankID) throws ServiceException {
        try {
            if (validator.isValid(client)) {
                daoService.update(client, bankID);
            }
        } catch (DaoServiceException e) {
            LOGGER.error("Can't update client", e);
            throw new ServiceException("Can't update client", e);
        } catch (ValidatorException e) {
            LOGGER.error("Client isn't valid", e);
            throw new ServiceException("Client isn't valid", e);
        }
    }

    public void delete(Client client) throws ServiceException {
        try {
            daoService.delete(client);
        } catch (DaoServiceException e) {
            LOGGER.error("Can't delete client", e);
            throw new ServiceException("Can't delete client", e);
        }
    }

    public List<Client> searchByField(List<Client> list, String input, Field field) {
        List<Client> result = null;
        switch ((ClientField) field.getValue()) {
            case FIRSTNAME:
                result = list.stream()
                        .filter(client -> client.getFirstname().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case LASTNAME:
                result = list.stream()
                        .filter(client -> client.getLastname().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case PATRONYMIC:
                result = list.stream()
                        .filter(client -> (client.getPatronymic() != null)
                                && client.getPatronymic().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case PHONE:
                result = list.stream()
                        .filter(client -> client.getPhone().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case EMAIL:
                result = list.stream()
                        .filter(client -> (client.getEmail() != null) && client.getEmail().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
            case PASSPORT:
                result = list.stream()
                        .filter(client -> client.getPassport().matches(String.format(FILTER_PATTERN, input)))
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }
}
