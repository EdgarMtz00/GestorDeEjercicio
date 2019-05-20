package com.example.agust.gestordeejercicio;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ContadorRepeticiones extends Activity implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float vibrateThreshold = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

    private boolean flag = false;

    private Handler handler;

    int interval= 500;

    int repeticiones = 0;

    //public Vibrator v;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_repeticiones);
        initializeViews();

        handler = new Handler();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            //vibrateThreshold = accelerometer.getMaximumRange() / 4;
            vibrateThreshold = 1.5f;
            Toast.makeText(this, "Threshold" + vibrateThreshold, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Sensor", Toast.LENGTH_SHORT).show();
            finish();
        }

        //initialize vibration
        //v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void initializeViews() {
        currentX = findViewById(R.id.currentX);
        currentY = findViewById(R.id.currentY);
        currentZ = findViewById(R.id.currentZ);

        maxX = findViewById(R.id.maxX);
        maxY = findViewById(R.id.maxY);
        maxZ = findViewById(R.id.maxZ);
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
        if(flag) {
            // clean current values
            displayCleanValues();
            // display the current x,y,z accelerometer values
            displayCurrentValues();
            // display the max x,y,z accelerometer values
            displayMaxValues();

            // get the change of the x,y,z values of the accelerometer
            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = Math.abs(lastY - event.values[1]);
            deltaZ = Math.abs(lastZ - event.values[2]);

            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];

            // if the change is below 2, it is just plain noise
            /*
            if (deltaX < 2)
                deltaX = 0;
            if (deltaY < 2)
                deltaY = 0;
            */
            if ((deltaX > vibrateThreshold)){ //|| (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
                repeticiones++;
                Log.d("myApp", "onSensorChanged: delta Z = " + deltaZ);
                Log.d("myApp", "onSensorChanged: delta x = " + deltaX);
                Log.d("myApp", "onSensorChanged: delta y = " + deltaY);
                Toast.makeText(this, "Threshold alcanzado" + repeticiones, Toast.LENGTH_SHORT).show();
            }
            flag = false;
        }
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }

    // display the max x,y,z accelerometer values
    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
    }
}
