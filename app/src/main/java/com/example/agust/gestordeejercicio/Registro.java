package com.example.agust.gestordeejercicio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registro extends AppCompatActivity {
    EditText etContrasena, etCorreo, etPwd, etEstatura, etPeso, etEdad;
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
    }

    public void onClickRegistro(View v){

    }

    public void onClickFacebook(View v){

    }

    public void help(View v){

    }

    public void terminos(View v){

    }
}
