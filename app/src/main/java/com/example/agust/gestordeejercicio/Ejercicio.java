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
    private String zona;
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

    public Ejercicio(int id, String nombre, String instruccion, int dificultad, String zona) {
        this.id = id;
        this.nombre = nombre;
        this.instruccion = instruccion;
        this.dificultad = dificultad;
        this.zona = zona;
    }

    /**
     * Funciones para obtener y establecer los datos del ejercicio
     */
    public String getZona() {
        return zona;
    }
    public void setZona(String zona) {
        this.zona = zona;
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

    /**
     * @param j json obtenido de la API con la informacion de un ejercicio
     * @return objeto tipo ejercicio con esta informacion
     * @throws JSONException
     */
    public static Ejercicio ejercicioParse(JSONObject j) throws JSONException {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre(j.getString("Nombre"));
        ejercicio.setInstruccion(j.getString("Instrucciones"));
        ejercicio.setId(j.getInt("ID"));
        ejercicio.setDificultad(j.getInt("Dificultad"));
        ejercicio.setZona(j.getString("Zona"));
        return ejercicio;
    }

    /**
     * @param usuario
     * @param dia
     * @return JSON con la informacion de un objeto tipo Ejercicio
     * @throws JSONException
     */
    public JSONObject JsonParse(String usuario, String dia) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("idEjercicio", this.id);
        res.put("idUsuario",  String.valueOf(usuario));
        res.put("repeticiones", this.repeticiones);
        res.put("dia", dia);
        res.put("nombre", this.nombre);
        res.put("zona", this.zona);
        return res;
    }
}
