package com.example.gerald.informed_city;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private  Button botonGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        botonGuardar = findViewById(R.id.btRegisSU);
    }
    public  void registrarUsuario(View view){
        EditText correo = findViewById(R.id.edtemailSU);
        EditText contra1 = findViewById(R.id.edtContra1);
        EditText contra2 = findViewById(R.id.edtContra2);
        String correoValor = correo.getText().toString();
        String contra1Valor = contra1.getText().toString();
        String contra2Valor = contra2.getText().toString();
        if(!correoValor.equals("") && !contra1Valor.equals("") && contra2Valor.equals("")){

        }else{
            Toast.makeText(this,"Datos incompletos",Toast.LENGTH_SHORT).show();
        }
    }
}
