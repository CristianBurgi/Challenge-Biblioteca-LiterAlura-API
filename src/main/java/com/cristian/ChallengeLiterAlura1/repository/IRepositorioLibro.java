package com.cristian.ChallengeLiterAlura1.repository;

import com.cristian.ChallengeLiterAlura1.model.Libro;
import com.cristian.ChallengeLiterAlura1.model.enums.Lenguaje;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface IRepositorioLibro extends JpaRepository<Libro,Long> {

    Libro findByTituloContainsIgnoreCase(String nombreLibro);

    List<Libro> findByIdiomas(String idioma);
}
