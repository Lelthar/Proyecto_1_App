package com.example.gerald.informed_city;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuPrincipal extends AppCompatActivity {

    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        correo = getIntent().getExtras().getString("correo");
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
        Intent intent = new Intent(MenuPrincipal.this, WebViewNacion.class);
        intent.putExtra("id", correo);
        startActivity(intent);
    }

}
