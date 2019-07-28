package com.example.agust.gestordeejercicio;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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


public class ConfiguracionFragment extends Fragment {
    EditText etPeso, etEdad, etEstatura;//campos para ingresar edad, peso y estatura
    TextView txtImc; //Texto del Indice de Masa Corporal
    Button btnCambio, btnCerrar; //Botones para cambiar datos y cerrar sesion
    SharedPreferences preferences; //preferencias del sistema
    SharedPreferences.Editor editor; //Editor de preferencias
    Spinner spinIntervalo; //Selector de tiempo para recibir notificaciones
    Switch swNotificacion; //Activar y desactivar notificaciones
    @Override
    public View onCreateView(LayoutInflater inflater,
                 ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_configuracion, container, false);
        etPeso = v.findViewById(R.id.peso);             //campo para ingresar peso
        etEdad = v.findViewById(R.id.edad);             //campo para ingresar edad
        etEstatura = v.findViewById(R.id.estatura);     //campo para ingresar estatura
        txtImc = v.findViewById(R.id.txtImc);
        etPeso.addTextChangedListener(textWatcher);     //
        etEstatura.addTextChangedListener(textWatcher); //Vincular campos de texto con TextWatcher
        etEdad.addTextChangedListener(textWatcher);     //
        spinIntervalo = v.findViewById(R.id.spinIntervalo);
        swNotificacion = v.findViewById(R.id.notificaciones);

        /**
         * Evento activado cuando se cambia swNotificaciones
         */
        swNotificacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((MainActivity)getActivity()).notificaciones(isChecked);//Cambia el estado de las notificaciones en el MainActivity
            }
        });

        /**
         *Evento activado cuando se selecciona una opcion de spinIntervalo
         */
        spinIntervalo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ((MainActivity)getActivity()).setInterval(AlarmManager.INTERVAL_DAY); //Cambia la frecuencia de notificaciones por una cada dia
                        break;
                    case 1:
                        ((MainActivity)getActivity()).setInterval(AlarmManager.INTERVAL_DAY * 7); //Cambia la frecuencia de notificaciones por una cada semana
                        break;
                    case 2:
                        ((MainActivity)getActivity()).setInterval(AlarmManager.INTERVAL_DAY * 30); //Cambia la frecuencia de notificaciones por una cada mes
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences
                (this.getActivity().getApplicationContext());       //Preferencias de la aplicacion
        editor = preferences.edit();                                //Editor de preferencias

        btnCerrar = v.findViewById(R.id.btnCerrarSesion); //Boton para cerrar sesion
        btnCambio = v.findViewById(R.id.btnCambio);       //Boton para guardar cambios

        String ip = preferences.getString("ip", "");
        String url = "http://" + ip + "/ServerEjercicio/imc.php?idUsuario=" + preferences.getLong("userId", -1); //URL de la API

        final JsonObjectRequest imcRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            double imc = response.getDouble("imc");
                            String estado = response.getString("estado");
                            txtImc.setTextColor(Color.RED);
                            if (estado.equals("bueno") )
                                txtImc.setTextColor(Color.GREEN);
                            txtImc.setText("Imc: " + imc + " " + estado);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
        });
        //inicia la peticion del IMC
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(imcRequest);
        rQueue.start();

        /*
          Evento accionado cuando se presiona btnCerrar
          elimina la id del usuario de las preferencias
          y regresa a la activity para iniciar sesion
         */
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                editor.remove("userId");
                editor.apply();
                startActivity(new Intent(getActivity(), InicioSesion.class));
                getActivity().finish();
            }
        });

        /*
          Evento accionado cuando se presiona btnCambio obtiene los valores de los campos,
          los convierte a un objeto json y realiza una peticion a la API para
          actualizar al usuario
         */
        btnCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    //Se revisa que campos no estan vacios y se guarda su valor
                    jsonObject.put("idUsuario",  String.valueOf(preferences.getLong("userId", -1)));
                    if(!etEdad.getText().toString().equals("")){
                        jsonObject.put("edad", Integer.parseInt(etEdad.getText().toString()));
                    }
                    if(!etEstatura.getText().toString().equals("")){
                        jsonObject.put("estatura", Integer.parseInt(etEstatura.getText().toString()));
                    }
                    if(!etPeso.getText().toString().equals("")){
                        jsonObject.put("peso", Integer.parseInt(etPeso.getText().toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String ip = preferences.getString("ip", "");
                String url = "http://" + ip + "/ServerEjercicio/usuario.php"; //URL de la API

                //Cuando se recibe la respuesta de que se han guardado los datos se limpian los campos
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                etEdad.setText("");
                                etEstatura.setText("");
                                etPeso.setText("");
                                btnCambio.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                //inicia la peticion para guardar los cambios
                RequestQueue rQueue = Volley.newRequestQueue(getContext());
                rQueue.add(jsonObjectRequest);
                rQueue.start();
            }
        });
        return v;
    }

    /**
     * Objeto para revisar cuando se cambia
     * un campo de texto.
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        /**
         * Hace visible el boton para guardar cambios
         * una vez se ingrese un nuevo valor.
         */
        @Override
        public void afterTextChanged(Editable s) {
            btnCambio.setVisibility(View.VISIBLE);
        }
    };
}
