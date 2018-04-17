package com.example.gerald.pruebalogin;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownLoadTask downLoadTask = new DownLoadTask();
        try {
            String result = downLoadTask.execute().get();
            TextView textView = findViewById(R.id.text_view1);
            textView.setText(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class DownLoadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String xmlString;
            HttpURLConnection urlConnection = null;
            URL url = null;

            try {
                url = new URL("https://informedcity.herokuapp.com/auth/sign_in");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestProperty("email","geraldm1998@hotmail.com");
                urlConnection.setRequestProperty("password","gerald123");
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
                    return "error1";
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
}
