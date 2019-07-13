package com.example.proyecto;


import android.app.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.content.FileProvider;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;


import android.support.v7.app.AlertDialog;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class producto extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> categoria;

    Button btn;
    ImageView img;
    Intent i ;
    Bitmap bmp;
    Adapter_menu men;
    final static int cons = 0;

    //RecyclerView recycler;
    GridLayoutManager lay;
    private final int CODE_PERMISSIONS = 101;
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
    }

    public void init (){
        btn = (Button) findViewById(R.id.subimg);
        btn.setOnClickListener(this);
        img =(ImageView)findViewById(R.id.subim);

    }

    /*private void loadComponents() {
        Button btnregistrarpro = findViewById(R.id.btnregistropro);
        btnregistrarpro.setOnClickListener(this);

    }*/
     //declarar variables
        /*Button btn;
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
        /*protected void onCreate(Bundle savedInstanceState) {
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
            //setSpinner();
            //loadComponents();
        //empezamos con la configuracion de los adaptadores Adapter_menu, item
           // recycler = findViewById(R.id.recyclerId);
            //cargar();
        //}
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
            img = findViewById(R.id.subim);
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
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            FileAndPath fileAndPath = createFile(path,getApplicationContext());
            File file = fileAndPath.getFile();
            path = fileAndPath.getPath();
            //this.startActivityForResult(camera,COD_CAMERA);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri fileuri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
            } else {
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            startActivityForResult(camera, COD_CAMERA);
        }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);
        if (COD_GALERIA == requestCode){
            if (data != null){
                Uri imgPath=data.getData();
                img.setImageURI(imgPath);
                path = getRealPathFromURI(this,imgPath);
                Toast.makeText(producto.this, path, Toast.LENGTH_SHORT).show();
            }
        }
        if (COD_CAMERA == requestCode){
            loadImageCamera();
        }
    }

    private void loadImageCamera() {
        Bitmap imgag = BitmapFactory.decodeFile(path);
        if(img != null) {
            img.setImageBitmap(imgag);
            }
    }
   /* @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK)
        {
            Bundle extra = data.getExtras();
            bmp = (Bitmap) extra.get("data");
            img.setImageBitmap(bmp);
        }
    }*/


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
        EditText estado = findViewById(R.id.editestado);

        ImageView image = findViewById(R.id.subim);

        String email = Utils.EMAIL_USER; //recibiendo email del login


        if (path == null || path.equals("") ){
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //params.add("idusuario",Utils.ID_USER);
        params.add("nombre",nombres.getText().toString());
        params.add("precio",precios.getText().toString());
        params.add("categoria",categoria.get(cats.getSelectedItemPosition()));
        params.add("stock",stocks.getText().toString());
        params.add("descripcion",descripciones.getText().toString());
        //params.add("imagen" , image.getImageMatrix().toShortString());//no sale
        params.add("emailuser",email); //introduciendo el email
        params.add("estado",estado.getText().toString());

        client.post(Utils.REGISTER_PRODUCT, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /*if (response.has("nombre")){
                    Toast.makeText(producto.this, "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                    Intent lista = new Intent(producto.this, lista_productos.class);
                    startActivity(lista);
                }*/
                try {
                    String id = response.getString("_id");
                    if (id != null){
                        AsyncHttpClient imagen = new AsyncHttpClient();
                        RequestParams paramsimagen = new RequestParams();
                        File file = new File(path);
                        try {
                            paramsimagen.put("img", file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imagen.post(Utils.REGISTER_IMAGE_PRODUCT+id, paramsimagen, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Toast.makeText(producto.this, "Éxito", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(producto.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        path = "";
                        Intent lista = new Intent(producto.this, lista_productos.class);
                        startActivity(lista);
                    } else {
                        Toast.makeText(producto.this, "Error en el ID", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }




    // aqui nos devolvera la direccion del file path

    public static class FileAndPath{
        File file;
        String path;

        public FileAndPath(File file, String path) {
            this.file = file;
            this.path = path;
        }
        public File getFile() {
            return file;
        }
        public String getPath() {
            return path;
        }
    }

    public static FileAndPath createFile(String path, Context c) {
        //Logica de creado
        File file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
        if (!file.exists()) {
            file.mkdirs();
            //Toast.makeText(c,"crea un archivo mkdirs",Toast.LENGTH_LONG).show();
        }

        //generar el nombre
        String name = "";
        if (file.exists()) {
            name = "IMG_" + System.currentTimeMillis() / 1000 + ".jpg";
        }
        path = file.getAbsolutePath() + File.separator + name;
        File fileimg = new File(path);
        //Toast.makeText(c,"este es el path "+path,Toast.LENGTH_LONG).show();
        //Toast.makeText(c,"este es el file img "+fileimg,Toast.LENGTH_LONG).show();
        return new FileAndPath(fileimg,path);
    }
    //Aqui recuperamos la url a partir de la imagen
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(contentURI,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
