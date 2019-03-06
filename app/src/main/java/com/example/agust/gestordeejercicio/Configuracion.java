package com.example.agust.gestordeejercicio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Configuracion extends AppCompatActivity {
    EditText etPeso, etEdad, etEstatura;
    Button btnCambio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        etPeso = findViewById(R.id.peso);
        etEdad = findViewById(R.id.edad);
        etEstatura = findViewById(R.id.estatura);
        etPeso.addTextChangedListener(textWatcher);
        etEstatura.addTextChangedListener(textWatcher);
        etEdad.addTextChangedListener(textWatcher);
        btnCambio = findViewById(R.id.btnCambio);
    }

    public void onClickCambio(View v){

    }
    public void onClickSalir(View v){
        super.finish();
    }

    public void CerrarSesion(View v){

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            btnCambio.setVisibility(View.VISIBLE);
            if(s == etEdad.getEditableText()){

            }else if(s == etEstatura.getEditableText()){

            }else if(s == etPeso.getEditableText()){

            }
        }
    };

}
