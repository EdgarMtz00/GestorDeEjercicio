package com.example.agust.gestordeejercicio;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
    ListView lvEjercicios;//Lista de elementos
    ListAdapter listAdapter;//controlador de la lista
    TextView txtDia;//Dia al que se agrega la rutina
    int numDia = 0;
    static String[] dias = {"lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"};
    Button btnSig;
    JSONArray ejercicios; // ejercicios soportados por la api
    Spinner spinZonas; // Selector de filtro de zona

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);
        lvEjercicios = findViewById(R.id.lvEjercicios);
        txtDia = findViewById(R.id.txtDia);
        txtDia.setText(dias[numDia]);
        btnSig = findViewById(R.id.btnSig);
        spinZonas = findViewById(R.id.spinZonas);
        listAdapter = new ListAdapter(this);
        final Context ctx = this;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        final String ip = preferences.getString("ip", "");
        String url ="http://" + ip + "/serverejercicio/ejercicios.php?idUsuario=" + preferences.getString("userId", "-1");
        Button btnCrear = findViewById(R.id.btnCrear);

        //Evento parainiciar la actividad de CrearEjercicio
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, CrearEjercicio.class));
            }
        });

        //Evento activado cuando se selecciona una zona
        spinZonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.zonaActiva = spinZonas.getSelectedItem().toString(); //desde el controlador se muestran solo los ejercicios de dicha zona
                lvEjercicios.setAdapter(listAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Peticion de ejercicios a la API
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        listAdapter.setEjercicios(jsonArray); //se cargan en el controlador
                        lvEjercicios.setAdapter(listAdapter); //se asocia la vista con el controlador
                        ejercicios = jsonArray;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        //Inicia la peticion de ejercicios
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(arrayRequest);
        rQueue.start();

        //Cuando se selecciona un ejercicio se marca de verde
        lvEjercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.GREEN);
                Toast.makeText(ctx, "hola", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        Evento de btnSig
         */
        btnSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray rutina = new JSONArray();
                for (int i = 0; i < listAdapter.getCount(); i++){ // recorre los ejercicios actuales
                    Ejercicio ejercicio = (Ejercicio) listAdapter.getItem(i);
                    if(ejercicio.getRepeticiones() > 0) { //si se le ha asignado un numero de repeticiones a alguno
                        String idUsuario = preferences.getString("userId", "-1");
                        String dia = txtDia.getText().toString();
                        try {
                            rutina.put(ejercicio.JsonParse(idUsuario, dia)); // se registra como parte de la rutina
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                String url ="http://" + ip + "/serverejercicio/rutinas.php";
                //Peticion para guardar la rutina
                JsonArrayRequest postRutina = new JsonArrayRequest(Request.Method.POST, url, rutina,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {//si la peticion es correcta
                            listAdapter.setEjercicios(ejercicios);
                            lvEjercicios.setAdapter(listAdapter);//se reinicia la lista de ejercicios
                            numDia++; // y avanza al siguiente dia
                            if(numDia < dias.length) {
                                txtDia.setText(dias[numDia]);
                            }else{//si ya recorrieron todos los dias
                                startActivity(new Intent(ctx, MainActivity.class));
                                finish(); // se termina la actividad
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, "error", Toast.LENGTH_SHORT).show(); //Mensaje de error en la peticion
                        }
                   });
                //inicia la peticion de la rutina
                RequestQueue rQueue = Volley.newRequestQueue(ctx);
                rQueue.add(postRutina);
                rQueue.start();
            }
        });
    }
}
