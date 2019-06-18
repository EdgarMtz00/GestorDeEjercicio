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
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip", "192.168.1.76");
        editor.apply();
        if ((int)(preferences.getLong("userId", -1)) == -1){
            Intent Sesion = new Intent(this, InicioSesion.class);
            startActivity(Sesion);
            finish();
        }

        Intent intent = new Intent(MainActivity.this, Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void notificaciones(boolean active){
        if(active) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }else{
            am.cancel(pendingIntent);
        }
    }

    public  void setInterval(long interval){
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }
}
