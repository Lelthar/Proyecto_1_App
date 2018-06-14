package com.example.gerald.informed_city;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MuroInstituciones extends AppCompatActivity {

    public static ArrayList<String> lista_lecciones;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    public static final String POSICION  = "leccion";
    public int periodico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muro_instituciones);
        getSupportActionBar().setTitle("Publicaciones de Instituciones Públicas");

        listView = findViewById(R.id.listaPublicaciones);

        /*try {
            lista_lecciones = (cargarLecciones());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,lista_lecciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enviarLeccion(position);

            }

        });*/

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
        intent.putExtra(POSICION,Integer.toString(seleccionado));
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



}
