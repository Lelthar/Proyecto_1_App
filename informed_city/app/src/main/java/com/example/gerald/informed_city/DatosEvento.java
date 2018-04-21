package com.example.gerald.informed_city;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class DatosEvento extends AppCompatActivity {

    private Evento evento;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_evento);
        //int id = getIntent().getExtras().getInt("id");
        //Toast.makeText(this,id,Toast.LENGTH_SHORT).show();
        /*
        evento = new Evento(getIntent().getExtras().getInt("1"),
                getIntent().getExtras().getString("2"),
                getIntent().getExtras().getString("3"),
                getIntent().getExtras().getInt("4"),
                getIntent().getExtras().getString("5"),
                (float)getIntent().getExtras().getDouble("6"),
                (float)getIntent().getExtras().getDouble("7"),
                getIntent().getExtras().getString("8")
                );

        editText1 = findViewById(R.id.editTextNombre);
        editText2 = findViewById(R.id.edtCategoria);
        editText3 = findViewById(R.id.edtHora);
        editText4 = findViewById(R.id.edtConfirmacion);
        editText5 = findViewById(R.id.edtDescripcion);

        editText1.setText(evento.getNombre());
        editText2.setText(evento.getCategoria());
        editText3.setText(evento.getFecha());
        editText4.setText("Sin confirmar");
        editText5.setText(evento.getDescripcion());
        */

        //editText1.setEnabled(false);
        //editText2.setEnabled(false);
        //editText3.setEnabled(false);
        //editText4.setEnabled(false);
        //editText5.setEnabled(false);

    }
}
