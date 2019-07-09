package com.example.proyecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListaCitas extends AppCompatActivity {

    private ArrayList<CitaConstructor> list_data;
    private AdaptadorListaCitas adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_citas);

        list_data = new ArrayList<CitaConstructor>();
        loadComponents();
        loadData();
    }

    private void loadComponents(){

        //Cargando adaptador
        ListView lista = findViewById(R.id.listdate);
        adapter = new AdaptadorListaCitas(this,list_data);
        //adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);
    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.GET_CITA, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                list_data.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        if (obj.getString("emailuser").equals(Utils.EMAIL_USER)){
                            CitaConstructor item = new CitaConstructor();
                            item.setFecha(obj.getString("fecha"));
                            item.setHora(obj.getString("hora"));
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
