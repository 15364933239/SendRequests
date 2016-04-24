package com.numetriclabz.sendrequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends Activity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void sendPostRequest(View View) {

        new PostClass(this).execute();
    }

    public class Student {
        public String sid;

        Student(){
            EditText input;
              input = (EditText) findViewById(R.id.sid_input);
            sid = input.getText().toString();

        }
        public String getSid(){
              return sid;
        }
    }

    public void sendGetRequest(View View) {
        new GetClass(this).execute();
    }

    private class PostClass extends AsyncTask<String, String, Void> {

        private final Context context;

        public PostClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("http://10.0.2.2:8080/DBProject-master/javaSendBack.php");
                        //http://10.0.2.2:8080/DBProject-master/javaSendBack.php");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                Student student = new Student();
                String urlParameters = student.getSid();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                final StringBuilder output = new StringBuilder("Request URL " + url);

                //Buffer to get received string from the server
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);}
                br.close();

                // Output msg appended
                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                //Displays the final result on the app
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        outputView.setText(output);
                        progress.dismiss();
                    }
                });


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
               e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }

    }

    private class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("http://10.0.2.2:8080/DBProject-master/javaSendBack.php");

                //http://10.0.2.2:8080/DBProject-master/javaSendBack.php
                //http://requestb.in/17b1oik1
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
               // String urlParameters = "1010110";
                Student student = new Student();
                String urlParameters = student.getSid();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                final StringBuilder output = new StringBuilder("Request URL " + url);

                output.append(System.getProperty("line.separator")  + "Type " + "GET");
                output.append(System.getProperty(urlParameters));
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        outputView.setText(output);
                        progress.dismiss();
                    }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }
}