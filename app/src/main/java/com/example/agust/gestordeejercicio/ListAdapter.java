package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Rutina> rutinas;
    private ArrayList<Ejercicio> ejercicios;
    private boolean type;

    public ListAdapter(Context context) {
        this.context = context;
        rutinas = new ArrayList<>();
        ejercicios = new ArrayList<>();
    }

    private Rutina rutinaParse(JSONObject jsonRutina) throws JSONException {
        Rutina rutina = new Rutina();
        rutina.setDia(jsonRutina.getString("dia"));
        rutina.setRepeticiones(jsonRutina.getString("repeticiones"));
        return rutina;
    }

    public void setRutinas(JSONArray jsonRutinas){
        type = true;
        for(int i = 0; i < jsonRutinas.length(); i++){
            try {
                JSONObject jsonRutina = jsonRutinas.getJSONObject(i);
                rutinas.add(rutinaParse(jsonRutina));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios){
        type = false;
        this.ejercicios = ejercicios;
    }

    @Override
    public int getCount() {
        return (type)? rutinas.size() : ejercicios.size();
    }

    @Override
    public Object getItem(int position) {
        return (type)? rutinas.get(position) : ejercicios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vLink = LayoutInflater.from(context);
        View v;
        if(type) {
            v = vLink.inflate(R.layout.rutina, null);
            TextView textView = v.findViewById(R.id.txtDia);
            textView.setText(rutinas.get(position).getDia());
            textView = v.findViewById((R.id.txtRepeticiones));
            textView.setText(rutinas.get(position).getRepeticiones());
        }else{
            v = vLink.inflate(R.layout.ejercicio, null);
            TextView textView = v.findViewById(R.id.txtNombre);
            textView.setText(ejercicios.get(position).getNombre());
            textView = v.findViewById((R.id.txtInstruccion));
            textView.setText(ejercicios.get(position).getInstruccion());
        }
        return v;
    }
}
