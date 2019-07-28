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
    EditText txtNombre, txtRepeticiones; //Campos de nombre y repeticiones a realizar
    Spinner spinDias; //Opciones de que dias se realizara el ejercicio
    private String dia; //dia seleccionado
    String url;
    Long id;
    Context ctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ejercicio);
        txtNombre = findViewById(R.id.txtNombre);
        txtRepeticiones = findViewById(R.id.txtRepeticiones);
        spinDias = findViewById(R.id.spinDias);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getLong("userId", -1);
        String ip = preferences.getString("ip", "");
        url = "http://" + ip + "/serverejercicio/ejercicios.php"; //url de la API
        Button btnCrear = findViewById(R.id.btnCrear);

        /**
         * cuando se selecciona un dia en spinDias se llama a setDia para guardarlo
         */
        spinDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDia(spinDias.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Evento para guardar el ejercicio creado
         */
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject request = new JSONObject();
                try {
                    //Se toman los datos ingresados y el id del usuario
                    request.put("nombre", txtNombre.getText().toString());
                    request.put("repeticiones", txtRepeticiones.getText().toString())
                    request.put("idUsuario", String.valueOf(id));
                    request.put("dia", dia);

                    //peticion a la API para almcenar los cambios
                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response.has("msg")) {//si se obtiene una respuesta
                                Toast.makeText(ctx, "Ejercicio Añadido", Toast.LENGTH_SHORT).show();
                                finish();//termina la actividad
                            } else {
                                Toast.makeText(ctx, "No se pudo registrar el ejercicio", Toast.LENGTH_SHORT).show(); //mensaje de error en la respuesta
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, "No se pudo registrar el ejercicio", Toast.LENGTH_SHORT).show(); //mensaje de error en la peticion
                        }
                    });

                    //inicia la peticion
                    RequestQueue rQueue = Volley.newRequestQueue(ctx);
                    rQueue.add(objectRequest);
                    rQueue.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    //Guarda el dia elegido
    public void setDia(String dia){
        this.dia = dia;
    }
}
