package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
public class Login extends AppCompatActivity
        implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    //variables
    private GoogleApiClient client;
    private SignInButton btngoogle;
    int GOOGLE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //////////////////////////////////
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        //PARTE PARA REGISTRARSE CON GOOGLE
        //objeto de opciones que dira como autenticarlos
        GoogleSignInOptions options = new GoogleSignInOptions
                .Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //llamamos al GoogleApliClient
        client = new GoogleApiClient.Builder( this )
                .enableAutoManage( this, this)
                .addApi( Auth.GOOGLE_SIGN_IN_API ,options)
                .build();

     /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       Button btnlogin = findViewById(R.id.btnlogin);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Snackbar.make(view, "Ingresando", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
               sendLogin();
               }
          });*/  //para que se pueda loguear comentar el anterior codigo

       // btngoogle = (SignInButton) findViewById( R.id.btngoogle );
        //btngoogle.setOnClickListener( this );

        loadComponents();
    }

    private void loadComponents(){
        Button btnlogin = findViewById(R.id.btnlogin);
        Button btnregistro = findViewById(R.id.btnregistro_txt);
        btnlogin.setOnClickListener(this);
        btnregistro.setOnClickListener(this);
        //escoger el boton de google y registrar el evento
        btngoogle = findViewById(R.id.btngoogle);
        btngoogle.setOnClickListener(this);

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
                //llamar a una actividad
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
                startActivityForResult(intent, GOOGLE_CODE);
                break;
            }
        }
    }
    //sobre escribimos el metodo para login google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_CODE){

            //IdpResponse response=IdpResponse.fromResultIntent(data);
           GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           handleSignInResult (result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
       if (result.isSuccess()){
           goMainScreem();
       }else{
           Toast.makeText( this,R.string.error_login, Toast.LENGTH_SHORT ).show();

       }
    }

    private void goMainScreem() {
        Intent intent = new Intent( this,RegistroGoogle.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( intent );
    }
  //hasta aqui login de google

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
