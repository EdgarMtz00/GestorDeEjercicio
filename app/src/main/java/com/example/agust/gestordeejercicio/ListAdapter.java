package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;                    //contexto del fragment en el que se instance
    private ArrayList<Rutina> rutinas;          //Lista de una rutina
    private ArrayList<Ejercicio> ejercicios;    //Lista de ejercicios
    private boolean type;                       //Define que lista se usara

    public ListAdapter(Context context) {
        this.context = context;
        rutinas = new ArrayList<>();
        ejercicios = new ArrayList<>();
    }

    /**
     * Genera la lista de rutinas a partir de
     * un arreglo Json de objetos
     */
    public void setRutinas(JSONArray jsonRutinas){
        type = true;
        for(int i = 0; i < jsonRutinas.length(); i++){
            try {
                JSONObject jsonRutina = jsonRutinas.getJSONObject(i);
                rutinas.add(Rutina.rutinaParse(jsonRutina));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Genera la lista de rutinas a partir de
     * un arreglo Json de objetos
     */
    public void setEjercicios(JSONArray jsonEjercicios){
        type = false;
        for(int i = 0; i < jsonEjercicios.length(); i++){
            try {
                JSONObject jsonEjercicio = jsonEjercicios.getJSONObject(i);
                ejercicios.add(Ejercicio.ejercicioParse(jsonEjercicio));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return
     * La cantidad de objetos que almacenara la lista
     */
    @Override
    public int getCount() {
        return (type)? rutinas.size() : ejercicios.size();
    }

    /**
     * @return
     * El objeto correspondiente a una posicion
     * dada en la lista
     */
    @Override
    public Object getItem(int position) {
        return (type)? rutinas.get(position) : ejercicios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @return
     * Una vista correspondiente al tipo de lista con
     * la informacion del objeto en la posicion dada.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater vLink = LayoutInflater.from(context);
        if(type) {
            View v = vLink.inflate(R.layout.rutina, null);
            TextView textView = v.findViewById(R.id.txtDia);
            textView.setText(rutinas.get(position).getDia());
            textView = v.findViewById((R.id.txtRepeticiones));
            textView.setText(rutinas.get(position).getRepeticiones());
            return v;
        }else{
            final View v = vLink.inflate(R.layout.ejercicio, null);
            TextView textView = v.findViewById(R.id.txtNombre);
            textView.setText(ejercicios.get(position).getNombre());
            textView = v.findViewById((R.id.txtInstruccion));
            textView.setText(ejercicios.get(position).getInstruccion());
            final TextView txtRepeticiones = v.findViewById(R.id.txtRepeticiones);
            SeekBar sbRepeticiones = v.findViewById(R.id.sbRepeticiones);
            sbRepeticiones.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(@NonNull SeekBar seekBar, int progress, boolean fromUser) {
                    txtRepeticiones.setText(String.valueOf(progress));
                    ejercicios.get(position).setRepeticiones(progress);
                    if(progress != 0){
                        v.setBackgroundColor(Color.GREEN);
                    }else{
                        v.setBackgroundColor(Color.WHITE);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            return v;
        }
    }
}
