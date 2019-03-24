package com.example.agust.gestordeejercicio;

import android.os.AsyncTask;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class ConexionAsync extends AsyncTask<Void, Integer, String[]> {
    JSONObject params;
    WeakReference[] campos;
    JsonObjectRequest jsonRequest = new JsonObjectRequest();

    @Override
    protected String[] doInBackground(Void... voids) {
        return new String[0];
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
