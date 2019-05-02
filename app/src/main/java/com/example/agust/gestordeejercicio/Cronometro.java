package com.example.agust.gestordeejercicio;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    TextView txtDistancia;
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
        View v = inflater.inflate(R.layout.fragment_cronometro, container, false);
        txtDistancia = v.findViewById(R.id.txtDistancia);
        btnInicio = v.findViewById(R.id.btnInicio);
        btnPausa = v.findViewById(R.id.btnPausa);
        final Chronometer crono = v.findViewById(R.id.crono);
        prgReloj = v.findViewById(R.id.prgReloj);
        clickInicio = true;
        clickPausa = true;
        prgReloj.setMax(60); //Establece 60 como el valor maximo de prgReloj

/*        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
         }*/

        /*
            Evento activado cuando se presiona btnInicio
            inicia o detine el cronometro  y el progreso de prgReloj
            basado en el valor de click
         */
        btnInicio.setOnClickListener(new View.OnClickListener() {
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

        /*
            Evento activado cuando se presiona btnPausa
            pausa o reanuda el cronometro  y el progreso de prgReloj
            basado en el valor de clickPausa
         */
        btnPausa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!clickInicio)
                {
                    Intent intent = new Intent(getActivity(), GpsService.class);
                    getActivity().startService(intent);
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String distance = intent.getStringExtra("distance");
            txtDistancia.setText("Distance is " + distance+" M" );
        }
    };

}
