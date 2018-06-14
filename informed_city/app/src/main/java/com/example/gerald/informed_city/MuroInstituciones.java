package com.example.gerald.informed_city;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.crashlytics.android.Crashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class MuroInstituciones extends AppCompatActivity {
    public static ArrayList<ArrayList<String>> lista_datos_post;
    public static ArrayList<String> lista_post;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    public static final String PERIODICO  = "periodico";
    public static final String POSICION  = "post";

    public static final String ID  = "id";
    public static final String ASUNTO  = "asunto";
    public static final String DETALLE  = "detalle";
    public static final String INSTITUCION  = "institución";
    public static final String FECHA  = "fecha";

    public int periodico;

    private MixpanelAPI mixpanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muro_instituciones);
        Fabric.with(this, new Crashlytics());
        mixpanel = MixpanelAPI.getInstance(this,"d61a1009087902b272677b74ff7ff8f6");
        mixpanel.track("Ventana Muro Instituciones",null);
        mixpanel.flush();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Publicaciones de Instituciones Públicas");

        listView = findViewById(R.id.listaPublicaciones);

        try {
            lista_post = (cargarPosts());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,lista_post);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enviarPost(position);
            }

        });

        Button btnSucesos = findViewById(R.id.btnSucesos);
        btnSucesos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //primero preguntamos cuál es la página a la que quiere ir
                preguntarPeriodico();
            }
        });


    }

    public void enviarInfo(int seleccionado){
        Intent intent = new Intent(MuroInstituciones.this,WebViewNacion.class);
        intent.putExtra(PERIODICO,Integer.toString(seleccionado));
        startActivity(intent);

    }


    public void preguntarPeriodico(){
        String[] arregloPeriodico = {"La Nación","La Prensa Libre","Diario Extra","La República","CR Hoy","La Teja"};
        final int[] result = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione el periódico que desea visitar.")
                .setItems(arregloPeriodico, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        periodico = which + 1;

                        //luego enviamos la info
                        enviarInfo(periodico);

                    }
                });
        builder.create();
        builder.show();

    }

    public void enviarPost(int posicion){
        Intent intent = new Intent(MuroInstituciones.this,PublicacionComentario.class);
        intent.putExtra(POSICION,Integer.toString(posicion));
        startActivity(intent);
    }


    public ArrayList<String> cargarPosts() throws JSONException, ExecutionException, InterruptedException {

        Conexion user_extendeds = new Conexion();
        String resultado_consulta_publicaciones = user_extendeds.execute("https://informedcityapp.herokuapp.com/publications.json", "GET").get();
        JSONArray datos_publicaciones = new JSONArray(resultado_consulta_publicaciones);
        List<String> publicacion = new ArrayList<>();

        JSONObject elemento;

        for(int i = 0; i < datos_publicaciones.length(); i++){

            elemento = datos_publicaciones.getJSONObject(i);

            String id = elemento.getString("id");
            String asunto = elemento.getString("asunto");
            String detalle = elemento.getString("detalle");
            String institucion = elemento.getString("institucion");
            String fecha = elemento.getString("fecha");

            /*ArrayList<String> tmp = null;
            tmp.add(id); tmp.add(asunto); tmp.add(detalle); tmp.add(institucion); tmp.add(fecha);

            lista_datos_post.add(tmp);*/

            publicacion.add(institucion+", "+fecha+"\nTema: "+asunto);
        }

        return (ArrayList<String>) publicacion;


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
