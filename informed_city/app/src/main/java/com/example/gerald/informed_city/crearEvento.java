package com.example.gerald.informed_city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.services.s3.model.transform.Unmarshallers;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

public class crearEvento extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;
    LatLng latLng; //coordenadas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        String[] datos = new String[] {"Robo o Asalto", "Accidente de Tránsito", "Congestión Vial", "Descarrilamiento de Tren", "Incendio", "Personas Misteriosas en la Zona", "Pleitos o Peleas",
                "Derrumbes", "Inundaciones", "Caída de Objeto", "Persona Desaparecida", "Mascota Desaparecida", "Apagón", "Sin Agua Potable", "Espectáculo en Vía Pública", "Bloqueo de Vía", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);

        //agregado BRYAN
        tvPlace = (TextView) findViewById(R.id.tvPlace);
    }


    public void goPlacePicker(View view){

        //agregado BRYAN
        //This is the place to call the place picker function

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try{

            startActivityForResult(builder.build(crearEvento.this),PLACE_PICKER_REQUEST);

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

                Place place = PlacePicker.getPlace(crearEvento.this,data);
                tvPlace.setText(place.getAddress());//getLatLng().toString()

                //MODIFICAR LAS COORDENADAS ACÁ
                latLng = place.getLatLng();
            }

        }



    }



/*    public void btnUbicacion(View view) {
        Intent intent = new Intent(crearEvento.this, MapaCrear.class);
        startActivity(intent);
    }*/
}
