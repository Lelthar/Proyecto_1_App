package com.example.gerald.informed_city;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    private ArrayList<Evento> listaEventos;
    private String correo;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        correo = getIntent().getExtras().getString("correo");
        listaEventos = new ArrayList<>();
        //Evento evento = new Evento(1,"EVENTO NUMERO 1","Es un evento raro",5,"Incendio",9.869893f, -83.910499f);
        //Evento evento1 = new Evento(1,"EVENTO NUMERO 2","Es un evento raro2",5,"Choque",9.868540f, -83.910499f);
        //listaEventos.add(evento);
        //listaEventos.add(evento1);
        cargarEventos();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void enviarDatos(Marker arg0){
        try{
            if(arg0 != null){
                String marcaSeleccionada = arg0.getTitle();
                int num = buscarEvento(marcaSeleccionada);
                if(num!=-1){
                    Evento eventoSelec = listaEventos.get(num);
                    Intent intent1 = new Intent(MapsActivity.this, DatosEvento.class);
                    //Toast.makeText(this,eventoSelec.getNombre(),Toast.LENGTH_LONG).show();

                    intent1.putExtra("id",eventoSelec.getNumero());
                    intent1.putExtra("2",eventoSelec.getNombre());
                    intent1.putExtra("3",eventoSelec.getDescripcion());
                    // intent1.putExtra("4",eventoSelec.getCreador());
                    intent1.putExtra("5",eventoSelec.getCategoria());
                    // intent1.putExtra("6",eventoSelec.getLatitud());
                    // intent1.putExtra("7",eventoSelec.getLongitud());
                    intent1.putExtra("8",eventoSelec.getFecha());
                    intent1.putExtra("9",eventoSelec.getCantidadReportes());
                    intent1.putExtra("correo",correo);
                    startActivity(intent1);
                }

            }
        }catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                enviarDatos(arg0);

                return  false;
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.clear();
                for(int i=0;i<listaEventos.size();i++){
                    Evento evento = listaEventos.get(i);
                    LatLng posicion = new LatLng(evento.getLatitud(),evento.getLongitud());
                    MarkerOptions marca = new MarkerOptions();
                    mMap.addMarker(new MarkerOptions().position(posicion).title(evento.getNombre()).snippet(evento.getCategoria())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }

                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Usted está acá"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        try{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 0);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                mMap.clear();

                for(int i=0;i<listaEventos.size();i++){
                    Evento evento = listaEventos.get(i);
                    LatLng posicion = new LatLng(evento.getLatitud(),evento.getLongitud());
                    MarkerOptions marca = new MarkerOptions();
                    mMap.addMarker(new MarkerOptions().position(posicion).title(evento.getNombre()).snippet(evento.getCategoria())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }

                LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(location).title("Usted está acá Inicio"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15.5f));
                //mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        }catch (Exception excepcion){
            Toast.makeText(this, "No se cargo ubicación correctamente.", Toast.LENGTH_SHORT).show();
        }

    }

    public void cargarEventos(){
        String result="";
        DownLoadTask user_extendeds = new DownLoadTask();
        //
        try {
            result = user_extendeds.execute().get();
            //      Toast.makeText(this,result,Toast.LENGTH_SHORT).show();

            JSONArray marcas = new JSONArray(result);

            for(int i= 0;i<marcas.length();i++){
                String valorJson = marcas.getString(i);
                JSONObject marca = new JSONObject(valorJson);
                String nombre = marca.getString("title");
                String categoria = marca.getString("categoria");
                String descrpcion = marca.getString("descripcion");
                float latitud = (float) marca.getDouble("latitud");
                float longitud = (float) marca.getDouble("longitud");
                int numero = marca.getInt("user_id");
                int id=marca.getInt("id");
                String fecha = marca.getString("created_at");
                int cantidadReportes = marca.getInt("confirmacion");
                //Toast.makeText(this,fecha,Toast.LENGTH_SHORT).show();
                //Toast.makeText(this,id+"|"+numero+"|"+nombre+"|"+categoria+"|"+descrpcion+"|"+latitud+"|"+longitud,Toast.LENGTH_LONG).show();
                Evento evento = new Evento(id,
                        nombre,
                        descrpcion,
                        numero,
                        categoria,
                        latitud,
                        longitud,
                        fecha,cantidadReportes);
                listaEventos.add(evento);

            }
        } catch (InterruptedException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class DownLoadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String xmlString;
            HttpURLConnection urlConnection = null;
            URL url = null;
            try {
                url = new URL("https://informedcityapp.herokuapp.com/events.json");
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
    public int buscarEvento(String evento){
        int resutl=-1;
        for(int i=0;i<listaEventos.size();i++){
            Evento event = listaEventos.get(i);
            if(event.getNombre().equals(evento)){
                return i;
            }
        }
        return  resutl;
    }
}
