package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class ConexionAsync extends AsyncTask<Void, Integer, JSONObject> {
    JSONObject params, jsonResponse;
    String url;
    Context ctx;
    RequestQueue rQueue;
    JsonObjectRequest jsonRequest;
    boolean ready = false;

    public ConexionAsync(String url, JSONObject params, Context  ctx) {
        this.url = url;
        this.params = params;
        this.ctx = ctx;
        rQueue = Volley.newRequestQueue(ctx);

        jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonResponse = response;
                        ready = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonResponse = new JSONObject();
                        try {
                            jsonResponse.put("Error", "Error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        if(ready) {
            rQueue.add(jsonRequest);
            rQueue.start();
            return jsonResponse;
        }else{
            //Toast.makeText(ctx, "fac", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
