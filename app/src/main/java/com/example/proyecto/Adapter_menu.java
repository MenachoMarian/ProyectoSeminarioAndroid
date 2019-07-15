package com.example.proyecto;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class Adapter_menu extends RecyclerView.Adapter<Adapter_menu.Datos>
{

    ArrayList<item> list;
    Context context;
    Adapter_menu(Context c)
    {
        list = new ArrayList<>();
        context = c;
    }

    public void add (item i ){
        list.add(i);
        notifyItemInserted(list.indexOf(i));
    }

    @NonNull
    @Override
    public Datos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_producto,viewGroup,false);

        return new Datos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_menu.Datos datos, int i) {
        item it = list.get(i);
        Glide.with(context).load(Utils.HOST+"uploads/"+it.getUrl()).into(datos.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class Datos extends RecyclerView.ViewHolder{
        ImageView img;

        public Datos(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.subim);

        }
    }
}
