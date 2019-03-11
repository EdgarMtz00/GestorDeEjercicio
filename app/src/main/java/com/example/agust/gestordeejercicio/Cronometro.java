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

public class Cronometro extends Fragment {

    boolean click;
    ProgressBar prgReloj;
    int progreso;
    Handler mHandler = new Handler();

    final Thread prgThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(progreso <= 60){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        prgReloj.setProgress(progreso);
                    }
                });
                progreso++;
                SystemClock.sleep(1000);
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cronometro, container, false);
        Button btnPausa = v.findViewById(R.id.btnPausa);
        final Chronometer crono = v.findViewById(R.id.crono);
        prgReloj = v.findViewById(R.id.prgReloj);
        click = true;

        prgReloj.setMax(60);

        btnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click){
                    crono.start();
                    if(Thread.interrupted()){
                        synchronized (prgThread) {
                            prgThread.notify();
                        }
                    }else {
                        prgThread.start();
                    }
                }else{
                    crono.stop();
                    synchronized (prgThread){
                        try {
                            prgThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                click = !click;
            }
        });
        return v;
    }
}
