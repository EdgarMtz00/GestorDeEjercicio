package com.example.agust.gestordeejercicio;

public class Ejercicio {
    private int id;
    private String nombre;
    private String instruccion;
    private  int dificultad;

    public Ejercicio() {
        id = -1;
    }

    public Ejercicio(int id, String nombre, String instruccion, int dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.instruccion = instruccion;
        this.dificultad = dificultad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }
}
