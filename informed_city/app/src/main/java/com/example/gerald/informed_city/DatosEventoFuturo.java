package com.example.gerald.informed_city;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class DatosEventoFuturo extends AppCompatActivity {
    private Evento evento;
    int id;
    String nombre;
    String descripcion;
    String categoria;
    String fecha;
    int cantidadVerificacion;
    private String correo;

    private Button buttonReportar;
    private Button buttonVerificar;
    private Button buttonComentar;
    private Conexion conexion;
    private Button buttonVerComentarios;
    private MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_evento_futuro);
        Fabric.with(this, new Crashlytics());
        mixpanel = MixpanelAPI.getInstance(this,"d61a1009087902b272677b74ff7ff8f6");
        mixpanel.track("Ventana Datos Eventos Futuros",null);
        mixpanel.flush();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonComentar = findViewById(R.id.btComentarF);
        buttonReportar = findViewById(R.id.btReportarF);
        buttonVerificar = findViewById(R.id.btVerificarF);
        buttonVerComentarios = findViewById(R.id.buttonVerCF);

        id = getIntent().getExtras().getInt("id");
        nombre = getIntent().getExtras().getString("2");
        descripcion = getIntent().getExtras().getString("3");
        categoria = getIntent().getExtras().getString("5");
        fecha = getIntent().getExtras().getString("8");
        cantidadVerificacion= getIntent().getExtras().getInt("9");
        correo = getIntent().getExtras().getString("correo");
        //Toast.makeText(this,nombre+"|"+descripcion+"|"+categoria+"|"+fecha,Toast.LENGTH_SHORT).show();

        TextView text = findViewById(R.id.edtEventoF);
        text.setText(nombre);
        TextView text1 = findViewById(R.id.edtCategoriaF);
        text1.setText(categoria);
        TextView text2 = findViewById(R.id.edtHoraF);
        text2.setText(fecha);
        TextView text3 = findViewById(R.id.edtConfirmacionF);
        if(cantidadVerificacion>8){
            text3.setText("Confirmado");
        }else{
            text3.setText("Sin confirmar");
        }
        TextView text4 = findViewById(R.id.edtDescripcionF);
        text4.setText(descripcion);

        buttonVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    verificarEvento();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                intent1.putExtra("correo",correo);
                startActivity(intent1);
            }
        });
        buttonVerComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DatosEventoFuturo.this, ClaseComentarioEvento.class);
                intent1.putExtra("id",id);
                intent1.putExtra("tipo",2);
                startActivity(intent1);
            }
        });
    }

    private void verificarEvento() throws JSONException {
        int cantidad = cantidadVerificacion+1;

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("confirmacion", cantidad);

        conexion = new Conexion();

        String eventoS = String.valueOf(id);
        String result="";
        //
        try {
            result = conexion.execute("https://informedcityapp.herokuapp.com/event_futures/"+eventoS,"PATCH",jsonParam.toString()).get();
        } catch (InterruptedException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this,"Confirmación enviada.",Toast.LENGTH_SHORT).show();
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
