package com.aluracursos.desafio.principal;

import com.aluracursos.desafio.model.Datos;
import com.aluracursos.desafio.model.DatosLibros;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        //Top 10 libros mas descargados
        System.out.println("=============================");
        System.out.println("Top 10 libros mas descargados");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
        System.out.println("=============================");

        //Busqueda de libro por nombre
        System.out.println("Ingresa un pedazo del nombre del libro");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado");
            System.out.println("Los datos son: " + libroBuscado.get());
        }else {
            System.out.println("No se encontro el libro");
        }
        System.out.println("=============================");

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datosBusqueda.resultados().stream()
                .filter(d -> d.numeroDeDescargas() > 0.0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("Media de descargas: " + est.getAverage());
        System.out.println("Numero mayor de descargas: " + est.getMax());
        System.out.println("Numero menor de descargas: " + est.getMin());
        System.out.println("Numero de registros contados:"+ est.getCount());
    }
}

