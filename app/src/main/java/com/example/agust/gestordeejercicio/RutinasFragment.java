package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;


public class RutinasFragment extends Fragment {
    ListView listView;          //lista del fragment
    ListAdapter listAdapter;    //Adapta la informacion para ser mostrada en la lista
    Button btnCrear;
    Intent crearRutina;
    Spinner spinDias;
    SharedPreferences preferences;

    /**
     * Al iniciar el fragment, pide a la API la rutina del usuario,
     * y una vez obtenida prepara un adapter para la lista con la
     * informacion de la rutina
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rutinas, container, false);
        listView = v.findViewById(R.id.listView);
        listAdapter = new ListAdapter(getContext());
        btnCrear = v.findViewById(R.id.btnCrear);
        spinDias = v.findViewById(R.id.spinDias);

        spinDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.diaActivo = spinDias.getSelectedItem().toString();
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        crearRutina = new Intent(this.getActivity(), CrearRutina.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        String ip = preferences.getString("ip", "");
        String url = "http://" + ip + "/serverejercicio/rutinas.php?idUsuario=" + preferences.getLong("userId", -1);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        listAdapter.setRutinas(jsonArray);
                        listView.setAdapter(listAdapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError){
                        Log.d("myApp", "error");

                    }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(arrayRequest);
        rQueue.start();

         btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(crearRutina);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rutina rutina = (Rutina) listAdapter.getItem(position);
                Ejercicio ejercicio = rutina. getEjercicio();
                Intent contarRepeticiones = new Intent(getActivity(), ContadorRepeticiones.class);
                contarRepeticiones.putExtra("Nombre", ejercicio.getNombre());
                contarRepeticiones.putExtra("Id", ejercicio.getId());
                contarRepeticiones.putExtra("Instruccion", ejercicio.getInstruccion());
                contarRepeticiones.putExtra("Repeticiones", rutina.getRepeticiones());
                startActivity(contarRepeticiones);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject request = new JSONObject();
                    request.put("repeticiones", data.getIntExtra("repeticiones", -1));
                    if (data.hasExtra("ingresar")){
                        String ip = preferences.getString("ip", "");
                        String url = "http://" + ip + "/serverejercicio/ejercicios.php";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
