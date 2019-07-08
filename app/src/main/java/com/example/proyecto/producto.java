package com.example.proyecto;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class producto extends AppCompatActivity implements View.OnClickListener {

        //declarar variables
    Button btn;
    ArrayList<String> categoria;
    ImageView img;
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
    //recuperamos
    private void loadComponents() {
        Button btnregistrarpro = findViewById(R.id.btnregistropro);
        btnregistrarpro.setOnClickListener(this);
        //parte de camara
        btn = (Button)findViewById(R.id.subimg);
        btn.setOnClickListener(this);
        img = (ImageView)findViewById(R.id.subim);
    }

    //cargar imagen
    /*
    private void cargar() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.HOST+"image",null,new JsonHttpResponseHandler(){
            @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                        super.onSuccess(statusCode , headers,response);
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject dat = response.getJSONObject(i);
                                String url = dat.getString("url");
                                men.add(new item(url));
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
            }
        });
    }*/
    //hasta aqui cargar imagen

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

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK){
            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");
            img.setImageBitmap(bmp);
        }
    }
    private void sendData() {
        EditText nombres = findViewById(R.id.namep);
        EditText precios = findViewById(R.id.precio);
        EditText stocks = findViewById(R.id.stock);
        EditText descripciones = findViewById(R.id.descripcion);
        Spinner cats = findViewById(R.id.btncategoria);
        ImageView imagen = findViewById(R.id.subim);


        String email = Utils.EMAIL_USER; //recibiendo email del login

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //params.add("idusuario",Utils.ID_USER);
        params.add("nombre",nombres.getText().toString());
        params.add("precio",precios.getText().toString());
        params.add("categoria",categoria.get(cats.getSelectedItemPosition()));
        params.add("stock",stocks.getText().toString());
        params.add("descripcion",descripciones.getText().toString());

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

    //elegir o galeria o camara

    //opciones para acceder a la camara o galeria
    //declaramos variables
    final int COD_GALERIA=10;
    final int COD_CAMERA=20;
    String path;
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

    }

    //enviar Datos
    /*
    private void enviarDatos(){
        if (path == null || path == ""){
            Toast.makeText(this,"cargar imagen");
            return;
        }
        File file = new File (path);
        try {
            params.put("img",file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
        client.post(Utils.HOST+"image",params,(JsonHttpResponseHandler) super.onSuccess(statusCode,headers,response);
        try {
            String mesagge = response.getString ("menssage")
            if (mesagge!= null) {
                Toast.makeText(getApplicationContext(),mesagge,Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        );

    }
*/
    //hasta aqui datos enviados

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


}
