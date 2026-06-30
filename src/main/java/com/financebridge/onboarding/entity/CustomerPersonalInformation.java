package com.financebridge.onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "customerPersonalInformation")
public class CustomerPersonalInformation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable=true)
    private String bevorzugterName;

    @Column(nullable=false)
    private String nationalitaet;

    @Column(nullable=false)
    private LocalDate geburtsDatum;

    @Column(nullable=false)
    private String telefonnummer;

    @Column(nullable=false)
    private String ausweisTyp;

    @Column(nullable=false)
    private String ausweisNummer;
}
