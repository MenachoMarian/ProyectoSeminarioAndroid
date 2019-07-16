package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class EditarProducto extends AppCompatActivity implements View.OnClickListener {

    private String idprodu;
    private EditText nombres, precios, stocks,descripciones, estados;
    private Spinner cats;
    private ImageView image;

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
        setContentView(R.layout.activity_editar_producto);

        //RECUPERANDO EL ID Y EL NOMBRE DEL PRODUCTO QUE SE ENVIA AL HACER CLICK
        idprodu = this.getIntent().getExtras().getString("idproducto");
        setSpinner();
        loadComponents();

        if (idprodu != null){
            recuperarDatos();
        }
    }

    private void loadComponents() {

        //CARGANDO COMPONENTES
        this.nombres = findViewById(R.id.editarnombreproductos);
        this.precios = findViewById(R.id.editarprecio);
        this.stocks = findViewById(R.id.editarcantidad);
        this.descripciones = findViewById(R.id.editardescrip);
        this.cats = findViewById(R.id.spinnereditar);
        this.estados = findViewById(R.id.editarestado);
        this.image = findViewById(R.id.editarimagen);

        //CARGAR BOTONES
        Button btnregistrarpro = findViewById(R.id.btnregistrareditar);
        btnregistrarpro.setOnClickListener(this);
        //parte de camara
        btn = (Button)findViewById(R.id.btnsubirimageneditar);
        btn.setOnClickListener(this);
        img = findViewById(R.id.editarimagen);

        ImageButton btnhome = findViewById(R.id.btnhomeeditar);
        btnhome.setOnClickListener(this);

    }

    private void  setSpinner(){ //Adaptador para la categoría
        categoria = new ArrayList<>();
        categoria.add("Hogar");
        categoria.add("Trabajo");
        categoria.add("Otros");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoria);
        Spinner spinner = findViewById(R.id.spinnereditar);
        spinner.setAdapter(adapter);
        //spinner.getSelectedItemPosition(); // recuperando posición
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnregistrareditar: {
                Toast.makeText(EditarProducto.this, "Registrando producto", Toast.LENGTH_SHORT).show();
                if (idprodu != null){
                    putData();
                }
                break;
            }
            case R.id.btnsubirimageneditar:{
                cargarImagen();
                break;
            }
            case R.id.btnhomeeditar: {
                Intent main = new Intent(EditarProducto.this, MainActivity.class);
                startActivity(main);
            }
        }
    }

    public void recuperarDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        //RequestParams params = new RequestParams();
        client.get(Utils.GET_PRODUCT_ID+idprodu,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String titulo = obj.getString("nombre");
                        String descripcion = obj.getString("descripcion");
                        String precio = obj.getString("precio");
                        String estado = obj.getString("estado");
                        idprodu = obj.getString("_id");
                        String stock = obj.getString("stock");
                        String cate = obj.getString("categoria");
                        String img = obj.getString("picture");

                        nombres.setText(titulo);
                        descripciones.setText(descripcion);
                        precios.setText(precio);
                        estados.setText(estado);
                        stocks.setText(stock);
                        //image.setImageResource(img);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    public void putData(){
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
        params.add("estado",estados.getText().toString());

        client.patch(Utils.GET_PRODUCT_ID+idprodu, params, new JsonHttpResponseHandler(){
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
                                Toast.makeText(EditarProducto.this, "Éxito", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(EditarProducto.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        path = "";
                        Intent lista = new Intent(EditarProducto.this, lista_productos.class);
                        startActivity(lista);
                    } else {
                        Toast.makeText(EditarProducto.this, "Error en el ID", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    //elegir o galeria o camara
    //opciones para acceder a la camara o galeria
    //declaramos variables
    final int COD_GALERIA=10;
    final int COD_CAMERA=11;
    String path;

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(EditarProducto.this);
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
        producto.FileAndPath fileAndPath = createFile(path,getApplicationContext());
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
                Toast.makeText(EditarProducto.this, path, Toast.LENGTH_SHORT).show();
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

    public static producto.FileAndPath createFile(String path, Context c) {
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
        return new producto.FileAndPath(fileimg,path);
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
