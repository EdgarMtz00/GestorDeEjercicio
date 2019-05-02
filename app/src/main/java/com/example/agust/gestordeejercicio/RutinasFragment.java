package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    /**
     * Al iniciar el fragment, pide a la API la rutina del usuario,
     * y una vez obtenida prepara un adapter para la lista con la
     * informacion de la rutina
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rutinas, container, false);
        listView = v.findViewById(R.id.listView);
        listAdapter = new ListAdapter(getContext());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        String url ="http://192.168.1.86/serverejercicio/rutinas.php?idUsuario=" + preferences.getInt("userId", -1);
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
        return v;
    }
}
