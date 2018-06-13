package com.example.gerald.informed_city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.s3.model.transform.Unmarshallers;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class crearEvento extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;
    LatLng latLng; //coordenadas
    private Conexion conexion;
    private JSONObject datosJson;
    public String[] datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        datos = new String[] {"Robo o Asalto", "Accidente de Tránsito", "Congestión Vial", "Descarrilamiento de Tren", "Incendio", "Personas Misteriosas en la Zona", "Pleitos o Peleas",
                "Derrumbes", "Inundaciones", "Caída de Objeto", "Persona Desaparecida", "Mascota Desaparecida", "Apagón", "Sin Agua Potable", "Espectáculo en Vía Pública", "Bloqueo de Vía", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);

        //agregado BRYAN
        tvPlace = (TextView) findViewById(R.id.tvPlace);


        //Para guardar
        String data = getIntent().getExtras().getString("id");
        //Toast.makeText(this,data,Toast.LENGTH_LONG).show;
        try {
            JSONObject jsonObject = new JSONObject(data);
            datosJson = jsonObject.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

                //MODIFICAR LAS COORDENADAS ACa
                latLng = place.getLatLng();
            }

        }



    }

    public void GuardarEvento(View view) throws JSONException {

        EditText txtTitulo = findViewById(R.id.txtTitulo);
        String titulito = txtTitulo.getText().toString();
        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        int ind = spinnerCategoria.getSelectedItemPosition();
        String Cat = datos[ind];
        EditText txtDescripcion = findViewById(R.id.txtDescripcion);
        Float lat = convertToFloat(latLng.latitude);
        Float lon = convertToFloat(latLng.longitude);


        conexion = new Conexion();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("categoria",Cat);//str
        jsonObject.put("descripcion",txtDescripcion.getText().toString());//str
        jsonObject.put("latitud",lat);//float
        jsonObject.put("longitud",lon);//float
        jsonObject.put("user_id",Integer.parseInt(datosJson.getString("id")));
        jsonObject.put("title",titulito);

        String result="";
        //
        try {
            result = conexion.execute("https://informedcityapp.herokuapp.com/events","POST",jsonObject.toString()).get();
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }


    }

    public static Float convertToFloat(Double doubleValue) {
        return doubleValue == null ? null : doubleValue.floatValue();
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


/*    public void btnUbicacion(View view) {
        Intent intent = new Intent(crearEvento.this, MapaCrear.class);
        startActivity(intent);
    }*/
}
