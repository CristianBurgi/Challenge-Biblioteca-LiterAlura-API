package com.cristian.ChallengeLiterAlura1.principal;

import com.cristian.ChallengeLiterAlura1.model.*;
import com.cristian.ChallengeLiterAlura1.model.enums.Lenguaje;
import com.cristian.ChallengeLiterAlura1.repository.IRepositorioAutor;
import com.cristian.ChallengeLiterAlura1.repository.IRepositorioLibro;
import com.cristian.ChallengeLiterAlura1.service.ConsumoAPI;
import com.cristian.ChallengeLiterAlura1.service.ConvierteDatos;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class Principal {

    private static final String URL = "https://gutendex.com/books/";
    private final String NOMBRE = "/?search=";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Integer opcion = -1;
    private Scanner leer = new Scanner(System.in);
    private IRepositorioLibro libroRepository;
    private IRepositorioAutor autorRepository;
    private List<Libro> libros;
    private Autor autor = new Autor();
    private List<Autor> autors;

    public Principal() {
    }

    public Principal(IRepositorioLibro libroRepository, IRepositorioAutor autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    // Métodos a usar
    private void infoMenu() {
        var menu = """
                ----------------------------------------
                    MENU:
                                    
                1 - Buscar libros por titulo
                2 - Mostrar libros registrados
                3 - Mostrar autores registrados
                4 - Mostrar autores vivos en determinado año
                5 - Mostrar libros por idiomas
                               
                    
                0 - Salir
                -----------------------------------------
                """;
        System.out.println(menu);
    }

    private void ingreseNumeros() {
        while (!leer.hasNextInt()) {
            System.out.println("ingrese solo numeros");
            leer.next();
        }
    }

    //--------------------------------------------------------------------------
    public void menu() {
        int opcion = -1;

        while (opcion != 0) {
            infoMenu();
            ingreseNumeros();
            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibrosBD();
                    break;
                case 3:
                    mostrarAutoresBD();
                    break;
                case 4:
                    buscarAutoresVivos();
                    break;
                case 5:
                    buscarPorIdiomas();
                    break;

                case 0:
                    System.out.println("Terminando Aplicación");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción Invalida");

            }
        }
    }


    private void buscarLibro() {
        System.out.println("Por favor Ingrese el nombre del Libro a Buscar...");
        var nombreLibro = leer.nextLine();
        var json = consumoAPI.obtenerDatos(URL + NOMBRE + nombreLibro.replace(" ", "+"));
        var convertido = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroBuscado = convertido.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        //Si Encuentra el libro lo registra, si 3no informa que no encontró.
        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();
            DatosAutor datosAutor = datosLibro.autor().get(0);
            Autor autor = autorRepository.findByNombre(datosAutor.nombre());

            //verifica que autor esté registrado en BD
            if (autor == null) {
                autor = new Autor(datosAutor);
                autorRepository.save(autor);
            }
            //verifica que el Libro esté registrado en la BD
            Libro libro = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());
            if (libro == null) {
                System.out.println("Encontré el libro");
                libro = new Libro(datosLibro, autor);
                libroRepository.save(libro);
                System.out.println(libro);
            } else {
                System.out.println("El libro ya se encuentra Registrado.");
            }
        } else {
            System.out.println("No se encontró el libro que busca");
        }
    }

    private void mostrarLibrosBD() {
        libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }


    private void mostrarAutoresBD() {

        autors = autorRepository.findAll();
        autors.forEach(System.out::println);

    }

    private void buscarAutoresVivos() {
        System.out.println("Ingrese año");
        ingreseNumeros();
        var anio = leer.nextInt();
        autors = autorRepository.autoresVivosEnDeterminadoAnio(anio);
        if (autor == null) {
            System.out.println("No hay registro de autores en ese año");
        } else {
            autors.forEach(System.out::println);
        }
    }

    private void buscarPorIdiomas() {
        System.out.println("""
        --------------------------------
        Ingrese numero de idioma deseado
        
        1- Ingles
        2- Español
        3- Portuges
        4- Italiano
        
        -------------------------------
        """);
        ingreseNumeros();
        var  numero = leer.nextInt();
        switch (numero) {
            case 1:
                buscarIdioma("en");
                break;
            case 2:
                buscarIdioma("es");
                break;
            case 3:
                buscarIdioma("pt");
                break;
            case 4:
                buscarIdioma("it");
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void buscarIdioma(String idioma) {
        try {
            libros = libroRepository.findByIdiomas(idioma);

            if (libros == null) {
                System.out.println("No hay Libros registrados");
            } else {
                libros.forEach(System.out::println);
            }
        }catch (Exception e){
            System.out.println("Error en la busqueda");
        }

    }
}
