package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Registro extends AppCompatActivity {
    //Campos de informacion del usuario
    EditText etContrasena, etCorreo, etPwd, etEstatura, etPeso, etEdad;
    SharedPreferences preferences; //preferencias de la aplicacion
    String url;
    Context ctx = this;
    Spinner spinNivel;
    int nivel = 1;
    String enfoque = "";
    Spinner spinTipo;
    List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        etContrasena = findViewById(R.id.etContrasena);
        etCorreo = findViewById(R.id.etCorreo);
        etPwd = findViewById(R.id.etPwd);
        etEstatura = findViewById(R.id.etEstatura);
        etPeso = findViewById(R.id.etPeso);
        etEdad = findViewById(R.id.etEdad);
        spinTipo = (Spinner)findViewById(R.id.spinTipo);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ip = preferences.getString("ip", "");
        url = "http://" + ip + "/ServerEjercicio/Registrar.php"; //URL de la API
        spinNivel = findViewById(R.id.spinNivel);
        list.add("acondicionamiento");

        final ArrayAdapter spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTipo.setAdapter(spinAdapter);

        Button btnSalir = findViewById(R.id.btnSalir);

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Evento para cuando se selecciona un nivel
        spinNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nivel = position + 1;//el nivel se representa del 1 al 3 por lo que solo se toma la posicion seleccionada
                if(nivel == 1)
                {
                    spinAdapter.clear();
                    spinAdapter.add("Ninguno");
                    spinAdapter.add("Acondicionamiento");
                    spinAdapter.notifyDataSetChanged();
                }else{
                    spinAdapter.clear();
                    spinAdapter.add("Ninguno");
                    spinAdapter.add("Tonificar");
                    spinAdapter.add("Aumentar musculatura");
                    spinAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enfoque = spinTipo.getSelectedItem().toString();
                if(enfoque == "Ninguno"){
                    enfoque = "";
                }else if(enfoque == "Aumentar musculatura"){
                    enfoque = "musculatura";
                }else {
                    enfoque = enfoque.toLowerCase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Evento accionado cuando se presiona el boton de registrar
     * obtiene los datos ingresados en las campos
     * realiza una peticion a la API para crear el usuario
     * e inicia sesion en la aplicacion
     * @param v
     */
    public void onClickRegistro(View v){
        String correo, pwd, estatura, edad, peso;

        correo = etCorreo.getText().toString();
        pwd = etContrasena.getText().toString();
        edad = etEdad.getText().toString();
        estatura = etEstatura.getText().toString();
        peso = etPeso.getText().toString();

        if (Integer.parseInt(estatura) < 150 || Integer.parseInt(estatura) > 230){
            Toast.makeText(ctx, "Estatura fuera de los límites de la aplicación", Toast.LENGTH_SHORT).show();
            return;
        }else if(Integer.parseInt(edad) < 15){
            Toast.makeText(ctx, "Tienes que ser mayor de 15 años para usar la aplicación", Toast.LENGTH_SHORT).show();
            return;
        }else if(Integer.parseInt(edad) > 60){
            Toast.makeText(ctx, "Por tu seguridad debes de ser menor de 60 años para usar la aplicación", Toast.LENGTH_SHORT).show();
            return;
        }else if(Integer.parseInt(peso) < 45 || Integer.parseInt(peso) > 250){
            Toast.makeText(ctx, "Peso invalido", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etContrasena.getText().toString().equals(etPwd.getText().toString())) {
            //se recolecta la informacion ingresada
            String[] input ={correo, pwd, edad, estatura, peso};
            if(Utils.validateText(input)) {//si es validada
                if(Utils.validateEmail(correo)) {
                    JSONObject data = new JSONObject();
                    try {
                        //se almacena en el json que se enviara en la peticion
                        data.put("correo", correo);
                        data.put("contrasena", pwd);
                        data.put("edad", Integer.parseInt(edad));
                        data.put("peso", Integer.parseInt(peso));
                        data.put("estatura", Integer.parseInt(estatura));
                        data.put("nivel", nivel);
                        data.put("enfoque", enfoque);

                        //peticion para registrar un usuario
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        logIn(response); //si no falla la peticion procede a iniciar la sesion recien creada
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });

                        //inicia la peticion de registrar
                        RequestQueue rQueue = Volley.newRequestQueue(this);
                        rQueue.add(jsonRequest);
                        rQueue.start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                    Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "LLene todos los campos", Toast.LENGTH_SHORT).show();
            }
        }else{
            //Datos no validos
            Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
        }
    }


    public void help(View v){
        Intent Info = new Intent(this, Informacion.class);
        startActivity(Info);
    }


    /**
     * Inicia sesion con el id del usuario recien creado
     */
    public void logIn(JSONObject data){
        try {
            //guarda el id y nivel del usuario en las preferencias
            Toast.makeText(this, "Registrado" + data.getString("msg"), Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userId", data.getString("id"));
            editor.putInt("nivel", data.getInt("nivel"));
            editor.apply();

            //inicia la actividad principal
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
