package com.example.agust.gestordeejercicio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class Registro extends AppCompatActivity {
    EditText etContrasena, etCorreo, etPwd, etEstatura, etPeso, etEdad;
    SharedPreferences preferences;
    String url = "http://192.168.1.69/ServerEjercicio/Registrar.php";
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
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void onClickRegistro(View v){
        String correo, pwd,estatura, edad, peso;
        if(etContrasena.getText().toString().equals(etPwd.getText().toString())) {
            correo = etCorreo.getText().toString();
            pwd = etContrasena.getText().toString();
            edad = etEdad.getText().toString();
            estatura = etEstatura.getText().toString();
            peso = etPeso.getText().toString();
            String[] input ={correo, pwd, edad, estatura, peso};

            if(Utils.validateText(input)) {
                JSONObject data = new JSONObject();
                try {
                    data.put("correo", correo);
                    data.put("pwd", pwd);
                    data.put("edad", Integer.parseInt(edad));
                    data.put("peso", Integer.parseInt(peso));
                    data.put("estatura", Integer.parseInt(estatura));

                    Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    logIn(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    RequestQueue rQueue = Volley.newRequestQueue(this);
                    rQueue.add(jsonRequest);
                    rQueue.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFacebook(View v){

    }

    public void help(View v){

    }

    public void terminos(View v){

    }

    public void logIn(JSONObject data){
        try {
            Toast.makeText(this, "Registrado" + data.getString("msg"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLogged", true);
        editor.apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
