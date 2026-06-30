package com.financebridge.onboarding.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Anrede anrede;

    @Column(nullable=false)
    private String vorname;

    @Column(nullable=false)
    private String nachname;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType;

    public Customer() {}

    public Customer(String vorname,String nachname, String email, CustomerType customerType) {
        this.vorname = vorname;
        this.nachname=nachname;
        this.email = email;
        this.customerType=customerType;
    }

}
