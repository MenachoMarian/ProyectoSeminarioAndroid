package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EditarUsuario extends AppCompatActivity implements View.OnClickListener {

    private String idusu;
    EditText nombre,contraseña;
    TextView correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        //RECUPERANDO EL ID Y EL NOMBRE DEL PRODUCTO QUE SE ENVIA AL HACER CLICK
        idusu = this.getIntent().getExtras().getString("idusu");

        loadComponents();
        loadData();
    }

    private void loadComponents() {
        this.nombre = findViewById(R.id.nombreditado);
        this.correo = findViewById(R.id.correoeditado);
        this.contraseña = findViewById(R.id.contraseñaeditado);

        Button guardar = findViewById(R.id.btnguardareditado);
        ImageButton home = findViewById(R.id.btnhomeinformacion);

        guardar.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    private void loadData() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.GET_USER_ID+idusu,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String nombres = obj.getString("nombre");
                        String emails = obj.getString("email");
                        String pass = obj.getString("contraseña");
                        idusu = obj.getString("_id");

                        nombre.setText(nombres);
                        correo.setText(emails);
                        contraseña.setText(pass);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendData(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("nombre",nombre.getText().toString());
        params.add("email",correo.getText().toString());
        params.add("contraseña",contraseña.getText().toString());

        client.patch(Utils.GET_USER_ID+idusu, params , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response.has("roles")){
                    Toast.makeText(EditarUsuario.this, "Información registrada con exito", Toast.LENGTH_LONG).show();
                    Intent perfil = new Intent(EditarUsuario.this, MainActivity.class);
                    startActivity(perfil);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnguardareditado: {
                sendData();
                break;
            }
            case R.id.btnhomeinformacion: {
                Intent home = new Intent(EditarUsuario.this, MainActivity.class);
                startActivity(home);
                break;
            }
        }
    }
}
