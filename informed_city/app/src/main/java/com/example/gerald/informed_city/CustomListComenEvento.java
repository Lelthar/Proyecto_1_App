package com.example.gerald.informed_city;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListComenEvento extends ArrayAdapter {
    private final Activity context;
    private String[] Comentarios;
    private String[] Autores;
    private String[] Fechas;

    public CustomListComenEvento(Activity context,String[] Comentarios,String[] Autores,String[] Fechas){
        super(context, R.layout.item_lista_comentario_evento,Comentarios);
        this.context = context;
        this.Comentarios = Comentarios;
        this.Autores = Autores;
        this.Fechas =Fechas;
    }
    /*
     * Metodo que carga los datos al elemento del listview
     * */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_lista_comentario_evento, null, true);
        TextView autor = rowView.findViewById(R.id.textViewAutorC);
        TextView contenido = rowView.findViewById(R.id.textViewContenidoC);
        TextView fecha = rowView.findViewById(R.id.textViewFechaC);


        autor.setText("Autor: "+Autores[position]);
        contenido.setText("Comentario: "+Comentarios[position]);
        fecha.setText("Fecha publicación: "+Fechas[position]);

        autor.setTag(position);
        contenido.setTag(position);
        fecha.setTag(position);

        return rowView;
    }
}
