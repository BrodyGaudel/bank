package com.mounanga.accountservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Compter {
    @Id
    private Long id;

    public Compter(Long id) {
        this.id = id;
    }

    public Compter() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Compter{" +
                "id=" + id +
                '}';
    }
}
