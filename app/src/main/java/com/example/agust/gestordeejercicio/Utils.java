package com.example.agust.gestordeejercicio;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class Utils {
    private JsonObjectRequest jsonObejctRequest;
    private JsonArrayRequest jsonArrayRequest;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private RequestQueue rQueue;
    private Context context;

    /**
     * validateText: comprueba que los strings de entrada no esten vacios
     * input: Un string o array de strings que seran validados
     * return: Verdadero si no hay strings vacios o falso en caso contrario
     */
    static boolean validateText(String[] input){
        for (String value : input){
            if(value.equals("")){
                return false;
            }
        }
        return true;
    }

    static boolean validateText(String input){
        return !input.equals("");
    }

    /**
     * validateEmail: Comprueba que un string tenga las caracteristicas de un email
     * email: String a validar
     * return: Verdadero si el string es un email o falso en el caso contrario
     */
    static boolean validateEmail(String email){
        return !email.equals("") && email.contains("@");
    }
}
