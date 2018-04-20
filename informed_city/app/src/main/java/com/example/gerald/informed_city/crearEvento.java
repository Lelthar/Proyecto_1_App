package com.example.gerald.informed_city;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class crearEvento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategoria);
        String[] datos = new String[] {"Robo o Asalto", "Accidente de Tránsito", "Congestión Vial", "Descarrilamiento de Tren", "Incendio", "Personas Misteriosas en la Zona", "Pleitos o Peleas",
                "Derrumbes", "Inundaciones", "Caída de Objeto", "Persona Desaparecida", "Mascota Desaparecida", "Apagón", "Sin Agua Potable", "Espectáculo en Vía Pública", "Bloqueo de Vía", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);


    }

    public void btnUbicacion(View view) {
        Intent intent = new Intent(crearEvento.this, MapaCrear.class);
        startActivity(intent);
    }
}
