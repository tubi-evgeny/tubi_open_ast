package ru.tubi.project.utilites;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class InitialData extends AsyncTask<String,Void,String> {
            // ProgressDialog asyncDialog = new ProgressDialog(this);

  /*  @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }*/


    @Override
    protected String doInBackground(String... strings) {
        String link=strings[0];
        try {
            URL url=new URL(link);
            URLConnection conn=url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());

            wr.flush();

            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String line=null;

            while((line=reader.readLine())!=null){
                sb.append(line);
                break;
            }
            return sb.toString();

        }catch(Exception e){
            Log.d("A111","InitialData / doInBackground / Exception="+e);
            return new String("Exception: "+e.getMessage());
        }
    }
 /*   @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }*/
}