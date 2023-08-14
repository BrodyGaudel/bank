package com.mounanga.customerservice.entities.buiders;

import com.mounanga.customerservice.entities.Customer;
import com.mounanga.customerservice.enums.Sex;

import java.util.Date;

public class CustomerBuilder {

    private final Customer customer = new Customer();

    public CustomerBuilder id(String id) {
        customer.setId(id);
        return this;
    }

    public CustomerBuilder firstname(String firstname) {
        customer.setFirstname(firstname);
        return this;
    }

    public CustomerBuilder name(String name) {
        customer.setName(name);
        return this;
    }

    public CustomerBuilder placeOfBirth(String placeOfBirth) {
        customer.setPlaceOfBirth(placeOfBirth);
        return this;
    }

    public CustomerBuilder dateOfBirth(Date dateOfBirth) {
        customer.setDateOfBirth(dateOfBirth);
        return this;
    }

    public CustomerBuilder nationality(String nationality) {
        customer.setNationality(nationality);
        return this;
    }

    public CustomerBuilder sex(Sex sex) {
        customer.setSex(sex);
        return this;
    }

    public CustomerBuilder cin(String cin) {
        customer.setCin(cin);
        return this;
    }

    public CustomerBuilder email(String email) {
        customer.setEmail(email);
        return this;
    }

    public CustomerBuilder phone(String phone) {
        customer.setPhone(phone);
        return this;
    }

    public CustomerBuilder lastUpdate(Date lastUpdate) {
        customer.setLastUpdate(lastUpdate);
        return this;
    }

    public Customer build() {
        customer.setCreation(new Date());
        return customer;
    }
}
