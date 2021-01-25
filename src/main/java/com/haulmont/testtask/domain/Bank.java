package com.haulmont.testtask.domain;

import java.util.*;

public class Bank {
    private UUID id;
    private String name;
    private List<Client> clients;
    private List<Credit> credits;

    public Bank() {
    }

    public Bank(String name, List<Client> clients, List<Credit> credits) {
        id = UUID.randomUUID();
        this.name = name;
        this.clients = clients;
        this.credits = credits;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public void setCredits(List<Credit> credits) {
        this.credits = credits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return id.equals(bank.id) && name.equals(bank.name);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return '{' +
                "\n\t'id': '" + id + '\'' +
                ",\n\t'name': '" + name + '\'' +
                ",\n\t'clients': " + clients +
                ",\n\t'credits': " + credits +
                "\n}";
    }
}
