package com.example.proyecto;


import android.app.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

//import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.graphics.Bitmap;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class producto extends AppCompatActivity implements View.OnClickListener {





     //declarar variables
        Button btn;
        ArrayList<String> categoria;
        ImageView image;
        Intent i;
        Bitmap bmp;
        Adapter_menu men;
        //RecyclerView recycler;
        GridLayoutManager lay;
        private final int CODE_PERMISSIONS = 101;
        final static int cons = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_producto);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            //crear permisos para la camara y almacenamiento
            checkPermissionForCameraAndStorage();
            /*FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Registro en proceso", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });*/
            setSpinner();
            loadComponents();
        //empezamos con la configuracion de los adaptadores Adapter_menu, item
           // recycler = findViewById(R.id.recyclerId);
            //cargar();
        }
        //permisos de uso de camara y almacenamiento
        private void checkPermissionForCameraAndStorage() {
            if (Build.VERSION.SDK_INT<= Build.VERSION_CODES.M){
                return;
            }
            if (this.checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
                return;
            }else{
                this.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},99);

            }
            return;
        }
        //fin de permisos de camara y almacenamiento
        //recuperamos variables
        private void loadComponents() {
            Button btnregistrarpro = findViewById(R.id.btnregistropro);
            btnregistrarpro.setOnClickListener(this);
            //parte de camara
            btn = (Button)findViewById(R.id.subimg);
            btn.setOnClickListener(this);
            image = findViewById(R.id.subim);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnregistropro: {
                    Toast.makeText(producto.this, "Registrando producto", Toast.LENGTH_SHORT).show();
                    sendData();
                    break;
                }
                case R.id.subimg:{
                    cargarImagen();
                    break;
                }
            }
        }
        //elegir o galeria o camara
        //opciones para acceder a la camara o galeria
        //declaramos variables
        final int COD_GALERIA=10;
        final int COD_CAMERA=11;

        private void cargarImagen() {
            final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
            final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(producto.this);
            alertOpciones.setTitle("Seleccione una Opción");
            alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (opciones[i].equals("Tomar Foto")){
                        tomarFotografia();
                    }else{
                        if (opciones[i].equals("Cargar Imagen")){
                            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/");
                            startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_GALERIA);
                        }else{
                            dialogInterface.dismiss();
                        }
                    }
                }
            });
            alertOpciones.show();
        }

        private void tomarFotografia() {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            this.startActivityForResult(camera,COD_CAMERA);
        }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);
        if (COD_GALERIA == requestCode){
            if (data != null){
                Uri img = data.getData();
                image .setImageURI(img);
            }
        }
        if (COD_CAMERA == requestCode){
            if (data != null){
                Bundle infornation = data.getExtras();
                Bitmap img = (Bitmap) infornation.get("data");
                image.setImageBitmap(img);
            }
            /*case R.id.subim:{
                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,cons);
                break;
            }*/
        }
    }



        private void  setSpinner(){ //Adaptador para la categoría
            categoria = new ArrayList<>();
            categoria.add("Hogar");
            categoria.add("Trabajo");
            categoria.add("Otros");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoria);
            Spinner spinner = findViewById(R.id.btncategoria);
            spinner.setAdapter(adapter);
            //spinner.getSelectedItemPosition(); // recuperando posición
        }

    private void sendData() {
        EditText nombres = findViewById(R.id.namep);
        EditText precios = findViewById(R.id.precio);
        EditText stocks = findViewById(R.id.stock);
        EditText descripciones = findViewById(R.id.descripcion);
        Spinner cats = findViewById(R.id.btncategoria);

        ImageView image = findViewById(R.id.subim);

        String email = Utils.EMAIL_USER; //recibiendo email del login
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //params.add("idusuario",Utils.ID_USER);
        params.add("nombre",nombres.getText().toString());
        params.add("precio",precios.getText().toString());
        params.add("categoria",categoria.get(cats.getSelectedItemPosition()));
        params.add("stock",stocks.getText().toString());
        params.add("descripcion",descripciones.getText().toString());
        params.add("imagen" , image.getImageMatrix().toShortString());//no sale
        params.add("emailuser",email); //introduciendo el email

        client.post(Utils.REGISTER_PRODUCT, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.has("nombre")){
                    Toast.makeText(producto.this, "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                    Intent lista = new Intent(producto.this, lista_productos.class);
                    startActivity(lista);
                }
            }
        });
    }



}
