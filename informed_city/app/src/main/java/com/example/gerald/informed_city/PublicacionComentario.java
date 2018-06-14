package com.example.gerald.informed_city;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class PublicacionComentario extends AppCompatActivity {
    public static ArrayList<String> lista_comentarios;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private String numero_post;
    private String idGlobal, asuntoGlobal, detalleGlobal, institucionGlobal, fechaGlobal;
    private ListView lista_view_comentarios;

    public static final String ID  = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion_comentario);
        Fabric.with(this, new Crashlytics());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button btnComentar = findViewById(R.id.btnComentar);
        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarAComentar();
            }
        });

        lista_view_comentarios = findViewById(R.id.listaComentarios);
        lista_view_comentarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mostrarMsj(position);
            }
        });


        Intent i = getIntent();
        numero_post = i.getExtras().getString("post");

        getSupportActionBar().setTitle(asuntoGlobal);


        try {
            tomaDatosPost();
            lista_comentarios = (cargarComentarios());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,lista_comentarios);
        lista_view_comentarios.setAdapter(adapter);


        getSupportActionBar().setTitle(asuntoGlobal);
        TextView txtPost = findViewById(R.id.txtPost);
        txtPost.setText(institucionGlobal+", "+fechaGlobal+"\n"+detalleGlobal);


    }


    public void enviarAComentar(){
        Intent intent = new Intent(PublicacionComentario.this,CrearComentario.class);
        intent.putExtra(ID,idGlobal);//le enviamos el id del post que se desea comentar
        startActivity(intent);
    }

    public void mostrarMsj(int pos){
        String msj = lista_comentarios.get(pos);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msj)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();


    }


    public ArrayList<String> cargarComentarios() throws JSONException, ExecutionException, InterruptedException {

        Conexion user_extendeds = new Conexion();
        String resultado_consulta_publicaciones = user_extendeds.execute("https://informedcityapp.herokuapp.com/commentary_publications.json", "GET").get();
        JSONArray datos_publicaciones = new JSONArray(resultado_consulta_publicaciones);
        List<String> publicacion = new ArrayList<>();

        JSONObject elemento;

        for(int i = 0; i < datos_publicaciones.length(); i++){

            elemento = datos_publicaciones.getJSONObject(i);

            String id = elemento.getString("publication_id");
            String nombre = elemento.getString("nombre");
            String comentario = elemento.getString("comentario_texto");
            String fecha = elemento.getString("fecha");

            if(id == idGlobal){

                publicacion.add(nombre+": "+comentario+"\nFecha: "+fecha+".");
            }
        }

        return (ArrayList<String>) publicacion;


    }




    public void tomaDatosPost() throws JSONException, ExecutionException, InterruptedException {

        Conexion user_extendeds = new Conexion();
        String resultado_consulta_publicaciones = user_extendeds.execute("https://informedcityapp.herokuapp.com/publications.json", "GET").get();
        JSONArray datos_publicaciones = new JSONArray(resultado_consulta_publicaciones);
        List<String> publicacion = new ArrayList<>();

        JSONObject elemento;

        for(int i = 0; i < datos_publicaciones.length(); i++){

            if(Integer.valueOf(numero_post) != i){
                continue;
            }

            elemento = datos_publicaciones.getJSONObject(i);

            idGlobal = elemento.getString("id");
            asuntoGlobal = elemento.getString("asunto");
            detalleGlobal = elemento.getString("detalle");
            institucionGlobal = elemento.getString("institucion");
            fechaGlobal = elemento.getString("fecha");

            return;
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
