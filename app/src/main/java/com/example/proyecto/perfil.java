package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
    }

        private void loadComponents(){

            ImageButton producto = findViewById(R.id.btnProduct);
            ImageButton perfil = findViewById(R.id.btnPerfil);
           // ImageButton sale = findViewById(R.id.btnSale);
            producto.setOnClickListener(this);
            perfil.setOnClickListener(this);
            //sale.setOnClickListener(this);
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
           /* case R.id.btnEditP:{
                Intent perfil = new Intent(this,EditarPerfil.class);
                startActivity(perfil);
                break;
            }*/
            case R.id.btnProduct:{
                Intent listar_producto = new Intent(this, lista_productos.class);
                listar_producto.putExtra("msn","ver lista de Productos");
                startActivity(listar_producto);
                break;
            }
            /*case R.id.btnSale:{
                Intent sale = new Intent(this, lista_sale.class);
                startActivity(sale);
                break;
            }*/
        }
    }
}
