package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProductoUsuario extends AppCompatActivity implements View.OnClickListener {

    private String idprodu,nompro,idproducto;
    protected TextView titulo,descripcion,estado,precio,stocks,categorias;
    protected ImageView imagen;

    private ProductoUsuario root;
    protected ProductoUsuarioClase INFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_usuario);

        //RECUPERANDO EL ID Y EL NOMBRE DEL PRODUCTO QUE SE ENVIA AL HACER CLICK
        idprodu = this.getIntent().getExtras().getString("idpro");

        //CARGAR COMPONENTES
        loadComponents();
        // PEDIR DATOS AL SERVIDOR
        loadData();
    }

    private void loadComponents() {

        //CARGANDO COMPONENTES
        this.titulo = findViewById(R.id.txtnombreusu);
        this.imagen = findViewById(R.id.imgprousuario);
        this.descripcion = findViewById(R.id.txtdescriusu);
        this.precio = findViewById(R.id.txtpreciousu);
        this.estado = findViewById(R.id.txtestadousu);
        this.stocks = findViewById(R.id.txtstockusu);
        this.categorias = findViewById(R.id.txtcateusu);

        //Botones
        Button btneditar = findViewById(R.id.btneditar);
        Button btneliminar = findViewById(R.id.btneliminar);
        btneditar.setOnClickListener(this);
        btneliminar.setOnClickListener(this);

        ImageButton btnhome = findViewById(R.id.btnhomeproductousuario);
        btnhome.setOnClickListener(this);
    }

    private void loadData(){
        AsyncHttpClient client = new AsyncHttpClient();
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
                        idproducto = obj.getString("_id");
                        String stock = obj.getString("stock");
                        String cate = obj.getString("categoria");
                        String img = obj.getString("picture");
                        nompro = obj.getString("nombre");
                        INFO = new ProductoUsuarioClase(titulo,descripcion,precio,estado,idproducto,stock,cate,img);
                        root.cargarInformacion();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void cargarInformacion(){
        this.titulo.setText(INFO.getNombre());
        this.descripcion.setText(INFO.getDescripcion());
        this.precio.setText(INFO.getPrecio());
        this.estado.setText(INFO.getEstado());
        this.stocks.setText(INFO.getStock());
        this.categorias.setText(INFO.getCategoria());
    }

    public void eliminarData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.delete(Utils.GET_PRODUCT_ID+idproducto, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent listanueva = new Intent(ProductoUsuario.this,lista_productos.class);
                startActivity(listanueva);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btneditar: {
                Intent Editar = new Intent(ProductoUsuario.this, EditarProducto.class);
                Editar.putExtra("idproducto",idproducto);
                startActivity(Editar);
                break;
            }
            case R.id.btneliminar: {
                eliminarData();
            }
            case R.id.btnhomeproductousuario: {
                Intent main = new Intent(ProductoUsuario.this, MainActivity.class);
                startActivity(main);
            }

        }
    }
}
