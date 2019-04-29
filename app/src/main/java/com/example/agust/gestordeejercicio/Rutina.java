package com.example.agust.gestordeejercicio;

public class Rutina {
     private Ejercicio ejercicio;
     private String dia;
     private int id;
     private String repeticiones;

    public Rutina() {
        id = -1;
    }

    public Rutina(Ejercicio ejercicio, String dia, int id, String repeticiones) {
        this.ejercicio = ejercicio;
        this.dia = dia;
        this.id = id;
        this.repeticiones = repeticiones;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(String repeticiones) {
        this.repeticiones = repeticiones;
    }
}
