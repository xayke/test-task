package com.haulmont.testtask.domain;

import java.util.UUID;

public class Client {
    private UUID id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String phone;
    private String email;
    private String passport;

    public Client() {
    }

    public Client(String firstname, String lastname, String patronymic, String phone, String email, String passport) {
        id = UUID.randomUUID();
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.passport = passport;
    }

    public Client(UUID id, String firstname, String lastname, String patronymic,
                  String phone, String email, String passport) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.passport = passport;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getFullName() {
        return lastname + " " + firstname + ((patronymic != null) ? " " + patronymic : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id) && firstname.equals(client.firstname) && lastname.equals(client.lastname)
                && ((patronymic != null) && patronymic.equals(client.patronymic)) && phone.equals(client.phone)
                && ((email != null) && email.equals(client.email)) && passport.equals(client.passport);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "{\n\t'id': '" + id + '\'' +
                ",\n\t'firstname': '" + firstname + '\'' +
                ",\n\t'lastname': '" + lastname + '\'' +
                ",\n\t'patronymic': '" + patronymic + '\'' +
                ",\n\t'phone': '" + phone + '\'' +
                ",\n\t'email': '" + email + '\'' +
                ",\n\t'passport': '" + passport + '\'' +
                "\n}";
    }
}
