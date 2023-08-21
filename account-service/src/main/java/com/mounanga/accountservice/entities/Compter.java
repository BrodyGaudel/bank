package com.mounanga.accountservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Compter {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    public Compter(){
        super();
    }

    public Compter(Long id) {
        this.id = id;
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
