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
 * btnInicio: Boton para iniciar y detener el cronometro.
 * btnPausa: Boton para pausar y reanudar el cronometro.
 * clickInicio: Alterna de estado cada que se presiona btnInicio para cambiar entre iniciar y detener el cronómetro.
 * clickPausa: Alterna de estado cada que se presiona btnPausa para cambiar entre pausar y reanudar el cronómetro.
 * prgReloj: Barra de progreso circular que muestra el avance del cronómetro.
 * progreso: Segundos transcurridos desde que se inicio el cronómetro.
 * progresoTemp: Almacena el tiempo transcurrido desde que se inició el cronómetro hasta que se presionó btnPausa.
 */

public class Cronometro extends Fragment {
    boolean clickInicio, clickPausa;
    ProgressBar prgReloj;
    int progreso = 0;
    long progresoTemp=0;
    Button btnInicio, btnPausa;

    /**
     * timerProgress: Objeto para realizar conteo de 60 segundos realizando una accion cada segundo
     * onTick: Cada segundo se aumenta la variable progreso y se actualiza prgReloj con el nuevo valor
     * onFinish: Reinicia el conteo al acabar
     */
    CountDownTimer timerProgress = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if(progreso==60)
                progreso=0;
            progreso++;
            prgReloj.setProgress(progreso);
        }

        @Override
        public void onFinish() {
            prgReloj.setProgress(progreso);
            timerProgress.start();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cronometro, container, false);
        btnInicio = v.findViewById(R.id.btnInicio);
        btnPausa = v.findViewById(R.id.btnPausa);
        final Chronometer crono = v.findViewById(R.id.crono);
        prgReloj = v.findViewById(R.id.prgReloj);
        clickInicio = true;
        clickPausa = true;
        prgReloj.setMax(60); //Establece 60 como el valor maximo de prgReloj

        btnInicio.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick: basado en el valor de click inicia o detiene el cronometro y el metodo timerProgress
             */
            @Override
            public void onClick(View v) {
                if(clickInicio){
                    crono.setBase(SystemClock.elapsedRealtime());
                    crono.start();
                    timerProgress.start();
                    btnInicio.setText("DETENER");
                    progreso--;
                }else{
                    crono.stop();
                    timerProgress.cancel();
                    progreso = 0;
                    progresoTemp = 0;
                    prgReloj.setProgress(progreso);
                    crono.setBase(SystemClock.elapsedRealtime());
                    btnInicio.setText("INICIAR");
                }
                clickInicio = !clickInicio;
            }
        });

        btnPausa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!clickInicio)
                {
                    if(clickPausa){
                        progresoTemp = crono.getBase() - SystemClock.elapsedRealtime();
                        crono.stop();
                        timerProgress.cancel();
                        btnPausa.setText("REANUDAR");
                    }else{
                        crono.setBase(SystemClock.elapsedRealtime() + progresoTemp);
                        crono.start();
                        timerProgress.start();
                        btnPausa.setText("PAUSAR");
                        progreso--;
                    }
                    clickPausa = !clickPausa;
                }
            }
        });
        return v;
    }
}
