package com.example.proyecto;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cita extends AppCompatActivity implements View.OnClickListener{

    TimePicker timePicker;
    DatePicker datePicker;

    int hour,minute;
    static final int TIME_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadComponents();

        timePicker = (TimePicker)findViewById(R.id.hora);
        timePicker.setIs24HourView(true);
        datePicker = (DatePicker)findViewById(R.id.fecha);

    }

    private void loadComponents(){
        Button btnhora = findViewById(R.id.btnhora);

        btnhora.setOnClickListener(this);

    }



    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id){
            case TIME_DIALOG_ID:
                return new TimePickerDialog(
                        this, mTimeSetListener, hour, minute, false
                );
        }
        return null;
    }
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            hour = hourOfDay;
            minute = minuteOfHour;

            SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm aa");
            Date date = new Date(0,0,0, hour, minute);
            String strDate = timeformat.format(date);

            Toast.makeText(getBaseContext(), "Seleccionaste: "+strDate, Toast.LENGTH_SHORT).show();

        }
    };

    public void onClick(View v){
        Toast.makeText(getBaseContext(),
                "Fecha seleccionada: "+(datePicker.getMonth() + 1) +
                "/" + datePicker.getDayOfMonth() +
                "/" + datePicker.getYear() + "\n" +
                "Hora seleccionada: " + timePicker.getCurrentHour() +
                ":" + timePicker.getCurrentMinute(),
                Toast.LENGTH_SHORT).show();
    }

}
