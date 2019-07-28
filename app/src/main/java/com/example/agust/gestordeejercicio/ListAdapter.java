package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ListAdapter extends BaseAdapter {
    private Context context;                    //contexto del fragment en el que se instance
    private ArrayList<Rutina> rutinas;          //Lista de una rutina
    private ArrayList<Ejercicio> ejercicios;    //Lista de ejercicios
    private boolean type;                       //Define que lista se usara
    public String zonaActiva = "Brazos";
    public String diaActivo = "Lunes";

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
        ejercicios = new ArrayList<>();
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
            //Prepara la vista de una rutina
            Rutina r = rutinas.get(position);
            View v = vLink.inflate(R.layout.rutina, null);

            TextView textView = v.findViewById(R.id.txtDia);
            textView.setText(r.getDia());
            textView = v.findViewById(R.id.txtNombre);
            textView.setText(r.getEjercicio().getNombre());
            textView = v.findViewById((R.id.txtRepeticiones));
            textView.setText(r.getRepeticiones());
            ImageView imgDif = v.findViewById(R.id.imgDif);

            //Dependiendo del valor de dificultad se muestra la imagen con el mismo numero de estrelas
            switch (r.getEjercicio().getDificultad()){
                case 1:
                    imgDif.setImageResource(R.drawable.unaestrella);
                    break;
                case 2:
                    imgDif.setImageResource(R.drawable.dosestrella);
                    break;
                case 3:
                    imgDif.setImageResource(R.drawable.tresestrella);
                    break;
                default:

            }

            //Si el dia seleccionado en la actividad de rutinas es igual al de esta rutina se muestra
            if(r.getDia().equals(diaActivo)){
                textView = v.findViewById(R.id.txtDia);
                textView.setVisibility(View.VISIBLE);
                textView = v.findViewById(R.id.txtNombre);
                textView.setVisibility(View.VISIBLE);
                textView = v.findViewById((R.id.txtRepeticiones));
                textView.setVisibility(View.VISIBLE);
                textView = v.findViewById((R.id.txtDificultad));
                textView.setVisibility(View.VISIBLE);
                imgDif.setVisibility(View.VISIBLE);
            }else{ //si no se ocultan todos sus elementos
                textView = v.findViewById(R.id.txtDia);
                textView.setVisibility(View.GONE);
                textView = v.findViewById(R.id.txtNombre);
                textView.setVisibility(View.GONE);
                textView = v.findViewById((R.id.txtRepeticiones));
                textView.setVisibility(View.GONE);
                textView = v.findViewById((R.id.txtDificultad));
                textView.setVisibility(View.GONE);
                imgDif.setVisibility(View.GONE);
            }
            return v;
        }else{
            //prepara la vista de un ejercicio
            final View v = vLink.inflate(R.layout.ejercicio, null);
            Ejercicio e = ejercicios.get(position);
            TextView textView = v.findViewById(R.id.txtNombre);
            textView.setText(e.getNombre());
            textView = v.findViewById((R.id.txtInstruccion));
            textView.setText(e.getInstruccion());
            ImageView imgDif = v.findViewById(R.id.imgDif);

            //Dependiendo del valor de dificultad se muestra la imagen con el mismo numero de estrelas
            switch (e.getDificultad()){
                case 1:
                    imgDif.setImageResource(R.drawable.unaestrella);
                    break;
                case 2:
                    imgDif.setImageResource(R.drawable.dosestrella);
                    break;
                case 3:
                    imgDif.setImageResource(R.drawable.tresestrella);
                    break;
            }

            final TextView txtRepeticiones = v.findViewById(R.id.txtRepeticiones);
            SeekBar sbRepeticiones = v.findViewById(R.id.sbRepeticiones); //barra para seleccionar el numero de repeticines
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sbRepeticiones.setMax(preferences.getInt("nivel", 0) * 25); //Se configura el maximo de repeticiones para el usuario
            //Evento para cuando se selecciona un valor de repeticiones
            sbRepeticiones.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(@NonNull SeekBar seekBar, int progress, boolean fromUser) {
                    txtRepeticiones.setText(String.valueOf(progress));
                    // se guarda el valor en el objeto correspondiente a la vista modificada
                    ejercicios.get(position).setRepeticiones(progress);
                    //si es mayor a cero se marca de verde para indicar que sera agregado a la rutina
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
            //Si la zona seleccionado en la actividad de CrearRutinas es igual al de este ejercicio se muestra
            if(!e.getZona().equals(zonaActiva)) {
                textView = v.findViewById(R.id.txtDificultad);
                textView.setVisibility(View.GONE);
                textView = v.findViewById(R.id.txtNombre);
                textView.setVisibility(View.GONE);
                textView = v.findViewById((R.id.txtInstruccion));
                textView.setVisibility(View.GONE);
                imgDif.setVisibility(View.GONE);
                txtRepeticiones.setVisibility(View.GONE);
                 sbRepeticiones.setVisibility(View.GONE);
            }else{ //si no se ocultan todos sus elementos
                textView = v.findViewById(R.id.txtDificultad);
                textView.setVisibility(View.VISIBLE);
                textView = v.findViewById(R.id.txtNombre);
                textView.setVisibility(View.VISIBLE);
                textView = v.findViewById((R.id.txtInstruccion));
                textView.setVisibility(View.VISIBLE);
                imgDif.setVisibility(View.VISIBLE);
                txtRepeticiones.setVisibility(View.VISIBLE);
                sbRepeticiones.setVisibility(View.VISIBLE);
            }

            return v;

        }
    }
}
