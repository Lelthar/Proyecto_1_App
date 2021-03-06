package com.example.gerald.informed_city;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class WebViewNacion extends AppCompatActivity {
    private String url;


    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_nacion);
        Fabric.with(this, new Crashlytics());
        Intent i=getIntent();
        int numero =Integer.valueOf( i.getExtras().getString("periodico"));


        switch(numero){
            case 1:
                url = "https://www.nacion.com/sucesos/";
                break;
            case 2:
                url = "https://www.laprensalibre.cr/Noticias/cover/2/nacional";
                break;
            case 3:
                url = "http://www.diarioextra.com/Seccion/sucesos";
                break;
            case 4:
                url = "https://www.larepublica.net/seccion/nacionales";
                break;
            case 5:
                url = "https://www.crhoy.com/site/dist/seccion-nacionales.html#/sucesos";
                break;
            case 6:
                url = "https://www.lateja.cr/sucesos/";
                break;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        web = findViewById(R.id.paginaNacion);
        web.setWebViewClient(new MyAppWebViewClient());

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl(url);

        //int numero=0; //NUMERO DEBE SER LLAMADO DESDE LA OTRA VENTANA


    }

    /*public void onDestroy() {

        super.onDestroy();
    }*/

    public void cerrarVentanaClicked(View view){
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (web.canGoBack()) {
                        web.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyAppWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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
