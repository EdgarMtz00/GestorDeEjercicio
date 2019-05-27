package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DatosUsuario extends AppCompatActivity {
    TextView txtEdad, txtPeso, txtEstatura;
    Context ctx = this;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        txtEdad = findViewById(R.id.etEdad);
        txtEstatura = findViewById(R.id.etEstatura);
        txtPeso = findViewById(R.id.etPeso);
        Intent intent = getIntent();
        final long userid = intent.getLongExtra("id", -1);
        preferences = PreferenceManager.getDefaultSharedPreferences(this); //preferencias de la aplicacion
        editor = preferences.edit();
        String ip = preferences.getString("ip", "");
        final String url = "http://" + ip + "/ServerEjercicio/Registrar.php"; //URL de la API
        findViewById(R.id.btnDatos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("edad", txtEdad.getText().toString());
                    jsonObject.put("estatura", txtEstatura.getText().toString());
                    jsonObject.put("peso", txtPeso.getText().toString());
                    jsonObject.put("id", userid);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    editor.putLong("userId", userid);
                                    startActivity(new Intent(ctx, MainActivity.class));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    RequestQueue rQueue = Volley.newRequestQueue(ctx);
                    rQueue.add(jsonRequest);
                    rQueue.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}