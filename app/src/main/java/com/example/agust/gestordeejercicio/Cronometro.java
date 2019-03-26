package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;

import java.sql.Time;

/**
 * Cronometro: Clase Controladora de las funciones y vistas del cronometro.
 * btnPausa: Boton para iniciar y detener el cronometro.
 * click: Alterna de estado cada que se presiona btnPausa para cambiar entre iniciar y detener el cronometro.
 * prgReloj: Barra de progreso circular que muestra el avance del cronometro.
 * progreso: Segundas transcurridos desde que se inicio el cronometro
 */

public class Cronometro extends Fragment {
    boolean click;
    ProgressBar prgReloj;
    int progreso = 0;
    Button btnPausa;

    /**
     * timerProgress: Objeto para realizar conteo de 60 segundos realizando una accion cada segundo
     * onTick: Cada segundo se aumenta la variable progreso y se actualiza prgReloj con el nuevo valor
     * onFinish: Reinicia el conteo al acabar
     */
    CountDownTimer timerProgress = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            progreso++;
            prgReloj.setProgress(progreso);
        }

        @Override
        public void onFinish() {
            progreso = 0;
            prgReloj.setProgress(progreso);
            timerProgress.start();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cronometro, container, false);
        btnPausa = v.findViewById(R.id.btnPausa);
        final Chronometer crono = v.findViewById(R.id.crono);
        prgReloj = v.findViewById(R.id.prgReloj);
        click = true;
        prgReloj.setMax(60); //Establece 60 como el valor maximo de prgReloj

        btnPausa.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick: basado en el valor de click inicia o detiene el cronometro y el metodo timerProgress
             */
            @Override
            public void onClick(View v) {
                if(click){
                    crono.setBase(SystemClock.elapsedRealtime());
                    crono.start();
                    timerProgress.start();
                }else{
                    crono.stop();
                    timerProgress.cancel();
                    progreso = 0;
                    prgReloj.setProgress(progreso);
                    crono.setBase(SystemClock.elapsedRealtime());
                }
                click = !click;
            }
        });
        return v;
    }
}
