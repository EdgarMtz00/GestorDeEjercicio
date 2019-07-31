package com.example.agust.gestordeejercicio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;


public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    SharedPreferences preferences;
    PendingIntent pendingIntent;
    AlarmManager am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configuracion de la vista
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        //configuracion de preferencias
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip", "192.168.1.76");
        editor.apply();

        //Revisa si ya hay un usuario en las preferencias, si no abre la actividad de iniciar sesion
        String id = preferences.getString("userId", "-1");
        if (id.equals("-1")){
            Intent Sesion = new Intent(this, InicioSesion.class);
            startActivity(Sesion);
            finish();
        }

        //Configuracion de notificaciones
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //metodo para poder activar o desactivar las notificaciones desde otras actividadades
    public void notificaciones(boolean active){
        if(active) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }else{
            am.cancel(pendingIntent);
        }
    }

    //metodo para poder cambiar el intervalo de las las notificaciones desde otras actividadades
    public  void setInterval(long interval){
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }
}
