package com.example.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Detalle_Producto extends AppCompatActivity implements View.OnClickListener {

    private String idprodu;
    protected TextView titulo,descripcion,estado,precio;
    protected ImageView imagen;
    //protected EditText cantidad;-------------------

    private Detalle_Producto root;

    protected DetalleProductoClase INFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__producto);
//RECUPERANDO EL ID DEL PRODUCTO QUE SE ENVIA AL HACER CLICK
        idprodu = this.getIntent().getExtras().getString("idpro");
//CARGAR COMPONENTES
        loadComponents();
 //PEDIR DATOS AL SERVIDOR
        loadData();


    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Utils.GET_PRODUCT+"?id="+idprodu,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String titulo = obj.getString("nombre");
                        String descripcion = obj.getString("descripcion");
                        String precio = obj.getString("precio");
                        String estado = obj.getString("estado");
                        String idpro = obj.getString("_id");
                        INFO = new DetalleProductoClase(titulo,descripcion,precio,estado,idpro);
                        root.cargarInformacion();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void cargarInformacion(){
        this.titulo.setText(INFO.getTitulo());
        this.descripcion.setText(INFO.getDescripcion());
        this.precio.setText(INFO.getPrecio());
        this.estado.setText(INFO.getEstado());
    }

    private void loadComponents() {

        //CARGANDO COMPONENTES
        this.titulo = findViewById(R.id.txttitulo);
        this.imagen = findViewById(R.id.imgbig);
        this.descripcion = findViewById(R.id.txtdescripcion);
        this.precio = findViewById(R.id.txtprecio);
        this.estado = findViewById(R.id.txtestado);



        //Botones
        Button btncita = findViewById(R.id.btncita);
        Button btnchat = findViewById(R.id.btnchat);
        btncita.setOnClickListener(this);
        btnchat.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btncita: {
                if (!Utils.TOKEN.equals("")){  //VERIFICAR SI EL USUARIO YA ACCEDIÓ CON SU CUENTA PARA PODER INGRESAR A SU PERFIL, SI EL TOKEN ESTÁ VACÍO NO INGRESÓ AÚN
                    Intent cita = new Intent(Detalle_Producto.this, Cita.class);
                    startActivity(cita);

                }else{
                    Toast.makeText(this, "Ud. debe acceder con su cuenta para poder ingresar", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.btnchat: {
                Intent chatview = new Intent(Detalle_Producto.this, Chat.class);
                startActivity(chatview);
                break;
            }

        }
    }
}
