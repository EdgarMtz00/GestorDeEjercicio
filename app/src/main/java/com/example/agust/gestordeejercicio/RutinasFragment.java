package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;


public class RutinasFragment extends Fragment {
    ListView listView;          //lista del fragment
    ListAdapter listAdapter;    //Adapta la informacion para ser mostrada en la lista
    Button btnCrear;
    Intent crearRutina;

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
        crearRutina = new Intent(this.getActivity(), CrearRutina.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        String ip = preferences.getString("ip", "");
        String url = "http://" + ip + "/serverejercicio/rutinas.php?idUsuario=" + preferences.getLong("userId", -1);
        JSONArray jsonArray = new JSONArray();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        listAdapter.setRutinas(jsonArray);
                        listView.setAdapter(listAdapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

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
                contarRepeticiones.putExtra("Instruccion", ejercicio.getInstruccion());
                contarRepeticiones.putExtra("Repeticiones", rutina.getRepeticiones());
                startActivity(contarRepeticiones);
            }
        });
        return v;
    }
}
