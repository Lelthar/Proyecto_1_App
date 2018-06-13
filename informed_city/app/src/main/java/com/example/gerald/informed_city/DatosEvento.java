package com.example.gerald.informed_city;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DatosEvento extends AppCompatActivity {

    private Evento evento;
    int id;
    String nombre;
    String descripcion;
    String categoria;
    String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id = getIntent().getExtras().getInt("id");
        nombre = getIntent().getExtras().getString("2");
        descripcion = getIntent().getExtras().getString("3");
        categoria = getIntent().getExtras().getString("5");
        fecha = getIntent().getExtras().getString("8");
        //Toast.makeText(this,nombre+"|"+descripcion+"|"+categoria+"|"+fecha,Toast.LENGTH_SHORT).show();

        TextView text = findViewById(R.id.edtEvento);
        text.setText(nombre);
        TextView text1 = findViewById(R.id.edtCategoria);
        text1.setText(categoria);
        TextView text2 = findViewById(R.id.edtHora);
        text2.setText(fecha);
        TextView text3 = findViewById(R.id.edtConfirmacion);
        text3.setText("Sin confirmar");
        TextView text4 = findViewById(R.id.edtDescripcion);
        text4.setText(descripcion);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
