package com.example.agust.gestordeejercicio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {
    EditText etContrasena, etCorreo, etPwd, etEstatura, etPeso, etEdad;
    Utils utils;
    String url = "http://192.168.1.79/ServerEjercicio/Registrar.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        etContrasena = findViewById(R.id.etContrasena);
        etCorreo = findViewById(R.id.etCorreo);
        etPwd = findViewById(R.id.etPwd);
        etEstatura = findViewById(R.id.etEstatura);
        etPeso = findViewById(R.id.etPeso);
        etEdad = findViewById(R.id.etEdad);
        utils = new Utils(this);
    }

    public void onClickRegistro(View v){
        String correo, pwd,estatura, edad, peso;
        if(etContrasena.getText().toString().equals(etPwd.getText().toString())) {
            correo = etCorreo.getText().toString();
            pwd = etContrasena.getText().toString();
            edad = etEdad.getText().toString();
            estatura = etEstatura.getText().toString();
            peso = etPeso.getText().toString();
            String[] input ={correo, pwd, edad, estatura, peso};

            if(utils.validateText(input)) {
                JSONObject data = new JSONObject();
                try {
                    data.put("correo", correo);
                    data.put("pwd", pwd);
                    data.put("edad", edad);
                    data.put("peso", peso);
                    data.put("estatura", estatura);
                    new ConexionAsync(url, data, this){
                        public void onPostExecute(JSONObject response){
                            Toast.makeText(ctx, "Registrado", Toast.LENGTH_LONG).show();
                        }
                    }.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                utils.requestJson(url, data);
                JSONObject res = utils.getJsonObject();
                try {
                    String s = res.getString("Error");
                    Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }else{
            Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFacebook(View v){

    }

    public void help(View v){

    }

    public void terminos(View v){

    }
}
