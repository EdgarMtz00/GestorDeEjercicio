package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CrearEjercicio extends AppCompatActivity {
    EditText txtNombre, txtRepeticiones;
    Spinner spinDias, spinZona;
    private String dia;
    int zona;
    String url;
    String  id;
    Context ctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ejercicio);
        txtNombre = findViewById(R.id.txtNombre);
        txtRepeticiones = findViewById(R.id.txtRepeticiones);
        spinDias = findViewById(R.id.spinDias);
        spinZona = findViewById(R.id.spinZona);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getString("userId", "-1");
        String ip = preferences.getString("ip", "");
        url = "http://" + ip + "/serverejercicio/ejercicios.php";
        spinDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDia(spinDias.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zona = position + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });
        Button btnCrear = findViewById(R.id.btnCrear);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject request = new JSONObject();
                try {
                    request.put("nombre", txtNombre.getText().toString());
                    request.put("repeticiones", txtRepeticiones.getText().toString());
                    request.put("idUsuario", id);
                    request.put("dia", dia);
                    request.put("zona",zona);

                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response.has("msg")){
                                Toast.makeText(ctx, "Ejercicio Añadido", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ctx, "No se pudo registrar el ejercicio", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, "No se pudo registrar el ejercicio", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(ctx);
                    rQueue.add(objectRequest);
                    rQueue.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public void setDia(String dia){
        this.dia = dia;
    }
}
