package com.example.agust.gestordeejercicio;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
public class Cronometro extends Fragment implements SensorEventListener {
    boolean clickInicio, clickPausa;
    ProgressBar prgReloj;
    boolean correr;
    Switch swCorrer;
    int progreso = 0;
    long progresoTemp=0;
    Button btnInicio, btnPausa;
    SensorManager sManager;
    Sensor stepSensor;
    TextView txtDistancia, txtPasos, txtTiempo;
    private long steps = 0;
    private float distance;
    View v;

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
        v = inflater.inflate(R.layout.fragment_cronometro, container, false);
        swCorrer = v.findViewById(R.id.swCorrer);

        txtDistancia = v.findViewById(R.id.txtDistancia);
        txtPasos = v.findViewById(R.id.txtPasos);
        txtTiempo = v.findViewById(R.id.txtTiempo);
        btnInicio = v.findViewById(R.id.btnInicio);
        btnPausa = v.findViewById(R.id.btnPausa);
        final Chronometer crono = v.findViewById(R.id.crono);
        prgReloj = v.findViewById(R.id.prgReloj);
        prgReloj.setMax(60); //Establece 60 como el valor maximo de prgReloj
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        swCorrer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                correr = isChecked;
                if (correr) {
                    txtDistancia.setVisibility(View.VISIBLE);
                    txtPasos.setVisibility(View.VISIBLE);
                }else {
                    txtDistancia.setVisibility(View.GONE);
                    txtDistancia.setVisibility(View.GONE);
                }
            }
        });

        /*
            Evento activado cuando se presiona btnInicio
            inicia o detine el cronometro  y el progreso de prgReloj
            basado en el valor de click
         */
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInicio = !clickInicio;
                if(clickInicio){
                    txtTiempo.setVisibility(View.GONE);
                    crono.setBase(SystemClock.elapsedRealtime());
                    crono.start();
                    timerProgress.start();
                    btnInicio.setText("DETENER");
                    progreso--;
                    btnPausa.setVisibility(View.VISIBLE);
                }else{
                    crono.stop();
                    timerProgress.cancel();
                    guardarTiempo(progreso);
                    progreso = 0;
                    progresoTemp = 0;
                    prgReloj.setProgress(progreso);
                    crono.setBase(SystemClock.elapsedRealtime());
                    steps = 0;
                    distance = 0;
                    btnInicio.setText("INICIAR");
                    btnPausa.setVisibility(View.GONE);
                }
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
                    clickPausa = !clickPausa;
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
                }
            }
        });
        return v;
    }

    private void guardarTiempo(final int tiempo){
        JSONObject request = new JSONObject();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext()); //preferencias de la aplicacion
        Long id = preferences.getLong("userId", -1);
        String ip = preferences.getString("ip", "");
        final String url = "http://" + ip + "/ServerEjercicio/tiempo.php"; //URL de la API
        try {
            if (correr) {
                request.put("Correr", true);
            }
            request.put("tiempo", tiempo);
            request.put("idUsuario", id.toString());
            JsonObjectRequest setTiempo = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(response.has("tiempo")){
                        try {
                            txtTiempo.setText("Tiempo :" + tiempo + "\n tiempo anterior:"
                                    + response.getInt("tiempo") + "\n" +response.getInt("porcentaje")
                                    + "% en comparacion al recorrido anterior");
                            txtTiempo.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(getContext());
            rQueue.add(setTiempo);
            rQueue.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onStop() {
        super.onStop();
        sManager.unregisterListener(this, stepSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR && clickInicio && correr) {
            steps++;
            distance = (float)(steps*78)/(float)100;
            txtPasos.setText("Pasos:" + steps);
            txtDistancia.setText("Distancia: " + distance + "m");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
