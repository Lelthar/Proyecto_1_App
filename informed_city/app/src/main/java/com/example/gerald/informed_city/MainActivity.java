package com.example.gerald.informed_city;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapa = findViewById(R.id.btMapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mapa = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(mapa);
            }
        });
    }
}
