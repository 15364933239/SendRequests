package com.numetriclabz.sendrequests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by munkh on 4/24/2016.
 */
public class Display_info extends Activity{
    private ProgressDialog progress;

    View v;
    public class Student {
        public String sid;

        Student() {
            EditText input;
            input = (EditText) findViewById(R.id.sid_input);
            sid = input.getText().toString();
        }
        public String getSid() {
            return sid;
        }
    }

    Student student = new Student();
    String urlParameters = student.getSid();

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sendPostRequest((View)findViewById(R.id.showOutput));
    }


    public void sendPostRequest(View View) {

        new PostClass(this).execute();
    }




    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public PostClass(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                /* Variable Instantiation
                    outputView: Displays the text to the screen?
                    url: Specific Website to connect to, in this case localhost
                    connection: Instantiate the connection to the website, set the needed properties of the site as well
                    urlParameters: The active points that are to be sent to the site. Key Value Pair. "key=Value"
                */


                final TextView outputView = (TextView) findViewById(R.id.showOutput);
                URL url = new URL("http://10.0.2.2:8080/DBProject-master/javaSendBack.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // This should be the encapsulated Value that needs to be sent in POST

                urlParameters = "SID=" + urlParameters;

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                final StringBuilder output = new StringBuilder("");

                //Buffer to receive string from the server
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line ;
                StringBuilder responseOutput = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    responseOutput.append(line);
                }
                br.close();

                // Output msg appended
                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + responseOutput.toString());

                responseOutput.toString();

                //Displays the final result on the app


                Display_info.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        outputView.setText(output);
                        progress.dismiss();
                    }
                });

            }  catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }



}
