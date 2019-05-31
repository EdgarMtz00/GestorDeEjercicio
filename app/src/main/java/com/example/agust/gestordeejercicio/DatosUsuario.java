package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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
    Spinner spinNivel;
    int nivel = 1;

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
        spinNivel = findViewById(R.id.spinNivel);

        spinNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nivel = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btnDatos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("edad", txtEdad.getText().toString());
                    jsonObject.put("estatura", txtEstatura.getText().toString());
                    jsonObject.put("peso", txtPeso.getText().toString());
                    jsonObject.put("id", String.valueOf(userid));
                    jsonObject.put("nivel", nivel);
                    jsonObject.put("facebook", true);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    editor.putLong("userId", userid);
                                    editor.apply();
                                    startActivity(new Intent(ctx, MainActivity.class));
                                    finish();
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
