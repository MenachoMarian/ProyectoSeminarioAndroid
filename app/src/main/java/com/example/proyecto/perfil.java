package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class perfil extends AppCompatActivity implements View.OnClickListener {

    private String email;
    private String idusu;
    TextView nombreusu,correo,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //email = Utils.EMAIL_USER;
        //RECUPERANDO EL ID Y EL NOMBRE DEL PRODUCTO QUE SE ENVIA AL HACER CLICK
        email = this.getIntent().getExtras().getString("email");

        loadComponents();
        loadData();
    }

    private void loadData() {
        if (!Utils.TOKEN.equals("")){
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(Utils.GET_USER+email,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String nombres = obj.getString("nombre");
                                String emails = obj.getString("email");
                                String pass = obj.getString("contraseña");
                                idusu = obj.getString("_id");

                                nombreusu.setText(nombres);
                                correo.setText(emails);
                                password.setText(pass);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }else {
            Toast.makeText(perfil.this, "NO TOKEN", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadComponents(){

            ImageButton producto = findViewById(R.id.btnProduct);
            //ImageButton perfil = findViewById(R.id.btneditarPerfil);
            ImageButton cita = findViewById(R.id.btncitas);
            ImageButton editarinfo = findViewById(R.id.btneditarinformacion);
            Button sesion = findViewById(R.id.btncerrarsesion);

            producto.setOnClickListener(this);
            //perfil.setOnClickListener(this);
            cita.setOnClickListener(this);
            editarinfo.setOnClickListener(this);
            sesion.setOnClickListener(this);

            this.nombreusu = findViewById(R.id.txtnombredeusuario);
            this.correo = findViewById(R.id.txtemailusuario);
            this.password = findViewById(R.id.txtcontraseñausu);

            ImageButton btnhome = findViewById(R.id.btnhomeper);
            btnhome.setOnClickListener(this);
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.btnPerfil:{
                Intent perfil = new Intent(this,Cita.class);
                startActivity(perfil);
                break;
            }*/
            case R.id.btnProduct:{
                Intent listar_producto = new Intent(perfil.this, lista_productos.class);
                listar_producto.putExtra("msn","ver lista de Productos");
                startActivity(listar_producto);
                break;
            }
            case R.id.btncitas:{
                Intent citas = new Intent(perfil.this, ListaCitas.class);
                startActivity(citas);
                break;
            }
            case R.id.btneditarinformacion: {
                Intent editar = new Intent(perfil.this, EditarUsuario.class);
                editar.putExtra("idusu",idusu);
                startActivity(editar);
                break;
            }

            case R.id.btnhomeper: {
                    Intent main = new Intent(perfil.this, MainActivity.class);
                    startActivity(main);
                    break;
            }
            case R.id.btncerrarsesion: {
                Utils.TOKEN = "";
                Utils.EMAIL_USER = "";
                Intent main = new Intent(perfil.this, MainActivity.class);
                startActivity(main);
                break;
            }

        }
    }
}
