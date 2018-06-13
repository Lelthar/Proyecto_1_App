package com.example.gerald.informed_city;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
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

    private final int SELECT_PICTURE = 3513;
    private Uri imagenUri;

    private Button botonGuardar;
    private ImageButton botonImagen;

    private Conexion conexion;

    private JSONObject datos_usuario;

    private boolean cargoImagen = false;

    private String path_portada;
    private static final int PERMISSION_REQUEST_CODE=5468;

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

        checkPermisosReadStorage(this);
        datos_usuario = null;
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
                if(intentLlamada==1){
                    try{
                        if(cargoImagen){
                            guardarAjustes();
                        }else{
                            infoMessageDialog("Seleccione una imagen.");
                        }

                    }catch(JSONException e){
                        e.toString();
                    }
                }else{
                    try {

                            modificarUsuarioAjustes(datos_usuario);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });


        botonImagen = findViewById(R.id.ibtImagen);
        botonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagenPerfil();
                cargoImagen=true;
            }
        });

        if(intentLlamada==1){
            correo = getIntent().getExtras().getString("correo");
            contrav = getIntent().getExtras().getString("contra");
            id = Integer.parseInt(getIntent().getExtras().getString("id"));
            correoEdt.setText(correo);
            correoEdt.setEnabled(false);

        }else{
            try {

                JSONObject json_usuario = new JSONObject(getIntent().getExtras().getString("correo"));
                JSONObject data = json_usuario.getJSONObject("data");
                //user_extended
                correo = data.getString("email");
                correoEdt.setText(correo);
                correoEdt.setEnabled(false);

                JSONObject jsonParam = new JSONObject();
                //jsonParam.put("id", data.getString("id"));

                String result="";
                DownLoadTask user_extendeds = new DownLoadTask();
                //
                try {
                    result = user_extendeds.execute().get();
                } catch (InterruptedException e) {
                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                }
                JSONObject json_elemento = null;

                JSONArray datos = new JSONArray(result);
                for(int i = 0; i < datos.length(); i++){
                    JSONObject elemento = datos.getJSONObject(i);
                    if(elemento.getString("email").equals(correo)){
                        json_elemento = elemento;
                    }
                }
                if(json_elemento != null){
                    seudo.setText(json_elemento.getString("nickname"));
                    nombre.setText(json_elemento.getString("name"));
                    Picasso.with(getApplicationContext()).load(json_elemento.getString("image")).into(imagenPers);
                    datos_usuario = json_elemento;
                }


                //seudo.setText(json_elemento.getString("name"));

            } catch (JSONException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            }

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    // ------------------ Elegir imagen para mostrar portada ----------
    public void cargarImagenPerfil(){
        if(Build.VERSION.SDK_INT >=23) {
            if (checkPermission()){
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            }else{
                requestPermission();
            }
        }else{
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri filePath = data.getData();
                if (null != filePath) {
                    try {
                        //portada_pelicula.setImageURI(filePath);
                        path_portada = getFilePath(getApplicationContext(),filePath);
                        imagenPers.setImageURI(Uri.parse(path_portada));
                        Log.d("PATH", filePath.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            /*
            if(requestCode==TOMADA_PICTURE){
                MediaScannerConnection.scanFile(this, new String[]{path_portada}, null,
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {

                            }

                            @Override
                            public void onScanCompleted(String s, Uri uri) {
                                Log.i("Path",s);
                            }
                        });
                bitmap= BitmapFactory.decodeFile(path_portada);
                //infoMessageDialog(path_portada);

            }*/
        }
    }
    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE );
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cargarImagenPerfil();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(getApplicationContext(), "No se puede acceder a la imagenes sin permisos", Toast.LENGTH_SHORT).show();
                    }else{
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Acceso denegado")
                                .setMessage("No se puede acceder a la imagenes sin permisos. Por favor active los permisos en la configuración de la aplicación.")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();//*/
                    }
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
            }
        }
    }
    public void modificarUsuarioAjustes(JSONObject json_user) throws JSONException {
        String nombreValor = nombre.getText().toString();
        String seudoValor = seudo.getText().toString();
        if(!nombreValor.equals("") && !seudoValor.equals("")){

            String path_image;
            if(cargoImagen){

                uploadWithTransferUtility(seudoValor+".jpg",path_portada);
                path_image = "https://s3.amazonaws.com/informedcity-userfiles-mobilehub-283008059/uploads/"+seudoValor+".jpg";
                cargoImagen=false;
            }else{
                path_image = json_user.getString("image");
            }

            conexion = new Conexion();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", nombreValor);
            jsonParam.put("nickname", seudoValor);
            jsonParam.put("image",path_image);
            String result="";
            //
            try {
                result = conexion.execute("https://informedcityapp.herokuapp.com/user_extendeds/"+json_user.getString("id"),"PATCH",jsonParam.toString()).get();
            } catch (InterruptedException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            } catch (ExecutionException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this,"Guardado",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else{
            Toast.makeText(this,"Datos incompletos",Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarAjustes()throws JSONException{
        String nombreValor = nombre.getText().toString();
        String seudoValor = seudo.getText().toString();
        if(!nombreValor.equals("") && !seudoValor.equals("")){

            String path_image="";
            if(cargoImagen){
                uploadWithTransferUtility(seudoValor+".jpg",path_portada);
                path_image = "https://s3.amazonaws.com/informedcity-userfiles-mobilehub-283008059/uploads/"+seudoValor+".jpg";
                cargoImagen=false;
            }

            conexion = new Conexion();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("name", nombreValor);
            jsonParam.put("nickname", seudoValor);
            jsonParam.put("email", correo);
            jsonParam.put("image",path_image);
            jsonParam.put("user_id",id);
            String result="";
            //
            try {
                result = conexion.execute("https://informedcityapp.herokuapp.com/user_extendeds","POST",jsonParam.toString()).get();
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


    public void uploadWithTransferUtility(String s3PathBucket, String filePathStorage) {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload("uploads/" + s3PathBucket, new File(filePathStorage));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });
    }


    public class DownLoadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String xmlString;
            HttpURLConnection urlConnection = null;
            URL url = null;

            try {
                url = new URL("https://informedcityapp.herokuapp.com/user_extendeds.json");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestMethod("GET");
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuilder xmlResponse = new StringBuilder();
                    BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        xmlResponse.append(strLine);
                    }
                    xmlString = xmlResponse.toString();
                    //xmlString += urlConnection.getHeaderField("access-token");
                    input.close();
                    return xmlString;

                }else{
                    return "Error";
                }
            }
            catch (Exception e) {
                return e.toString();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
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

    /**
     * Cuadro de diálogo para mensajes de información.
     * @param message
     */
    private void infoMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_img_diag_info_icon)
                .setMessage(message).setTitle("Información").setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { return; }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}