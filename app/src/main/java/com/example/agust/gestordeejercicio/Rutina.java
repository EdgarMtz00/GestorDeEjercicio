package com.example.agust.gestordeejercicio;

/**
 * Objeto modelo de las rutinas de dadas por la API
 */
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

    /**
     * Funciones para obtener y establecer los datos de la rutina
     */

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
