package com.example.proyecto;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient client;
    private int GOOGLE_CODE = 11235;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //PARTE PARA REGISTRARSE CON GOOGLE
        GoogleSignInOptions options = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this )
                .addApi(Auth.GOOGLE_SIGN_IN_API,options).build();

        FloatingActionButton fab = findViewById(R.id.fab);

        /*Button btnlogin = findViewById(R.id.btnlogin);

        fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Snackbar.make(view, "Ingresando", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
        sendLogin();
        }
        });*/
        loadComponents();
    }


    private void loadComponents(){
        Button btnlogin = findViewById(R.id.btnlogin);
        Button btnregistro = findViewById(R.id.btnregistro_txt);
        btnlogin.setOnClickListener(this);
        btnregistro.setOnClickListener(this);
        //escoger el boton de google
        SignInButton googlebtn = (SignInButton)this
                .findViewById(R.id.btngoogle);
        googlebtn.setOnClickListener(this);

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
            case R.id.btngoogle: {
                Intent google = Auth.GoogleSignInApi
                        .getSignInIntent(client);
                startActivityForResult(google, GOOGLE_CODE);
                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                Intent loginr = new Intent(Login.this, RegistroGoogle.class);
                loginr.putExtra("nombre",result.getSignInAccount().getDisplayName());
                loginr.putExtra("email",result.getSignInAccount().getEmail());
                startActivity(loginr);
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, R.string.error_login, Toast.LENGTH_LONG).show();
            }
        }
    }
 //Datos desde la base de datos
    private void sendLogin() {
        final EditText email = findViewById(R.id.email_txt);
        final EditText pass = findViewById(R.id.contraseña_txt);

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        params.add("email",email.getText().toString());
        params.add("contraseña",pass.getText().toString());

        client.post(Utils.LOGIN_USER,params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("token")){
                    try {
                        //Utils.EMAIL_USER = response.getString("email");
                        Utils.TOKEN = response.getString("token");
                        Utils.EMAIL_USER = email.getText().toString();
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
