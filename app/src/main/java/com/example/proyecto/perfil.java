package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class perfil extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
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
        loadData();
    }

    private void loadData() {

    }

    private void loadComponents(){

        ImageButton btnhome = findViewById(R.id.btnhomeperfil);
        btnhome.setOnClickListener(this);

            ImageButton producto = findViewById(R.id.btnProduct);
            ImageButton perfil = findViewById(R.id.btnPerfil);
            ImageButton cita = findViewById(R.id.btncitas);

            producto.setOnClickListener(this);
            perfil.setOnClickListener(this);
            cita.setOnClickListener(this);
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.btnPerfil:{
                Intent perfil = new Intent(this,Cita.class);
                startActivity(perfil);
                break;
            }*/
            case R.id.btnProduct:{
                Intent listar_producto = new Intent(perfil.this, lista_productos.class);
                listar_producto.putExtra("msn","ver lista de Productos");
                startActivity(listar_producto);
                break;
            }
            case R.id.btncitas:{
                Intent citas = new Intent(perfil.this, ListaCitas.class);
                startActivity(citas);
                break;
            }

            case R.id.btnhomeperfil: {
                    Intent main = new Intent(perfil.this, MainActivity.class);
                    startActivity(main);
                    break;
            }

        }
    }
}
