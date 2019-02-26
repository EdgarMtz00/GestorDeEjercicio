package com.example.agust.gestordeejercicio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Configuracion extends AppCompatActivity {
    Intent origen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        origen = getIntent();
    }

    void onClickSalir(View v){
        super.finish();
    }
}
