package com.example.agust.gestordeejercicio;


import android.content.Context;

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

    Utils(Context context){
        rQueue = Volley.newRequestQueue(context);
    }

    JSONObject getJson(String url){
        jsonObejctRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonObject = response;
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
        rQueue.start();
        return jsonObject;
    }

    JSONArray getJsonArray(String url){
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
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
}
