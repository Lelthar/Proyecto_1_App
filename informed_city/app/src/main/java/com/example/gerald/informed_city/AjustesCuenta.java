package com.example.gerald.informed_city;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

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
    private  int id;

    private EditText seudo;
    private EditText correoEdt;
    private EditText nombre;
    private ImageView imagenPers;

    private final int SELECT_PICTURE = 100;
    private Uri imagenUri;

    private Button botonGuardar;
    private ImageButton botonImagen;

    private Conexion conexion;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermisosReadStorage(final Context context){
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ventanaAutorizacionReadStorage("External storage", context,Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public void ventanaAutorizacionReadStorage(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_cuenta);

        AWSMobileClient.getInstance().initialize(this).execute();

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
            id = Integer.parseInt(getIntent().getExtras().getString("id"));
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

            String path = imagenUri.getLastPathSegment();
            String extension = "";
            if(path.toLowerCase().endsWith(".png")){
                extension = ".png";
            }else if(path.toLowerCase().endsWith(".jpg")){
                extension = ".jpg";
            }else if(path.toLowerCase().endsWith(".jpeg")){
                extension = ".jpeg";
            }else if(path.toLowerCase().endsWith(".gif")){
                extension = ".gif";
            }else{
                extension = ".png";
            }

            uploadWithTransferUtility(imagenUri.getLastPathSegment(),seudoValor,extension);

            conexion = new Conexion();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", nombreValor);
            jsonParam.put("nickname", seudoValor);
            jsonParam.put("email", correo);
            jsonParam.put("image","https://s3.amazonaws.com/informedcity-userfiles-mobilehub-283008059/uploads/"+seudoValor+extension);
            jsonParam.put("user_id",id);
            String result="";
            //
            try {
                result = conexion.execute("https://informedcity.herokuapp.com/user_extendeds","POST",jsonParam.toString()).get();
            } catch (InterruptedException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            } catch (ExecutionException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this,imagenUri.toString(),Toast.LENGTH_SHORT).show();

            //}

        }
    }

    public void uploadWithTransferUtility(String path,String nickname,String extension) {
        if (checkPermisosReadStorage(this)) {
            try{
                BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAI32IBF77PAMW4HCQ", "EdFcDO/Q/tGJdGZiCjSZHdcNaaDlAIiNcKrN7/pD");
                AmazonS3Client s3Client = new AmazonS3Client(credentials);
                TransferUtility transferUtility =
                        TransferUtility.builder()
                                .context(getApplicationContext())
                                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                                .s3Client(s3Client)
                                .build();


                TransferObserver uploadObserver =
                        transferUtility.upload("uploads/"+nickname+extension,new File(path));

                uploadObserver.setTransferListener(new TransferListener() {

                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (TransferState.COMPLETED == state) {

                            //Picasso.with(getApplicationContext()).load("https://s3.amazonaws.com/informedcity-userfiles-mobilehub-283008059/uploads/Nueva1.png").into(imageView);
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                        int percentDone = (int)percentDonef;

                        Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                                + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        //Error

                    }

                });

                if (TransferState.COMPLETED == uploadObserver.getState()) {
                    //Upload listo
                }

            }catch (Exception e){
                Log.d("ERROR: ",e.toString());
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                //textView.setText(e.toString());

            }

        }

    }

}
