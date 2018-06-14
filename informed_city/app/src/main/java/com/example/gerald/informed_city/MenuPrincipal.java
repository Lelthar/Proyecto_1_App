package com.example.gerald.informed_city;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MenuPrincipal extends AppCompatActivity {

    private String correo;
    public boolean notificaciones;
    int PLACE_PICKER_REQUEST = 1;
    LatLng latLng; //coordenadas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        correo = getIntent().getExtras().getString("correo");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //agregado BRYAN

    }


    public void goPlacePicker(){

        //agregado BRYAN
        //This is the place to call the place picker function

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try{

            startActivityForResult(builder.build(MenuPrincipal.this),PLACE_PICKER_REQUEST);

        }catch(GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }catch(GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK ){

                Place place = PlacePicker.getPlace(MenuPrincipal.this,data);

                //MODIFICAR LAS COORDENADAS ACa
                latLng = place.getLatLng();
                Float lat = convertToFloat(latLng.latitude);
                Float lon = convertToFloat(latLng.longitude);

                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("lat", lat);
                editor.putFloat("lon", lon);
                editor.apply();


            }

        }

    }


    public static Float convertToFloat(Double doubleValue) {
        return doubleValue == null ? null : doubleValue.floatValue();
    }

    public void ExitOnClick(View view) {
        finish();
        System.exit(0);
    }

    public void btnCrearClick(View view) {
        Intent intent = new Intent(MenuPrincipal.this, crearEvento.class);
        intent.putExtra("id", correo);
        startActivity(intent);
    }

    public void btnProgramarClick(View view) {
        Intent intent = new Intent(MenuPrincipal.this, eventosFuturos.class);
        intent.putExtra("id",correo);
        startActivity(intent);
    }

    public void btnMapaClick(View view) {
        Intent intent = new Intent(MenuPrincipal.this, MapsActivity.class);
        intent.putExtra("correo",correo);
        startActivity(intent);
    }

    public void btnMapaFuturoClick(View view) {
        Intent intent = new Intent(MenuPrincipal.this, MapsFuturoActivity.class);
        intent.putExtra("correo",correo);
        startActivity(intent);
    }

    public void btnCuentaClick(View view) {
        Intent intent = new Intent(MenuPrincipal.this, AjustesCuenta.class);
        intent.putExtra("intent",2);
        intent.putExtra("correo",correo);
        startActivity(intent);
    }

    public void btnAboutClick(View view) {
        Intent intent = new Intent(MenuPrincipal.this, AcercaDe.class);
        startActivity(intent);
    }

    public void btnInstitucionesClick(View view){
        Intent intent = new Intent(MenuPrincipal.this, MuroInstituciones.class);
        intent.putExtra("id", correo);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_puntos, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            case R.id.Zona:
                    //PLACE PICKER
                goPlacePicker();

                /* OJO:Get datos de shared preferences


                SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                Float f1 = preferences.getFloat("lat", (float) 0.0);
                Float f2 = preferences.getFloat("lon", (float) 0.0);

                System.out.println(f1);
                System.out.println(f2);*/
                return true;
            case R.id.Notificaciones:
                    //DIALOG SÍ NO
                preguntarNotificaciones();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void preguntarNotificaciones(){
        final CharSequence[] items = {"Recibir Notificaciones" };
        final boolean[] items2;

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        boolean notif = preferences.getBoolean("notificaciones", true);

        if (notif){
            items2 = new boolean[]{true};
        }
        else{
            items2 = new boolean[]{false};
        }

// arraylist to keep the selected items
        final ArrayList seletedItems=new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("¿Desea recibir notificaciones?")
                .setMultiChoiceItems(items, items2, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            notificaciones = true;
                            System.out.println(true);

                        } else  {
                            System.out.println(false);
                            notificaciones = false;
                        }
                    }
                }).setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("notificaciones", notificaciones);
                        editor.apply();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                }).create();
        dialog.show();
    }
}
