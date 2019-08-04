package com.example.agust.gestordeejercicio;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Informacion extends AppCompatActivity {

    TextView desc; //Texto donde se explica el nivel de dificultad
    Spinner spinNivel; //Selector del nivel de dificultad
    String[] descripciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        desc = findViewById(R.id.desc);
        spinNivel = findViewById(R.id.spinNivel);
        Resources res = getResources();
        descripciones = res.getStringArray(R.array.informacion_niveles); //Se cargan las descripciones desde el archivo de strings

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
                desc.setText(descripciones[position]); //Se muestra la descripcion correspondiente al nivel seleccionado
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
