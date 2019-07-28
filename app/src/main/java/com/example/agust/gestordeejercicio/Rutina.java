package com.example.agust.gestordeejercicio;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Objeto modelo de las rutinas de dadas por la API
 */
public class Rutina {
     private Ejercicio ejercicio;
     private String dia;
     private int id;
     private String repeticiones;
     private String peso;
    public Rutina() {
        id = -1;
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

    /**
     * Toma un objeto json y lo convierte
     * a un objeto de rutina
     */
    public static Rutina rutinaParse(JSONObject j) throws JSONException {
        //Crea un objeto Rutina con la informacion recibida
        Rutina rutina = new Rutina();
        rutina.setEjercicio(new Ejercicio(0, j.getString("Nombre"),
                j.getString("Instrucciones"), j.getInt("Dificultad"),
                j.getString("Zona")));
        rutina.setDia(j.getString("Dia"));
        rutina.setRepeticiones(j.getString("Repeticiones"));
        rutina.setId(j.getInt("Id"));
        //dependiendo del valor de peso se agrega un texto diferente a la instruccion
        if (j.has("Peso")){
            String instruccion = rutina.getEjercicio().getInstruccion();
            switch (j.getInt("Peso")){
                case 1:
                     instruccion += "peso bajo";
                     break;
                case 2:
                    instruccion += "peso bajo";
                    break;
                case 3:
                    instruccion += "peso bajo";
                    break;
            }
            rutina.getEjercicio().setInstruccion(instruccion);
        }
        return rutina;
    }
}
