package com.example.gerald.informed_city;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class comentarEventoFuturo extends AppCompatActivity {
    private int id;
    private String nombre;

    private TextView textViewEvento;
    private EditText editTextComentario;
    private Button buttonComentar;

    private Conexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentar_evento_futuro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewEvento = findViewById(R.id.textViewEventoF);
        editTextComentario = findViewById(R.id.editComentarioF);
        buttonComentar = findViewById(R.id.buttonCrearComentarioF);

        id = getIntent().getExtras().getInt("id");
        nombre = getIntent().getExtras().getString("event");
        textViewEvento.setText(nombre);

        buttonComentar.setOnClickListener(new View.OnClickListener() {
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

    private void guardarComentario() throws JSONException {
        String comentario = editTextComentario.getText().toString();
        if(!comentario.isEmpty()){

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("comentario_texto", comentario);
            jsonParam.put("event_future_id",id);

            conexion = new Conexion();

            String result="";
            //
            try {
                result = conexion.execute("https://informedcityapp.herokuapp.com/commentary_event_futures","POST",jsonParam.toString()).get();
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
        }else{
            Toast.makeText(this,"Complete el comentario.",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cuadro de diálogo para mensajes de información.
     * @param message
     */
    private void infoMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_img_diag_info_icon)
                .setMessage(message).setTitle("Información").setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { return; }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
