package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        //Button btnlogin = findViewById(R.id.btnlogin);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Snackbar.make(view, "Ingresando", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    //sendLogin();
            }
        });*/
        loadComponents();
    }

    private void loadComponents(){
        Button btnlogin = findViewById(R.id.btnlogin);
        Button btnregistro = findViewById(R.id.btnregistro_txt);
        Button btnregistrogoogle = findViewById(R.id.btnregistrogoogle);

        btnlogin.setOnClickListener(this);
        btnregistro.setOnClickListener(this);
        btnregistrogoogle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnlogin: {
                Toast.makeText(Login.this, "Ingresando", Toast.LENGTH_SHORT).show();
                sendLogin();
                break;
            }
            case R.id.btnregistro_txt: {
                Intent registro = new Intent(Login.this, Register.class);
                startActivity(registro);
                break;
            }
        }

    }


    private void sendLogin() {
        EditText email = findViewById(R.id.email_txt);
        EditText pass = findViewById(R.id.contraseña_txt);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add("email",email.getText().toString());
        params.add("contraseña",pass.getText().toString());

        client.post(Utils.LOGIN_USER,params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("token")){
                    try {
                        Utils.TOKEN = response.getString("token");
                        Toast.makeText(Login.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(Login.this, MainActivity.class);
                        startActivity(home);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }


}
