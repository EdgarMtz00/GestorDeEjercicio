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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Handler handler; //Maneja el tiempo en el que se ejecuta setFlag
    int repeticiones = 0;
    int MaxRep = 0;
    private LinearLayout Contador, Ingresar;
    final Intent result = new Intent();


    private final Runnable setFlag = new Runnable() {
        @Override
        public void run() {
            flag = true;
            handler.postDelayed(this, interval);//cada .670 milisegundos se vuelve a ejecutar
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
        Ingresar = findViewById(R.id.Ingresar);
        Contador = findViewById(R.id.Contador);
        txtNombre.setText(intent.getStringExtra("Nombre"));
        txtInstruccion.setText(intent.getStringExtra("Instruccion"));
        txtRepeticiones.setText("0");
        MaxRep = Integer.parseInt(String.valueOf(intent.getStringExtra("Repeticiones")));
        handler = new Handler();
        result.putExtra("Id", intent.getIntExtra("Id", -1));

        if(!intent.getStringExtra("Instruccion").equals("Realice el ejercicio")) {//si se esta realizando un ejercicio de la aplicacion
            Contador.setVisibility(View.VISIBLE);
            Ingresar.setVisibility(View.GONE);
            //evento activado cuando se toca la pantalla
            findViewById(R.id.activity).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start = !start; //la primera vez hace start verdadero
                    if (!start) { // la segunda vez termina la actividad
                        vibrar.vibrate(500);
                        result.putExtra("repeticiones", Integer.parseInt(txtRepeticiones.getText().toString()));
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }
                }
            });
            //Registra el acelerometro  en el administrador de sensores de android
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) { //revisa si tenemos acelerometro
                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "No Sensor", Toast.LENGTH_SHORT).show();//si no hay acelerometro informa al usuario y termina sin resultado
                finish();
            }
        }else{ //si se esta realizando un ejercicio agregado por el usuario
            Ingresar.setVisibility(View.VISIBLE);
            Contador.setVisibility(View.GONE);
            Button btnIngresar = findViewById(R.id.btnIngresar);//se muestra un campo de texto y un boton de ingersar
            btnIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText txtIngresar = findViewById(R.id.txtIngresar);
                    //el usuario ingresa sus repeticiones manualmente y se termina la actividad
                    result.putExtra("repeticiones", Integer.parseInt(txtIngresar.getText().toString()));
                    result.putExtra("ingreser", true);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            });
        }
    }

    //onResume() volver a usar el aceleromtro
    protected void onResume() {
        super.onResume();
        if(!intent.getStringExtra("Instruccion").equals("Realice el ejercicio")) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            handler.post(setFlag);
        }
    }

    //onPause() dejar de usar el acelerometro
    protected void onPause() {
        super.onPause();
        if(!intent.getStringExtra("Instruccion").equals("Realice el ejercicio")) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(flag && start) { // flag es activada por el timer para tener un intervalo entre mediciones
            //start se activa cuando el usuario toca la pantalla
            // cambio de aceleracion en cada vector
            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = Math.abs(lastY - event.values[1]);
            deltaZ = Math.abs(lastZ - event.values[2]);
            // nueva aceleracion en cada vector
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];

            //si el cambio de aceleracion supera el valor minimo
            if ((deltaX > vibrateThreshold) || (deltaZ > vibrateThreshold) && (deltaY < vibrateThreshold)) {
                repeticiones++; //se cuenta cada movimiento
                Log.d("myApp", "onSensorChanged: delta Z = " + deltaZ);
                Log.d("myApp", "onSensorChanged: delta x = " + deltaX);
                Log.d("myApp", "onSensorChanged: delta y = " + deltaY);
                if(repeticiones % 2 == 0) { // y se suma una repeticion por cada dos movimientos (arriba y abajo)
                    txtRepeticiones.setText(String.valueOf(repeticiones/2));
                }
                if (repeticiones == MaxRep * 2) { // una vez alcanzado el numero de repeticiones a realizar
                    vibrar.vibrate(500);
                    result.putExtra("repeticiones", repeticiones/2); // se termina la actividad regresando las repeticiones hechas
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            }
            flag = false;
        }
    }
}
