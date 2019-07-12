package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegistroGoogle extends AppCompatActivity implements View.OnClickListener {
    String nombre,email;
    private  TextView name,correo,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_google);

        nombre = this.getIntent().getExtras().getString("nombre");
        email = this.getIntent().getExtras().getString("email");

        loadComponents();
    }

    private void loadComponents() {
        this.name = findViewById(R.id.txtnombregoogle);
        this.correo = findViewById(R.id.txtemailgoogle);
        this.pass = findViewById(R.id.txtpassgoogle);

        name.setText(nombre);
        correo.setText(email);
        pass.setText(email);


        Button btnaceptar = findViewById(R.id.btngoogleacep);
        btnaceptar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btngoogleacep: {
                Toast.makeText(RegistroGoogle.this, "Registrando", Toast.LENGTH_SHORT).show();
                sendData();
                break;
            }

        }
    }

    private void sendData(){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add("nombre",name.getText().toString());
        params.add("email",correo.getText().toString());
        params.add("contraseña",pass.getText().toString());

        client.post(Utils.REGISTER_USER, params , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response.has("roles")){
                    Toast.makeText(RegistroGoogle.this, "Usuario registrado con exito", Toast.LENGTH_LONG).show();
                    sendLogin();
                }
            }
        });

    }

    private void sendLogin(){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add("email",correo.getText().toString());
        params.add("contraseña",pass.getText().toString());

        client.post(Utils.LOGIN_USER,params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("token")){
                    try {
                        //Utils.EMAIL_USER = response.getString("email");
                        Utils.TOKEN = response.getString("token");
                        Utils.EMAIL_USER = correo.getText().toString();
                        Toast.makeText(RegistroGoogle.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(RegistroGoogle.this, MainActivity.class);
                        startActivity(home);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }
}
