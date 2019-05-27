package com.example.agust.gestordeejercicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences preferences; //Preferencias de la aplicacion
    SharedPreferences.Editor editor; //Editor de preferencias
    EditText etCorreo, etContrasena; //Campos de corre y contraseña
    Button btnInicio;
    LoginButton loginButton; //Boton para iniciar sesion
    String url;
    Context ctx = this;
    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Inicializa elementos del activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        etContrasena = findViewById(R.id.etContrasena);
        etCorreo = findViewById(R.id.etCorreo);
        btnInicio = findViewById(R.id.btnInicio);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        String ip = preferences.getString("ip", "");
        url = "http://" + ip + "/ServerEjercicio/IniciarSesion.php"; //Url de la API
        btnInicio.setOnClickListener(this);



        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(ctx, DatosUsuario.class);
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                String id = accessToken.getUserId();
                intent.putExtra("id", id);
                editor.putLong("userId", Long.parseLong(id));
                editor.apply();
                startActivity(intent);

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }


    /**
     * Evento accionado cuando se presiona btnInicio
     * obtiene informacion de inicio de sesion de la API
     * y llama a realizar el logIn.
     */
    @Override
    public void onClick(View v){
        String correo, contrasena;
        correo = etCorreo.getText().toString();
        contrasena = etContrasena.getText().toString();
        if (Utils.validateEmail(correo) && Utils.validateText(correo)){
            JSONObject data = new JSONObject();
            try {
                data.put("correo", correo);
                data.put("pwd", contrasena);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                logIn(jsonObject);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                            }
                        });
                RequestQueue rQueue = Volley.newRequestQueue(this);
                rQueue.add(jsonRequest);
                rQueue.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Guarda el id del usuario en las preferencias y accede a MainActivity
     * @param jsonObject: json obtenido de la peticion a la API
     */
    public void logIn(JSONObject jsonObject){
        try {
            if(jsonObject.getBoolean("logIn")){
                editor.putLong("userId", jsonObject.getInt("id"));
                editor.apply();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else{
                retry();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Notifica al usuario si falla el inicio de sesion
     */
    public void retry(){
        Toast.makeText(this, "Bad Credentials", Toast.LENGTH_SHORT).show();
    }

    public void onClickFacebook(View v){

    }

    public void onClickRegistro(View v){
        startActivity( new Intent(this, Registro.class));
    }
}
