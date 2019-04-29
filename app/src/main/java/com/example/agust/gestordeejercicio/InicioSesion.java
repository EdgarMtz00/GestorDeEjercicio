package com.example.agust.gestordeejercicio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    EditText etCorreo, etContrasena;
    Button btnInicio;
    String url = "http://192.168.1.86/ServerEjercicio/IniciarSesion.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        etContrasena = findViewById(R.id.etContrasena);
        etCorreo = findViewById(R.id.etCorreo);
        btnInicio = findViewById(R.id.btnInicio);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        btnInicio.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        String correo, contrasena;
        correo = etCorreo.getText().toString();
        contrasena = etContrasena.getText().toString();
        if (Utils.validateEmail(correo) && Utils.validateText(correo)){
            JSONObject data = new JSONObject();
            try {
                data.put("correo", correo);
                data.put("pwd", contrasena);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    if(jsonObject.getBoolean("logIn")){
                                        editor.putInt("userId", jsonObject.getInt("id"));
                                        logIn();
                                    }else{
                                        retry();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                RequestQueue rQueue = Volley.newRequestQueue(this);
                rQueue.add(jsonRequest);
                rQueue.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void logIn(){
        editor.apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void retry(){
        Toast.makeText(this, "Bad Credentials", Toast.LENGTH_SHORT).show();
    }

    public void onClickFacebook(View v){

    }

    public void onClickRegistro(View v){
        startActivity( new Intent(this, Registro.class));
    }
}
