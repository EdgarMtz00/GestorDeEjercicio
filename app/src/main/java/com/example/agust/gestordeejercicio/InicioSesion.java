package com.example.agust.gestordeejercicio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InicioSesion extends AppCompatActivity {

    EditText etCorreo, etContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        etContrasena = findViewById(R.id.etContrasena);
        etCorreo = findViewById(R.id.etCorreo);
    }

    public void onClickInicio(View v){
        String correo, contrasena;
        correo = etCorreo.getText().toString();
        contrasena = etContrasena.getText().toString();
        if (!correo.equals("") && !contrasena.equals("")){
            Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    public void onClickFacebook(View v){

    }

    public void onClickRegistro(View v){
        startActivity( new Intent(this, Registro.class));
    }
}
