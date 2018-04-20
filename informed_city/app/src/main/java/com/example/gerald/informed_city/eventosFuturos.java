package com.example.gerald.informed_city;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.Calendar;

public class eventosFuturos extends AppCompatActivity {

    //agregado BRYAN
    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;
    Button btnPlace;
    LatLng latLng; //coordenadas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_futuros);

        //TIEMPO
        //now we go to create object from class
        final DialogFragment dialogFragment = new DatePickerDialogTheme4();


        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        String[] datos = new String[] {"Robo o Asalto", "Accidente de Tránsito", "Congestión Vial", "Descarrilamiento de Tren", "Incendio", "Personas Misteriosas en la Zona", "Pleitos o Peleas",
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
            textView.setText(day+"/"+(month+1)+"/"+year);//el mes inicia en 0
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
}
