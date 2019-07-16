package com.example.proyecto;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.DatePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Cita extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private String idprodu, nompro;
    TextView nomproducto;
    EditText efecha,ehora,cantidad,ubicacion;
    private MapView map;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private LatLng mainposition;
    double Lat = 0.0;
    double Lng = 0.0;

   // Button btnfecha, btnregistrocita;
    private int dia,mes,anio,hora,minutos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mapa
        map = findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.onResume();
        MapsInitializer.initialize(this);
        map.getMapAsync(this);
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

        //RECUPERANDO EL ID y el nombre DEL PRODUCTO QUE SE ENVIA AL HACER CLICK
        idprodu = this.getIntent().getExtras().getString("idpro");
        nompro = this.getIntent().getExtras().getString("nombre");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadComponents();

    }

    private void loadComponents(){
        this.efecha = findViewById(R.id.efecha);
        this.ehora = findViewById(R.id.ehora);
        this.cantidad = findViewById(R.id.cantidadcita);
        this.nomproducto = findViewById(R.id.txtnombrecita);
        this.ubicacion= findViewById( R.id.txt_ubicacion);

        Button btnhora = findViewById(R.id.btnhora);
        Button btnfecha = findViewById(R.id.btnfecha);
        Button btnregistro = findViewById(R.id.btnregistrocita);

        btnfecha.setOnClickListener(this);
        btnhora.setOnClickListener(this);
        btnregistro.setOnClickListener(this);

        nomproducto.setText(nompro);


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnfecha: {
                final Calendar c =  Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        efecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }
                ,dia,mes,anio);
                datePickerDialog.show();
                break;
            }

            case R.id.btnhora: {
                final Calendar c =  Calendar.getInstance();
                hora = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ehora.setText(hourOfDay+":"+minute);
                    }
                },hora,minutos,false);
                timePickerDialog.show();
                break;
            }
            case R.id.btnregistrocita: {
                sendDAta();
                break;
            }

        }
    }

    //MAPA

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //-19.5597641,-65.7633884
        //-19.5730936,-65.7559122
        LatLng potosi = new LatLng(-19.0429,  -65.2554);
        mainposition = potosi;
        mMap.addMarker(new MarkerOptions().position(potosi).title("Lugar").zIndex(18).draggable(true));
        mMap.setMinZoomPreference(15);
        mMap.moveCamera( CameraUpdateFactory.newLatLng(potosi));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }
            @Override
            public void onMarkerDrag(Marker marker) {
            }
            @Override
            public void onMarkerDragEnd(Marker marker) {
                mainposition = marker.getPosition();
                String street_string = getStreet(marker.getPosition().latitude, marker.getPosition().longitude);
                ubicacion.setText(street_string);
            }
        });

    }
    public String getStreet (Double lat, Double log) {
        List<Address> addresses;
        String result = "";
        try {
            addresses = geocoder.getFromLocation(lat, log, 1);
            result += addresses.get(0).getThoroughfare();

            /*for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getThoroughfare()!=null)
                result += addresses.get(i).getThoroughfare() + ",";
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void sendDAta() {

        String email = Utils.EMAIL_USER; //recibiendo email del login
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add("fecha",efecha.getText().toString());
        params.add("hora",ehora.getText().toString());
        params.add("emailuser",email);
        params.add("idpro",idprodu);
        params.add("nompreproducto",nompro); //no recibe
        params.add("cantidadprodu",cantidad.getText().toString());
        params.add("lugar",ubicacion.getText().toString());

        client.post(Utils.REGISTER_CITA, params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("fecha")){
                    Toast.makeText(Cita.this, "Cita registrada correctamente", Toast.LENGTH_SHORT).show();
                    Intent cita = new Intent(Cita.this, ListaCitas.class);
                    startActivity(cita);
                }
            }
        });
    }
}
