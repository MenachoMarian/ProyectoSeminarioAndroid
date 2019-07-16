package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.google.firebase.auth.AuthCredential ;
import  com.google.firebase.auth.AuthResult ;
import  com.google.firebase.auth.FirebaseAuth ;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //variables
    private static final int PERMISSION_SING_IN = 1234;
    private GoogleApiClient GoogleApiClient;
    //private SignInButton btngoogle;

    FirebaseAuth firebaseAuth;
    //AlertDialog waiting_dialog = new SpotsDialog.Builder().setContext(this);

    private void firebaseAuthwithGoogle(AuthCredential credential) {
        firebaseAuth.signInWithCredential( credential )
                .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //empezando una nueva actividad y pasando el email a la nueva actividad
                        Intent intent = new Intent( Login.this,RegistroGoogle.class );
                        intent.putExtra( "email", authResult.getUser().getEmail());
                        startActivity( intent );
                    }
                } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( Login.this, ""+e.getMessage(),Toast.LENGTH_SHORT ).show();
            }
        } );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        GoogleApiClient = new GoogleApiClient.Builder( this )
                .enableAutoManage( this, this )
                .addApi( Auth.GOOGLE_SIGN_IN_API , gso )
                .build();

        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
       // configureGoogleSignIn();
        loadComponents();


    }

    /*private void configureGoogleSignIn() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken( getString( R.string.default_web_client_id ))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .enableAutoManage( this,this )
                .addApi( Auth.GOOGLE_SIGN_IN_API,options )
                .build();
        mGoogleApiClient.connect();
    }*/

    private void loadComponents(){
        Button btnlogin = findViewById(R.id.btnlogin);
        Button btnregistro = findViewById(R.id.btnregistro_txt);
        btnlogin.setOnClickListener(this);
        btnregistro.setOnClickListener(this);
        //escoger el boton de google y registrar el evento
        SignInButton btngoogle = findViewById(R.id.btngoogle);
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
                SingIn();
                break;
            }
        }
    }

    private void SingIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent( GoogleApiClient );
        startActivityForResult( intent,PERMISSION_SING_IN );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == PERMISSION_SING_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            hasndleSignInResult(result);
        }
    }

    private void hasndleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            goMainScreem();
        }else {
            Toast.makeText( this,"no se pudo iniciar sesion",Toast.LENGTH_SHORT ).show();
        }
    }
    private void goMainScreem() {
        Intent intent = new Intent( this, RegistroGoogle.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
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
            Toast.makeText( this,""+connectionResult.getErrorMessage(),Toast.LENGTH_SHORT ).show();
    }
}
