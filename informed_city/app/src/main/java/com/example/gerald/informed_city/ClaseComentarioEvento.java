package com.example.gerald.informed_city;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class ClaseComentarioEvento extends AppCompatActivity {

    private ListView lista_view_comentario;
    private int numeroEvento;
    private String consultaJson;
    private int tipo;
    private MixpanelAPI mixpanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_comentario_evento);
        Fabric.with(this, new Crashlytics());
        mixpanel = MixpanelAPI.getInstance(this,"d61a1009087902b272677b74ff7ff8f6");
        mixpanel.track("Ventana Comentarios",null);
        mixpanel.flush();

        getSupportActionBar().setTitle("COMENTARIOS: ");
        lista_view_comentario = findViewById(R.id.list_view_clase_comentario);

        numeroEvento = getIntent().getExtras().getInt("id");
        tipo = getIntent().getExtras().getInt("tipo");

        if(tipo==1){
            consultaJson = "https://informedcityapp.herokuapp.com/commentary_events.json";
        }else{
            consultaJson = "https://informedcityapp.herokuapp.com/commentary_event_futures.json";
        }

        try {
            MostrarLeccion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este metodo muestra la leccion en el listview
     */
    //http://minnanokanjibackend.miwwk5bepd.us-east-1.elasticbeanstalk.com/leccion_hiraganas

    private void MostrarLeccion() throws InterruptedException, JSONException, ExecutionException {
        Conexion user_extendeds = new Conexion();
        String resultado_consulta_comentario = user_extendeds.execute(consultaJson, "GET").get();
        JSONArray datos_comentarios = new JSONArray(resultado_consulta_comentario);
        List<String> comentarios = new ArrayList<>();
        List<String> autores = new ArrayList<>();
        List<String> fecha = new ArrayList<>();

        JSONObject elemento;
        for(int i = 0; i < datos_comentarios.length(); i++){
            elemento = datos_comentarios.getJSONObject(i);
            if(tipo==1){
                if(elemento.getInt("event_id")==numeroEvento){
                    comentarios.add(elemento.getString("comentario_texto"));
                    autores.add(elemento.getString("nombre"));
                    fecha.add(elemento.getString("fecha"));
                }
            }else{
                if(elemento.getInt("event_future_id")==numeroEvento){
                    comentarios.add(elemento.getString("comentario_texto"));
                    autores.add(elemento.getString("nombre"));
                    fecha.add(elemento.getString("fecha"));
                }
            }

        }

        String[] comentarios_adapter = comentarios.toArray(new String[0]);
        String[] autores_adapter = autores.toArray(new String[0]);
        String[] fecha_adapter = fecha.toArray(new String[0]);

        CustomListComenEvento adapter = new CustomListComenEvento(this,comentarios_adapter,autores_adapter,fecha_adapter);

        if(adapter != null){
            lista_view_comentario.setAdapter(adapter);
            //infoMessageDialog("Sirve");
        }else{
            infoMessageDialog("No sirve");
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
}
