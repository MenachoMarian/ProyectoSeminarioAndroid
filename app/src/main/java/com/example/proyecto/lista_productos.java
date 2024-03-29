package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class lista_productos extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<Casillas> list_data;
    private Adaptadorlista adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list_data = new ArrayList<Casillas>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
            loadComponents();
            loadData();
    }

    private void loadComponents(){
        Button btn3 = findViewById(R.id.btn3);
        ImageButton btnhome = findViewById(R.id.btnhomelista);
        btn3.setOnClickListener(this);
        btnhome.setOnClickListener(this);

        //Cargando adaptador
        ListView lista = findViewById(R.id.listproducto);
        adapter = new Adaptadorlista(this,list_data);
        //adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

        //PARA ACCEDER A CADA ELEMENTO DE LA LISTA
        lista.setOnItemClickListener(this);
    }

    private void loadData() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.GET_PRODUCT, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                list_data.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if (obj.getString("emailuser").equals(Utils.EMAIL_USER)){
                            Casillas item = new Casillas();
                            item.setNombrepro(obj.getString("nombre"));
                            item.setPreciopro(obj.getString("precio"));
                            item.setImagen(obj.getString("picture"));
                            item.setIdpro(obj.getString("_id"));
                            list_data.add(item);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn3:{
                Intent agregar_producto = new Intent(this,producto.class);
                startActivity(agregar_producto);
                break;
            }
            case R.id.btnhomelista: {
                Intent main = new Intent(lista_productos.this, MainActivity.class);
                startActivity(main);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idpro = this.list_data.get(position).getIdpro();
        Intent detallepro = new Intent(lista_productos.this, ProductoUsuario.class);
        detallepro.putExtra("idpro",idpro);
        this.startActivity(detallepro);
    }
}
