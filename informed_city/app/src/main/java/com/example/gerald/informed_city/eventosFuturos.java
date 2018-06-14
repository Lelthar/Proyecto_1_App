package com.example.gerald.informed_city;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class eventosFuturos extends AppCompatActivity {

    //agregado BRYAN
    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;
    Button btnPlace;
    LatLng latLng; //coordenadas
    private Conexion conexion;
    private JSONObject datosJson;
    String[] datos;
    public static String fecha;
    private MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_futuros);
        Fabric.with(this, new Crashlytics());
        mixpanel = MixpanelAPI.getInstance(this,"d61a1009087902b272677b74ff7ff8f6");
        mixpanel.track("Ventana Crear eventos futuros",null);
        mixpanel.flush();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //TIEMPO
        //now we go to create object from class
        final DialogFragment dialogFragment = new DatePickerDialogTheme4();


        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        datos = new String[] {"Robo o Asalto", "Accidente de Tránsito", "Congestión Vial", "Descarrilamiento de Tren", "Incendio", "Personas Misteriosas en la Zona", "Pleitos o Peleas",
                "Derrumbes", "Inundaciones", "Caída de Objeto", "Persona Desaparecida", "Mascota Desaparecida", "Apagón", "Sin Agua Potable", "Espectáculo en Vía Pública", "Bloqueo de Vía", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);



        //agregado BRYAN
        tvPlace = (TextView) findViewById(R.id.tvPlace);
        btnPlace = findViewById(R.id.btnPlace);


        //agregado TIEMPO
        Button btnFecha = findViewById(R.id.btnFecha);
        //make  action to editText

        btnFecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                dialogFragment.show(getFragmentManager(),"Theme 4");
            }
        });

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


    //agregado TIEMPO
    //Now I will create Calendar Class
    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override//TIEMPO
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this,year,month,day);
            return datePickerDialog;
        }

        @Override//TIEMPO
        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView textView = (TextView)getActivity().findViewById(R.id.btnFecha);
            textView.setText(day+"-"+(month+1)+"-"+year);//el mes inicia en 0
            fecha = String.valueOf(day)+"/"+(String.valueOf(month)+1)+"/"+String.valueOf(year);
        }
    }



    public void goPlacePicker(View view){

        //agregado BRYAN
        //This is the place to call the place picker function

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try{

            startActivityForResult(builder.build(eventosFuturos.this),PLACE_PICKER_REQUEST);

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

                Place place = PlacePicker.getPlace(eventosFuturos.this,data);

                btnPlace.setText(place.getAddress());//getLatLng().toString()

                //MODIFICAR LAS COORDENADAS ACÁ
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
        String fechaEvento = fecha + " 12:00:00";

/*
        ["id", "categoria", "descripcion", "latitud", "longitud", "user_id", "created_at", "updated_at", "fecha"]
        Mae seria estos los valores que deberia de tener la consulta ["categoria", "descripcion", "latitud", "longitud", "user_id", "fecha"]

 */


        conexion = new Conexion();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("categoria",Cat);//str
        jsonObject.put("descripcion",txtDescripcion.getText().toString());//str
        jsonObject.put("latitud",lat);//float
        jsonObject.put("longitud",lon);//float
        jsonObject.put("user_id",Integer.parseInt(datosJson.getString("id")));
        jsonObject.put("fecha",fechaEvento);
        jsonObject.put("title",titulito);

        String result="";
        //
        try {
            result = conexion.execute("https://informedcityapp.herokuapp.com/event_futures","POST",jsonObject.toString()).get();
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
}
