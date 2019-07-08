package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<Casillas> list_data;
    private Adaptadorlista adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loadComponents();
        loadData();
    }

    private void loadComponents() {

        //Cargando adaptador
        ListView lista = findViewById(R.id.listaprincipal);
        adapter = new Adaptadorlista(this,list_data);
        //adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

        //PARA ACCEDER A CADA ELEMENTO DE LA LISTA
        lista.setOnItemClickListener(this);


        //Cargando Botones
        Button hogar = findViewById(R.id.hogarbtn);
        Button trabajo = findViewById(R.id.trabajobtn);
        Button otros = findViewById(R.id.otrosbtn);
        hogar.setOnClickListener(this);
        trabajo.setOnClickListener(this);
        otros.setOnClickListener(this);

        //PARTE EN LA QUE EL BUSCADOR (BUSCARÁ RESULTADOS)
        TextView buscar = findViewById(R.id.buscar);
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TEXT", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //ListView lista = findViewById(R.id.productlist);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.hogarbtn: {
                Intent hogarview = new Intent(MainActivity.this, Hogar.class);
                startActivity(hogarview);
                break;
            }
            case R.id.trabajobtn: {
                Intent trabajoview = new Intent(MainActivity.this, Trabajo.class);
                startActivity(trabajoview);
                break;
            }
            case R.id.otrosbtn: {
                Intent otrosview = new Intent(MainActivity.this, Otros .class);
                startActivity(otrosview);
                break;
            }
        }

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
                        Casillas item = new Casillas();
                        item.setNombrepro(obj.getString("nombre"));
                        item.setPreciopro(obj.getString("precio"));
                        item.setIdpro(obj.getString("_id"));
                        list_data.add(item);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.register: {
                Intent register = new Intent(this, Register.class);
                startActivity(register);
                break;
            }
            case R.id.login: {
                Intent login = new Intent(this, Login.class);
                startActivity(login);
                break;
            }
            case R.id.perfil: {
                if (!Utils.TOKEN.equals("")){  //VERIFICAR SI EL USUARIO YA ACCEDIÓ CON SU CUENTA PARA PODER INGRESAR A SU PERFIL, SI EL TOKEN ESTÁ VACÍO NO INGRESÓ AÚN
                    Intent perfil = new Intent(this, perfil.class);
                    startActivity(perfil);

                }else{
                    Toast.makeText(this, "Ud. debe acceder con su cuenta para poder ingresar", Toast.LENGTH_LONG).show();
                }
                break;
            }
            /*case R.id.work: {
                break;
            }*/
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



//RECUPERAR EL ELEMENTO DE CADA PRODUCTO
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idpro = this.list_data.get(position).getIdpro();
        Intent detallepro = new Intent(MainActivity.this, Detalle_Producto.class);
        detallepro.putExtra("idpro",idpro);
        this.startActivity(detallepro);
    }
}
