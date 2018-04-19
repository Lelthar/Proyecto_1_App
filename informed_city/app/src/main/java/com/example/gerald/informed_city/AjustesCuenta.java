package com.example.gerald.informed_city;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;



public class AjustesCuenta extends AppCompatActivity {

    private String correo;
    private String contrav;
    private int intentLlamada;

    private EditText seudo;
    private EditText correoEdt;
    private EditText nombre;
    private ImageView imagenPers;

    private final int SELECT_PICTURE = 100;
    private Uri imagenUri;

    private Button botonGuardar;
    private ImageButton botonImagen;

    private Conexion conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_cuenta);

        seudo = findViewById(R.id.editTextSeudonimo);
        correoEdt = findViewById(R.id.editTextContra);
        nombre = findViewById(R.id.editTextNombre);

        imagenPers = findViewById(R.id.imgVFotoP);

        intentLlamada = getIntent().getExtras().getInt("intent"); //Saber que intent lo esta invocando

        botonGuardar = findViewById(R.id.btGuardaAjustes);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    guardarAjustes();
                }catch(JSONException e){
                    e.toString();
                }

            }
        });

        botonImagen = findViewById(R.id.ibtImagen);
        botonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

        if(intentLlamada==1){
            correo = getIntent().getExtras().getString("correo");
            contrav = getIntent().getExtras().getString("contra");
            correoEdt.setText(correo);
            correoEdt.setEnabled(false);
        }else{
            correo = getIntent().getExtras().getString("correo");
            correoEdt.setText(correo);
            correoEdt.setEnabled(false);
        }


    }

    public void guardarAjustes()throws JSONException{
        String nombreValor = nombre.getText().toString();
        String seudoValor = seudo.getText().toString();
        if(!nombreValor.equals("") && !seudoValor.equals("")){
            conexion = new Conexion();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", nombreValor);
            jsonParam.put("nickname", seudoValor);
            jsonParam.put("email", correo);
            jsonParam.put("imagen","Path");
            jsonParam.put("user_id",1);
            String result="";
            try {
                result = conexion.execute("https://informedcity.herokuapp.com/user_extendeds","POST",jsonParam.toString()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(result.equals("Created")){
                Toast.makeText(this,"Guardado",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AjustesCuenta.this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this,"Guardar Datos",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Datos incompletos",Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione una imagen"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //if(requestCode==SELECT_PICTURE){
                imagenUri = data.getData();
                imagenPers.setImageURI(imagenUri);
            //}

        }
    }

}
