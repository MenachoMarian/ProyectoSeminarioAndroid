package com.example.proyecto;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.google.android.gms.tasks.Task ;
import com.google.firebase.auth.AuthCredential ;
import  com.google.firebase.auth.AuthResult ;
import  com.google.firebase.auth.FirebaseAuth ;
import  com.google.firebase.auth.FirebaseUser ;
import  com.google.firebase.auth.GoogleAuthProvider ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //variables
    private static final int PERMISSION_SING_IN = 1234;
    GoogleApiClient mGoogleApiClient;
    private SignInButton btngoogle;
    FirebaseAuth firebaseAuth;
    //AlertDialog waiting_dialog = new SpotsDialog.Builder().setContext(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == PERMISSION_SING_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                String idToken = account.getIdToken();

                AuthCredential credential = GoogleAuthProvider.getCredential( idToken,null );
                firebaseAuthwithGoogle(credential);

            }else{
                Log.e( "Emmm_Error","Login Failled" );
                Log.e( "Emmm_Error", result.getStatus().getStatusMessage());
            }
        }
    }

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
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        configureGoogleSignIn();
        loadComponents();
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken( getString( R.string.default_web_client_id ))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .enableAutoManage( this,this )
                .addApi( Auth.GOOGLE_SIGN_IN_API,options )
                .build();
        mGoogleApiClient.connect();
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
                SingIn();
                break;
            }
        }
    }

    private void SingIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent( mGoogleApiClient );
        startActivityForResult( intent,PERMISSION_SING_IN );
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
