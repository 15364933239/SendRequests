package com.example.munkh.dbproject_androidapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by munkh on 4/24/2016.
 */
public class Display_info extends Activity{
    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sendPostRequest(this);
    }

    public void sendPostRequest(Context c) {
        new PostClass(c).execute();
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

                URL url = new URL("http://10.0.2.2:8080/DBProject-master/javaSendBack.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Bundle b = getIntent().getExtras();
                String urlParameters =  b.getString("sid");
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
                final StringBuilder responseOutput = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                if(responseOutput != null){
                    Display_info.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(responseOutput.toString());

                                String name = obj.getString("Name");
                                ((TextView) findViewById(R.id.name)).setText(name);
                                String grad = obj.getString("grad");
                                ((TextView) findViewById(R.id.grad)).setText(grad);
                                String gpa = obj.getString("GPA");
                                ((TextView) findViewById(R.id.gpa)).setText(gpa);
                                String Student_id = obj.getString("StudentID");
                                ((TextView) findViewById(R.id.student_id)).setText(Student_id);
                                String Advisor = obj.getString("Advisor");
                                ((TextView) findViewById(R.id.adviser)).setText(Advisor);
                                String major = obj.getString("Major");
                                ((TextView) findViewById(R.id.major)).setText(major);
                                String career = obj.getString("career");
                                ((TextView) findViewById(R.id.career)).setText(career);
                                String degree = obj.getString("degreeHeld");
                                ((TextView) findViewById(R.id.degree)).setText(degree);


                                JSONArray array = obj.getJSONArray("courseList");
                                String[] arr_str = new String[100];
                                String courses = " ";
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject course = (JSONObject) array.get(i);
                                    courses = course.getString("courseName") + "\t\t\t\t" + "/";
                                    courses = courses.concat(course.getString("grade")+ "\t"+ "/");
                                    courses = courses.concat(course.getString("yearTaken")+ "\t"+ "/");
                                    courses = courses.concat(course.getString("semester")+ "\t"+ "/");
                                    courses = courses.concat(course.getString("section")+ "\n");
                                    arr_str[i] = courses;
                                    ((TextView) findViewById(R.id.one)).setText(arr_str[1]);
                                    ((TextView) findViewById(R.id.two)).setText(arr_str[2]);

                                    ((TextView) findViewById(R.id.three)).setText(arr_str[3]);
                                    ((TextView) findViewById(R.id.four)).setText(arr_str[4]);

                                    ((TextView) findViewById(R.id.five)).setText(arr_str[5]);
                                    ((TextView) findViewById(R.id.six)).setText(arr_str[6]);
                                    ((TextView) findViewById(R.id.seven)).setText(arr_str[7]);
                                    ((TextView) findViewById(R.id.eight)).setText(arr_str[8]);
                                    ((TextView) findViewById(R.id.nine)).setText(arr_str[9]);
                                    ((TextView) findViewById(R.id.ten)).setText(arr_str[10]);
                                    ((TextView) findViewById(R.id.eleven)).setText(arr_str[11]);
                                    ((TextView) findViewById(R.id.twelve)).setText(arr_str[12]);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }});
                }

                // Output msg appended
                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + responseOutput.toString());

                responseOutput.toString();
            }  catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            onPostExecute();
            return null;
        }
        protected void onPostExecute() {
            progress.dismiss();
        }
    }
}
