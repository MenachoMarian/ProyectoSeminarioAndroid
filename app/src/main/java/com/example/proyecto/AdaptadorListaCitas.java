package com.example.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListaCitas extends BaseAdapter {

    private Context context;
    private ArrayList<CitaConstructor> listacitas;

    public AdaptadorListaCitas(Context context, ArrayList<CitaConstructor> listacitas) {
        this.context = context;
        this.listacitas = listacitas;
    }


    @Override
    public int getCount() {
        return this.listacitas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){ //si la app se carga por primera vez tiene que crear el convertview, si la app está en pausa ya estará creada
            LayoutInflater inflate = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.listacitaformato, null);
        }
        //ImageView img = convertView.findViewById(R.id.imgproducto);
        TextView fecha = convertView.findViewById(R.id.txtfecha);
        TextView hora = convertView.findViewById(R.id.txthora);

        //img.setImageResource(this.listaproductos.get(position).getImagen());
        fecha.setText(this.listacitas.get(position).getFecha());
        hora.setText(this.listacitas.get(position).getHora());

        return convertView;
    }
}
