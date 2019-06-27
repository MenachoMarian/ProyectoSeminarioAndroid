package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.proyecto.Utils.REGISTER_USER;

public class Register extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Registro en proceso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendData();
            }
        });*/
        loadComponents();
    }

    private void loadComponents(){
        Button btnregistro = findViewById(R.id.btnregistro);

        btnregistro.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnregistro: {
                Toast.makeText(Register.this, "Registrando usuario", Toast.LENGTH_SHORT).show();
                sendData();
                break;
            }
        }

    }

    private void sendData(){
        EditText names = findViewById(R.id.nombre);
        EditText emails = findViewById(R.id.email);
        EditText pass = findViewById(R.id.contraseña);


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("nombre",names.getText().toString());
        params.add("email",emails.getText().toString());
        params.add("contraseña",pass.getText().toString());

        client.post(Utils.REGISTER_USER, params , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("roles")){
                    Toast.makeText(Register.this, "Usuario registrado con exito", Toast.LENGTH_LONG).show();
                    Intent login = new Intent(Register.this, Login.class);

                    startActivity(login);
                }
            }
        });
    }


}

