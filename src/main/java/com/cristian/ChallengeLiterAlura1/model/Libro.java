package com.cristian.ChallengeLiterAlura1.model;


import jakarta.persistence.*;
import lombok.Getter;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
@Getter
@Setter

public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String titulo;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idiomas;
    private Integer cantidadDescargas;
    private String nombreAutor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro(DatosLibro datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        this.nombreAutor = datosLibro.autor().stream().map(DatosAutor::nombre).collect(Collectors.toList()).toString();
        this.idiomas = datosLibro.idiomas();
        this.cantidadDescargas = datosLibro.cantidadDescargas();
        this.autor = autor;
    }

    public Libro() {
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
        if (autor != null && !autor.getLibros().contains(this)) {
            autor.getLibros().add(this);
        }
    }

    @Override
    public String toString() {
        return "\n************* LIBRO *************************"
                + "\nLibro numero: " + id
                + "\ntitulo='" + titulo + '\''
                + "\ncantidad Descargas=" + cantidadDescargas
                + "\nautores=" + autor
                + "\nlenguaje=" + idiomas

                + "\n******************************************";
    }
}
