package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    EditText etPeso, etEdad, etEstatura;
    Button btnCambio, btnCerrar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater,
                 ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_configuracion, container, false);
        etPeso = v.findViewById(R.id.peso);             //campo para ingresar peso
        etEdad = v.findViewById(R.id.edad);             //campo para ingresar edad
        etEstatura = v.findViewById(R.id.estatura);     //campo para ingresar estatura
        etPeso.addTextChangedListener(textWatcher);     //
        etEstatura.addTextChangedListener(textWatcher); //Vincular campos de texto con TextWatcher
        etEdad.addTextChangedListener(textWatcher);     //

        preferences = PreferenceManager.getDefaultSharedPreferences
                (this.getActivity().getApplicationContext());       //Preferencias de la aplicacion
        editor = preferences.edit();                                //Editor de preferencias

        btnCerrar = v.findViewById(R.id.btnCerrarSesion); //Boton para cerrar sesion
        btnCambio = v.findViewById(R.id.btnCambio);       //Boton para guardar cambios

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
                    jsonObject.put("idUsuario", preferences.getInt("userId", -1));
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
                String url = "http://192.168.1.73/serverejercicio/usuario.php";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                etEdad.setText("");
                                etEstatura.setText("");
                                etPeso.setText("");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
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
            if(s == etEdad.getEditableText()){

            }else if(s == etEstatura.getEditableText()){

            }else if(s == etPeso.getEditableText()){

            }
        }
    };
}
