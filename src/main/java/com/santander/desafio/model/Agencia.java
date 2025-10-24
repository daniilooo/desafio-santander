package com.santander.desafio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agencias")
@Getter @Setter
public class Agencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double posX;

    @NotNull
    private Double posY;

    @Column(nullable = false)
    private String nome;
}
