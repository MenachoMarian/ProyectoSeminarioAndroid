package com.example.proyecto;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class Cita extends AppCompatActivity implements View.OnClickListener{
    private String idprodu, nompro;
    TextView nomproducto;
    EditText efecha,ehora,cantidad;
   // Button btnfecha, btnregistrocita;
    private int dia,mes,anio,hora,minutos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    private void sendDAta() {

        String email = Utils.EMAIL_USER; //recibiendo email del login

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add("fecha",efecha.getText().toString());
        params.add("hora",ehora.getText().toString());
        params.add("emailuser",email);
        params.add("idpro",idprodu);
        params.add("nompreproducto",nompro);
        params.add("cantidadprodu",cantidad.getText().toString());

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
