package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.BasicUserPrincipal;

public class BuscarProducto extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ArrayList<Casillas> list_data;
    private Adaptadorlista adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        list_data = new ArrayList<Casillas>();

        loadComponents();
    }

    private void loadComponents() {

        ImageButton btnhome = findViewById(R.id.btnhomebusqueda);
        btnhome.setOnClickListener(this);


        //Cargando adaptador
        ListView lista = findViewById(R.id.listresultados);
        adapter = new Adaptadorlista(this,list_data);
        //adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

        //PARA ACCEDER A CADA ELEMENTO DE LA LISTA
        lista.setOnItemClickListener(this);

        //PARTE EN LA QUE EL BUSCADOR (BUSCARÁ RESULTADOS)
        TextView buscar = findViewById(R.id.buscar);
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("TEXT", s.toString());
                LoadRestData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void LoadRestData(String key){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.SEARCH_PRODUCT.replaceAll("cadena",key), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                list_data.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if (!obj.getString("emailuser").equals(Utils.EMAIL_USER)){
                            if (obj.has("nombre")){
                                Casillas item = new Casillas();
                                item.setNombrepro(obj.getString("nombre"));
                                item.setPreciopro(obj.getString("precio"));
                                //item.setImagen(obj.getInt("imagen"));
                                item.setIdpro(obj.getString("_id"));
                                list_data.add(item);
                            }
                            else {
                                Toast.makeText(BuscarProducto.this, "No hay resultados", Toast.LENGTH_SHORT).show();
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
        Intent detallepro = new Intent(BuscarProducto.this, Detalle_Producto.class);
        detallepro.putExtra("idpro",idpro);
        this.startActivity(detallepro);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnhomebusqueda: {
                Intent main = new Intent(BuscarProducto.this, MainActivity.class);
                startActivity(main);
            }
        }
    }
}
