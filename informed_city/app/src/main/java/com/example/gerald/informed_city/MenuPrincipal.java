package com.example.gerald.informed_city;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MenuPrincipal extends AppCompatActivity {

    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        correo = getIntent().getExtras().getString("correo");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
