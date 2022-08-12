package ru.tubi.project.utilites;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class InitialDataPOST extends AsyncTask<String,Void,String> {

    String res = "";

    @Override
    protected String doInBackground(String... strings) {
        String link=strings[0];
        String param=strings[1];
        try {
            URL obj = new URL(link);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) obj.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            //POST go - START
            httpsURLConnection.setDoOutput(true);
            OutputStream os = httpsURLConnection.getOutputStream();
            os.write(param.getBytes());
            os.flush();
            os.close();
            // POST  - FINISH
            int responseCode = httpsURLConnection.getResponseCode();
            Log.d("A111","POST Response Code ::  " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // get result
                res  = response.toString();
            } else {
                Log.d("A111","error POST request not worked");
            }
        }catch (Exception ex){
            Log.d("A111","error = "+ex);
        }
        return res;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getParamsString(final Map<String, String> params) {
        final StringBuilder result = new StringBuilder();

        params.forEach((name, value) -> {
            try {
                result.append(URLEncoder.encode(name, "UTF-8"));
                result.append('=');
                result.append(URLEncoder.encode(value, "UTF-8"));
                result.append('&');
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        final String resultString = result.toString();
        return !resultString.isEmpty()
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}

/*
 @Override
    protected String doInBackground(String... strings) {
        String link=strings[0];
        String param=strings[1];
        try {
            URL obj = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            //POST go - START
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(param.getBytes());
            os.flush();
            os.close();
            // POST  - FINISH
            int responseCode = httpURLConnection.getResponseCode();
            Log.d("A111","POST Response Code ::  " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // get result
                res  = response.toString();
            } else {
                Log.d("A111","error POST request not worked");
            }
        }catch (Exception ex){
            Log.d("A111","error = "+ex);
        }
        return res;
    }
 */
