package com.example.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adaptadorlista extends BaseAdapter {

    private Context context;
    private ArrayList<Casillas> listaproductos;

    public Adaptadorlista(Context context, ArrayList<Casillas> listaproductos) {
        this.context = context;
        this.listaproductos = listaproductos;
    }

    @Override
    public int getCount() {
        return this.listaproductos.size(); //las veces que se llamará la función
    }

    @Override
    public Object getItem(int position) { //obtendrá la posición de la lista

        return position;
    }

    @Override
    public long getItemId(int position) {

         return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //donde se creará cada vista de producto
        if (convertView == null){ //si la app se carga por primera vez tiene que crear el convertview, si la app está en pausa ya estará creada
            LayoutInflater inflate = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listaformato, null);
        }
        ImageView img = convertView.findViewById(R.id.imgproducto);
        TextView nombre = convertView.findViewById(R.id.txtnombrepro);
        TextView precio = convertView.findViewById(R.id.txtpreciopro);

        //img.setImageResource(this.listaproductos.get(position).getImagen());
        Glide.with(context).load(listaproductos.get(position).getImagen()).into(img);
        nombre.setText(this.listaproductos.get(position).getNombrepro());
        precio.setText(this.listaproductos.get(position).getPreciopro());

        return convertView;
    }
}
