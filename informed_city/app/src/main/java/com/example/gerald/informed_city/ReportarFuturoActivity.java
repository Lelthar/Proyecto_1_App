package com.example.gerald.informed_city;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class ReportarFuturoActivity extends AppCompatActivity {

    private EditText editTextMotivo;
    private  EditText editTextDetalles;
    private Button buttonCancelar;
    private Button buttonEnviar;

    private int ID;
    private  Conexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_futuro);
        Fabric.with(this, new Crashlytics());
        ID = getIntent().getExtras().getInt("id");

        editTextDetalles = findViewById(R.id.editTextDetallesF);
        editTextMotivo = findViewById(R.id.editTextMotivoF);
        buttonCancelar = findViewById(R.id.buttonCancelarF);
        buttonEnviar = findViewById(R.id.buttonEnviarF);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guardarReporte();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private  void guardarReporte() throws JSONException {
        String motivo = editTextMotivo.getText().toString();
        String detalles = editTextDetalles.getText().toString();
        if(!motivo.isEmpty() && !detalles.isEmpty())   {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("motivo", motivo);
            jsonParam.put("detalle", detalles);
            jsonParam.put("event_future_id",ID);

            conexion = new Conexion();
            String result="";
            //
            try {
                result = conexion.execute("https://informedcityapp.herokuapp.com/report_event_futures","POST",jsonParam.toString()).get();
            } catch (InterruptedException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            } catch (ExecutionException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            }

            if(result.equals("Created")){
                Toast.makeText(this,"Guardado",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            }
        }else{
            infoMessageDialog("Complete todos los datos.");
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
