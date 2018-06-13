package com.example.gerald.informed_city;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SignUp extends AppCompatActivity {

    private Conexion conexion;

    private  Button botonGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        botonGuardar = findViewById(R.id.btRegisSU);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    public  void registrarUsuario(View view) throws JSONException, ExecutionException, InterruptedException {
        EditText correo = findViewById(R.id.edtemailSU);
        EditText contra1 = findViewById(R.id.edtContra1);
        EditText contra2 = findViewById(R.id.edtContra2);
        String correoValor = correo.getText().toString();
        String contra1Valor = contra1.getText().toString();
        String contra2Valor = contra2.getText().toString();
        if(!correoValor.equals("") && !contra1Valor.equals("") && !contra2Valor.equals("")){
            if(contra1Valor.equals(contra2Valor)){
                Intent intent = new Intent(SignUp.this,AjustesCuenta.class);
                intent.putExtra("intent",1);  //Numero del intent que lo invoca.
                intent.putExtra("correo",correoValor);
                intent.putExtra("contra",contra1Valor);
                conexion = new Conexion();
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("email", correoValor);
                jsonParam.put("password", contra1Valor);
                String  result = conexion.execute("https://informedcityapp.herokuapp.com/auth","POST",jsonParam.toString()).get();
                if(result.equals("OK")){

                    DownLoadTask downLoadTask = new DownLoadTask();
                    String resultaJson = downLoadTask.execute(correoValor,contra1Valor).get();
                    //Toast.makeText(this,resultaJson,Toast.LENGTH_LONG).show();

                    if(!resultaJson.equals("Usuario Incorrecto")){
                        JSONObject nuevoJson = new JSONObject(resultaJson);
                        JSONObject data = nuevoJson.getJSONObject("data");
                        String id = data.getString("id");
                        intent.putExtra("id",id);
                        Toast.makeText(this,id,Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }else{
                        Toast.makeText(this,"idError",Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"No coincide la contraseña",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Datos incompletos",Toast.LENGTH_LONG).show();
        }
    }

    public class DownLoadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String xmlString;
            HttpURLConnection urlConnection = null;
            URL url = null;

            try {
                url = new URL("https://informedcityapp.herokuapp.com/auth/sign_in");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestProperty("email",strings[0]);
                urlConnection.setRequestProperty("password",strings[1]);
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestMethod("POST");
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
                    return "Usuario Incorrecto";
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

}
