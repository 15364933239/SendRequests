package com.numetriclabz.sendrequests;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by munkh on 4/24/2016.
 */
public class Display_info extends Activity{
    private ProgressDialog progress;

    View v;
    /*
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
*/

  //  Student student = new Student();

            //student.getSid();

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState); // make sure super.oncreate is called first.
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
                /* Variable Instantiation
                    outputView: Displays the text to the screen?
                    url: Specific Website to connect to, in this case localhost
                    connection: Instantiate the connection to the website, set the needed properties of the site as well
                    urlParameters: The active points that are to be sent to the site. Key Value Pair. "key=Value"
                */


               // final TextView outputView = (TextView) findViewById(R.id.showOutput2);
                URL url = new URL("http://10.0.2.2:8080/DBProject-master/javaSendBack.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // This should be the encapsulated Value that needs to be sent in POST

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
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject course = (JSONObject) array.get(i);
                                    String courses = course.getString("courseName" + " " + "grade" + " " + "yearTaken" + " " + "semester" + " " + "section");
                                    //((TextView) findViewById(R.id.course_list)).setText(courses);

                                }

                                ListView list= (ListView) findViewById(R.id.course_list);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }});
                }


                // Output msg appended
                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + responseOutput.toString());

                responseOutput.toString();

                //Displays the final result on the app




            }  catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute() {
            progress.dismiss();

        }
    }

}
