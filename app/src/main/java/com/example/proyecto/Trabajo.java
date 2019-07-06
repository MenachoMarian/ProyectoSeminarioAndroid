package com.example.proyecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Trabajo extends AppCompatActivity {

    private ArrayList<Casillas> list_data;
    private Adaptadorlista adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajo);
        list_data = new ArrayList<Casillas>();
        loadComponents();
        loadData();
    }
    private void loadComponents() {

        //Cargando adaptador
        ListView lista = findViewById(R.id.listtrabajo);
        adapter = new Adaptadorlista(this,list_data);
        //adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

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
                        if (obj.getString("categoria").equals("Trabajo")){
                            Casillas item = new Casillas();
                            item.setNombrepro(obj.getString("nombre"));
                            item.setPreciopro(obj.getString("precio"));
                            list_data.add(item);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } adapter.notifyDataSetChanged();
            }
        });
    }
}
