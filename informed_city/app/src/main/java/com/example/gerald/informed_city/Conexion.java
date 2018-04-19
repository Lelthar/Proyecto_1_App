package com.example.gerald.informed_city;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Conexion extends AsyncTask<String, Void, String>{
    protected String doInBackground(String... strings) {
        URL url = null;
        try {
            url = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(strings[1]);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(strings[2]);

            os.flush();
            os.close();

            return conn.getResponseMessage();
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }

    }
}
