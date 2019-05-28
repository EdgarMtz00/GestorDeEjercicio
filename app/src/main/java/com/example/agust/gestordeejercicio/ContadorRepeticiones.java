package com.example.agust.gestordeejercicio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ContadorRepeticiones extends Activity implements SensorEventListener {

    private float lastX, lastY, lastZ; //Ultima medicion del acelerometro obtenida
    private SensorManager sensorManager; //Clase que otorga acceso a los sensores del dispositivo
    private Sensor accelerometer; //Clase que maneja los datos del acelerometro
    public Vibrator vibrar; //Clase para controlar la vibracion del dispositivo
    private float deltaX = 0;
    private float deltaY = 0; //Almacenan la diferencia de aceleracion en cada eje
    private float deltaZ = 0;
    long interval = 670; //Cantidad de milisegundos a esperar entre mediciones del acelerometro
    private float vibrateThreshold = 1.5f; //Limite para no considerar la medicion como ruido del sensor
    private boolean start; //Indica si el usuario ha tocado la pantalla para empezar o detener la medicion
    private TextView txtRepeticiones, txtNombre, txtInstruccion; //Texto con la informacion del ejercicio
    private Intent intent;
    private boolean flag = false; //Se activa cuando ha pasado el tiempo suficiente para realizar otra medicion
    private Handler handler; //Maneja la 
    int repeticiones = 0;
    int MaxRep = 0;


    private final Runnable processSensors = new Runnable() {
        @Override
        public void run() {
            // Do work with the sensor values.
            flag = true;
            // The Runnable is posted to run again here:
            handler.postDelayed(this, interval);
        }
    };


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_repeticiones);
        intent = getIntent();
        vibrar = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        txtRepeticiones = findViewById(R.id.txtRepeticiones);
        txtInstruccion = findViewById(R.id.txtInstruccion);
        txtNombre = findViewById(R.id.txtNombre);
        txtNombre.setText(intent.getStringExtra("Nombre"));
        txtInstruccion.setText(intent.getStringExtra("Instruccion"));
        txtRepeticiones.setText("0");
        MaxRep = Integer.parseInt(String.valueOf(intent.getStringExtra("Repeticiones")));
        handler = new Handler();
        findViewById(R.id.activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = !start;
                if (!start){
                    vibrar.vibrate(500);
                    Intent result = new Intent();
                    result.putExtra("repeticiones", Integer.parseInt(txtRepeticiones.getText().toString()));
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            }
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "No Sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        handler.post(processSensors);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(flag && start) {

            // get the change of the x,y,z values of the accelerometer
            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = Math.abs(lastY - event.values[1]);
            deltaZ = Math.abs(lastZ - event.values[2]);

            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];

            if ((deltaX > vibrateThreshold) || (deltaZ > vibrateThreshold) && (deltaY < vibrateThreshold)) {
                repeticiones++;
                Log.d("myApp", "onSensorChanged: delta Z = " + deltaZ);
                Log.d("myApp", "onSensorChanged: delta x = " + deltaX);
                Log.d("myApp", "onSensorChanged: delta y = " + deltaY);
                if(repeticiones % 2 == 0) {
                    txtRepeticiones.setText(String.valueOf(repeticiones/2));
                }
                if (repeticiones == MaxRep * 2) {
                    vibrar.vibrate(500);
                    Intent result = new Intent();
                    result.putExtra("repeticiones", repeticiones);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            }
            flag = false;
        }
    }
}
