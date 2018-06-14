package com.example.gerald.informed_city;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DatosEventoFuturo extends AppCompatActivity {
    private Evento evento;
    int id;
    String nombre;
    String descripcion;
    String categoria;
    String fecha;

    private Button buttonReportar;
    private Button buttonVerificar;
    private Button buttonComentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_evento_futuro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonComentar = findViewById(R.id.btComentarF);
        buttonReportar = findViewById(R.id.btReportarF);
        buttonVerificar = findViewById(R.id.btVerificarF);

        id = getIntent().getExtras().getInt("id");
        nombre = getIntent().getExtras().getString("2");
        descripcion = getIntent().getExtras().getString("3");
        categoria = getIntent().getExtras().getString("5");
        fecha = getIntent().getExtras().getString("8");
        //Toast.makeText(this,nombre+"|"+descripcion+"|"+categoria+"|"+fecha,Toast.LENGTH_SHORT).show();

        TextView text = findViewById(R.id.edtEventoF);
        text.setText(nombre);
        TextView text1 = findViewById(R.id.edtCategoriaF);
        text1.setText(categoria);
        TextView text2 = findViewById(R.id.edtHoraF);
        text2.setText(fecha);
        TextView text3 = findViewById(R.id.edtConfirmacionF);
        text3.setText("Sin confirmar");
        TextView text4 = findViewById(R.id.edtDescripcionF);
        text4.setText(descripcion);

        buttonVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DatosEventoFuturo.this, ReportarFuturoActivity.class);
                intent1.putExtra("id",id);
                startActivity(intent1);
            }
        });
        buttonComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DatosEventoFuturo.this, comentarEventoFuturo.class);
                intent1.putExtra("id",id);
                intent1.putExtra("event",nombre);
                startActivity(intent1);
            }
        });
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
