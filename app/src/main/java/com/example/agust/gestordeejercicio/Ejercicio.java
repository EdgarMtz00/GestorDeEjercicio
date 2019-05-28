package com.example.agust.gestordeejercicio;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Objeto modelo de los ejercicios de dados por la API
 */
public class Ejercicio {
    private int id;
    private String nombre;
    private String instruccion;
    private  int dificultad;
    private int repeticiones;

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Ejercicio() {
        id = -1;
    }

    public Ejercicio(int id, String nombre, String instruccion, int dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.instruccion = instruccion;
        this.dificultad = dificultad;
    }

    /**
     * Funciones para obtener y establecer los datos del ejercicio
     */

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

    public static Ejercicio ejercicioParse(JSONObject j) throws JSONException {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre(j.getString("Nombre"));
        ejercicio.setInstruccion(j.getString("Instrucciones"));
        ejercicio.setId(j.getInt("ID"));
        ejercicio.setDificultad(j.getInt("Dificultad"));
        return ejercicio;
    }

    public JSONObject JsonParse(Long usuario, String dia) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("idEjercicio", this.id);
        res.put("idUsuario",  String.valueOf(usuario));
        res.put("repeticiones", this.repeticiones);
        res.put("dia", dia);
        return res;
    }
}
