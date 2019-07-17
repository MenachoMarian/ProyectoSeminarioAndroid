package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Hogar extends AppCompatActivity implements AdapterView.OnItemClickListener , View.OnClickListener {

    private ArrayList<Casillas> list_data;
    private Adaptadorlista adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hogar);
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
    private void loadComponents() {

        //Cargando adaptador
        ListView lista = findViewById(R.id.listhogar);
        adapter = new Adaptadorlista(this,list_data);
        //adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

        //PARA ACCEDER A CADA ELEMENTO DE LA LISTA
        lista.setOnItemClickListener(this);

        ImageButton btnhome = findViewById(R.id.btnhome);
        btnhome.setOnClickListener(this);

    }

    /*private void loadData() {
        ListView lista = findViewById(R.id.listhogar);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list_data);
        lista.setAdapter(adapter);


        AsyncHttpClient client = new AsyncHttpClient();
        list_data.clear();
        client.get(Utils.GET_PRODUCT, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                for (int i = 0; i < response.length(); i++){

                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if (obj.getString("categoria").equals("Hogar")){
                            list_data.add(obj.getString("nombre"));
                            list_data.add(obj.getString("precio"));
                            list_data.add(obj.getString("categoria"));
                            list_data.add(obj.getString("descripcion"));
                            list_data.add(obj.getString("stock"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }*/

    private void loadData() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.GET_PRODUCT, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                list_data.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if (!obj.getString("emailuser").equals(Utils.EMAIL_USER)){
                            if (obj.getString("categoria").equals("Hogar")){
                                Casillas item = new Casillas();
                                item.setNombrepro(obj.getString("nombre"));
                                item.setPreciopro(obj.getString("precio"));
                                item.setIdpro(obj.getString("_id"));
                                list_data.add(item);
                            }
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idpro = this.list_data.get(position).getIdpro();
        String nompro = this.list_data.get(position).getNombrepro();
        Intent detallepro = new Intent(Hogar.this, Detalle_Producto.class);
        detallepro.putExtra("idpro",idpro);
        this.startActivity(detallepro);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnhome: {
                Intent main = new Intent(Hogar.this, MainActivity.class);
                startActivity(main);
            }
        }
    }
}
