package com.example.agust.gestordeejercicio;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class CrearRutina extends AppCompatActivity {
    ListView lvEjercicios;
    ListAdapter listAdapter;
    TextView txtDia;
    static String[] dias = {"lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"};
    Button btnSig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);
        lvEjercicios = findViewById(R.id.lvEjercicios);
        txtDia = findViewById(R.id.txtDia);
        txtDia.setText(dias[0]);
        btnSig = findViewById(R.id.btnSig);
        listAdapter = new ListAdapter(this);
        final Context ctx = this;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String ip = preferences.getString("ip", "");
        String url ="http://" + ip + "/serverejercicio/ejercicios.php";
        JSONArray jsonArray = new JSONArray();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        listAdapter.setEjercicios(jsonArray);
                        lvEjercicios.setAdapter(listAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(arrayRequest);
        rQueue.start();

        lvEjercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.GREEN);
                Toast.makeText(ctx, "hola", Toast.LENGTH_SHORT).show();
            }
        });

        btnSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray rutina = new JSONArray();
                for (int i = 0; i < listAdapter.getCount(); i++){
                    Ejercicio ejercicio = (Ejercicio) listAdapter.getItem(i);
                    if(ejercicio.getRepeticiones() > 0) {
                        int idUsuario = preferences.getInt("userID", -1);
                        String dia = txtDia.getText().toString();
                        try {
                            rutina.put(ejercicio.JsonParse(idUsuario, dia));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                String url ="http://192.168.1.73/serverejercicio/rutinas.php";
                JsonArrayRequest postRutina = new JsonArrayRequest(Request.Method.POST, url, rutina,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                   });
                RequestQueue rQueue = Volley.newRequestQueue(ctx);
                rQueue.add(postRutina);
                rQueue.start();
                lvEjercicios.setAdapter(listAdapter);
            }
        });
    }
}
