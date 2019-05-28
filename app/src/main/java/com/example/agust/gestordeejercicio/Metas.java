package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Metas extends Fragment {
    Button btnCambiarMeta, btnMedir, btnPierna, btnBrazo, btnAbdomen;
    ConstraintLayout cambiarMeta, progresoMeta;
    View.OnClickListener elegirMeta;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = getView();

        btnCambiarMeta = v.findViewById(R.id.btnCambiarMeta);
        btnMedir = v.findViewById(R.id.btnMedirMeta);
        btnPierna = v.findViewById(R.id.btnPiernas);
        btnAbdomen = v.findViewById(R.id.btnAbdomen);
        btnBrazo = v.findViewById(R.id.btnBrazos);

        elegirMeta = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meta = ((Button)v).getText().toString();
            }
        };

        btnBrazo.setOnClickListener(elegirMeta);
        btnAbdomen.setOnClickListener(elegirMeta);
        btnPierna.setOnClickListener(elegirMeta);

        btnCambiarMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarMeta.setVisibility(View.VISIBLE);
                progresoMeta.setVisibility(View.GONE);
            }
        });
        return inflater.inflate(R.layout.fragment_metas, container, false);
    }
}
