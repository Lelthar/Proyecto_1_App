package com.example.gerald.informed_city;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CrearComentario extends AppCompatActivity {

    public EditText txtNombre;
    public EditText txtComentario;
    private String idGlobal2;

    private Conexion conexion;

    private MixpanelAPI mixpanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_comentario);
        Fabric.with(this, new Crashlytics());
        mixpanel = MixpanelAPI.getInstance(this,"d61a1009087902b272677b74ff7ff8f6");
        mixpanel.track("Ventana Comentario Publicación",null);
        mixpanel.flush();
        getSupportActionBar().setTitle("Comentar Publicación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtNombre = findViewById(R.id.txtNombre);
        txtComentario = findViewById(R.id.txtComentario);

        Intent i = getIntent();
        idGlobal2 = i.getExtras().getString("id");


        Button btnGuardarComentarios = findViewById(R.id.btnGuardarComentarios);
        btnGuardarComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guardarComentario();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }




    private void guardarComentario() throws JSONException{

        String nombre = txtNombre.getText().toString();
        String comentario = txtComentario.getText().toString();

        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = formatter.format(date);

        if(!comentario.isEmpty() || !nombre.isEmpty()){

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("comentario_texto",comentario);
            jsonParam.put("publication_id",idGlobal2);
            jsonParam.put("fecha",fecha);
            jsonParam.put("nombre",nombre);

            conexion = new Conexion();

            String result = "";

            try {
                result = conexion.execute("https://informedcityapp.herokuapp.com/commentary_publications","POST",jsonParam.toString()).get();
            } catch (InterruptedException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            } catch (ExecutionException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            }

            if(result.equals("Created")){
                Toast.makeText(this,"Guardado",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(this,"Complete el comentario.",Toast.LENGTH_SHORT).show();
        }



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
