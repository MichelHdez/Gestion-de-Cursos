package com.gestioncursos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false)
    private String titulo;
    @Column(length = 256)
    private String descripcion;
    @Column(nullable = false)
    private int nivel;
    @Column(name = "estadoPublicacion")
    private boolean isPublicado;
}
