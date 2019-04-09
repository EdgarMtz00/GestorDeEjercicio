package com.example.agust.gestordeejercicio;

public class Ejercicios {
    private String nombre, dificultad, dia, descripcion;
    private int repeticiones;

    public Ejercicios(String nombre, String dificultad, String dia, String descripcion, int repeticiones) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.dia = dia;
        this.descripcion = descripcion;
        this.repeticiones = repeticiones;
    }

    public Ejercicios() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }
}
