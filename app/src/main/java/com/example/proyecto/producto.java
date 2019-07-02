package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class producto extends AppCompatActivity implements View.OnClickListener {
    ArrayList<String> categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Registro en proceso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        setSpinner();
        loadComponents();
    }

    private void loadComponents() {
        Button btnregistrarpro = findViewById(R.id.btnregistropro);

        btnregistrarpro.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnregistropro: {
                Toast.makeText(producto.this, "Registrando producto", Toast.LENGTH_SHORT).show();
                sendData();
                break;
            }
        }

    }

    private void sendData() {
        EditText nombres = findViewById(R.id.namep);
        EditText precios = findViewById(R.id.precio);
        EditText stocks = findViewById(R.id.stock);
        EditText descripciones = findViewById(R.id.descripcion);
        Spinner cats = findViewById(R.id.btncategoria);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //params.add("idusuario",Utils.ID_USER);
        params.add("nombre",nombres.getText().toString());
        params.add("precio",precios.getText().toString());
        params.add("categoria",categoria.get(cats.getSelectedItemPosition()));
        params.add("stock",stocks.getText().toString());
        params.add("descripcion",descripciones.getText().toString());

        client.post(Utils.REGISTER_PRODUCT, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("nombre")){
                    Toast.makeText(producto.this, "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                    Intent lista = new Intent(producto.this, lista_productos.class);
                    startActivity(lista);
                }
            }
        });
    }

    private void  setSpinner(){ //Adaptador para la categoría
        categoria = new ArrayList<>();
        categoria.add("Hogar");
        categoria.add("Trabajo");
        categoria.add("Otros");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoria);
        Spinner spinner = findViewById(R.id.btncategoria);
        spinner.setAdapter(adapter);
        //spinner.getSelectedItemPosition(); // recuperando posición
    }


}
