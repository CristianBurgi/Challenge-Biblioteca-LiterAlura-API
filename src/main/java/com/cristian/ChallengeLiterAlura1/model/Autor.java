package com.cristian.ChallengeLiterAlura1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="autor")
@Getter
@Setter
public class Autor {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int fechaNacimiento;
    private int fechaMuerte;
    @ManyToMany(mappedBy = "autor", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Libro> libros = new HashSet<>();

    public Autor() {
    }

    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaMuerte = datosAutor.fechaMuerte();
    }

    public void eliminarLibro(Libro libro){
        libros.remove(libro);
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
        for (Libro libro : libros){
            libro.setAutor(this);
        }
    }

    @Override
    public String toString() {
        return
                "\nNombre='" + nombre + '\'' +
                "\nAñoNacimiento=" + fechaNacimiento +
                "\nAñoMuerte=" + fechaMuerte;

    }
}
