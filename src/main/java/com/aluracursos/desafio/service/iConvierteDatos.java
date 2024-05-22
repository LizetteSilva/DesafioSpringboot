package com.aluracursos.desafio.service;

public interface iConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
