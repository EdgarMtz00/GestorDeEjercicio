package com.example.agust.gestordeejercicio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Method;

public class Metas extends Fragment {
    Button btnCambiarMeta, btnMedir, btnPierna, btnBrazo, btnAbdomen;
    ConstraintLayout cambiarMeta, progresoMeta;
    SharedPreferences preferences;
    TextView txtProm, txtRecord, txtUltimas, txtMeta;

    View.OnClickListener elegirMeta = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String meta = ((Button)v).getText().toString();
            try {
                Long id = preferences.getLong("userId", -1);
                String ip = preferences.getString("ip", "");
                String url = "http://" + ip + "/serverejercicio/metas.php";
                JSONObject data = new JSONObject();
                data.put("meta", meta);
                data.put("idUsuario",  String.valueOf(id));
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cambiarMeta.setVisibility(View.GONE);
                        progresoMeta.setVisibility(View.VISIBLE);
                        getStats();
                    }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "No se pudo cambiar la meta", Toast.LENGTH_SHORT).show();
                            }
                        });
                RequestQueue rQueue = Volley.newRequestQueue(getContext());
                rQueue.add(objectRequest);
                rQueue.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK){
                
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_metas, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        btnCambiarMeta = v.findViewById(R.id.btnCambiarMeta);
        btnMedir = v.findViewById(R.id.btnMedirMeta);
        btnPierna = v.findViewById(R.id.btnPiernas);
        btnAbdomen = v.findViewById(R.id.btnAbdomen);
        btnBrazo = v.findViewById(R.id.btnBrazos);
        cambiarMeta = v.findViewById(R.id.elegirMeta);
        progresoMeta = v.findViewById(R.id.progresoMeta);
        txtProm = v.findViewById(R.id.txtPromedio);
        txtRecord = v.findViewById(R.id.txtRecord);
        txtUltimas = v.findViewById(R.id.txtUltimas);
        txtMeta = v.findViewById(R.id.txtMeta);

        getStats();

        btnBrazo.setOnClickListener(elegirMeta);
        btnAbdomen.setOnClickListener(elegirMeta);
        btnPierna.setOnClickListener(elegirMeta);

        btnCambiarMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarMeta.setVisibility(View.VISIBLE);
                progresoMeta.setVisibility(View.GONE);
            }
        });

        btnMedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medir = new Intent(getContext(), ContadorRepeticiones.class);
                medir.putExtra("Nombre", txtMeta.getText().toString());
                medir.putExtra("Instruccion", "Realiza la mayor cantidad de repeticiones que puedas");
                medir.putExtra("Repeticiones", "100");
                startActivityForResult(medir, 1);
            }
        });
        return v;
    }

    public void getStats(){
        Long id = preferences.getLong("userId", -1);
        String ip = preferences.getString("ip", "");
        String url = "http://" + ip + "/serverejercicio/metas.php?idUsuario=" + id;
        JsonObjectRequest requestStats = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("meta") != null) {
                        txtMeta.setText(response.getString("meta"));
                        txtProm.setText(response.getString("promedio"));
                        txtRecord.setText(response.getString("record"));
                        txtUltimas.setText(response.getString("ultimo"));
                    }else{
                        cambiarMeta.setVisibility(View.VISIBLE);
                        progresoMeta.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(requestStats);
        rQueue.start();
    }
}
