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
    private ArrayList<String> list_data;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajo);
        list_data = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        ListView lista = findViewById(R.id.listtrabajo);
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
                        if (obj.getString("categoria").equals("Trabajo")){
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
    }
}
