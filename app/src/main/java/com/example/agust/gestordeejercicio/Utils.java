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

    public JSONObject getJsonObject() {
        rQueue.start();
        return jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    Utils(Context context){
        rQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    void requestJson(String url, JSONObject params){
        jsonObejctRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("Error", "Error");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        rQueue.add(jsonObejctRequest);
    }

    private void processResponse(JSONObject response){
        jsonObject = response;
    }

    JSONArray getJsonArray(String url, JSONArray params){
        jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                jsonArray = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonArray = new JSONArray();
                jsonArray.put("Error");
            }
        });
        rQueue.add(jsonArrayRequest);
        rQueue.start();
        return jsonArray;
    }

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
