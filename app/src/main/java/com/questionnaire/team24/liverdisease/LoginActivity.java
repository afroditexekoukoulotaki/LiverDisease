package com.questionnaire.team24.liverdisease;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    public void checkLogin(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.91.190.186/lesson/api.php/users";
        StringRequest stringRequest = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Display the response string.
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        JSONObject json = null;
                        try {
                            boolean flag = false;
                            json = new JSONObject(response);
                            JSONArray usersRecords = json.getJSONObject("users").getJSONArray("records");
                            for (int i = 0; i < usersRecords.length(); i++) {
                                JSONArray curRecord = usersRecords.getJSONArray(i);
                                //Log.i("USER", curRecord.getString(1));
                                //Log.i("Pass", curRecord.getString(2));
                            }
                            //checkLogin(response);
                            TextView userTextV= (TextView) findViewById(R.id.userText);
                            TextView passTextV= (TextView) findViewById(R.id.passText);
                            for (int i= 0; i< usersRecords.length(); i++)
                            {
                                JSONArray curRecord= usersRecords.getJSONArray(i);
                                //Log.i("USER", curRecord.getString(1));
                                // Log.i("Pass", curRecord.getString(2));
                                if( (curRecord.getString(1).compareTo(userTextV.getText().toString()) ==0) && (curRecord.getString(2).compareTo(passTextV.getText().toString()) ==0) )
                                {
                                    Intent in = new Intent(getApplicationContext(), MenuActivity.class);
                                    in.putExtra("username",curRecord.getString(1));
                                    in.putExtra("id",curRecord.getString(0));

                                    Toast.makeText(getApplicationContext(), "Welcome  "+ curRecord.getString(1) + "!", Toast.LENGTH_LONG).show();
                                    startActivity(in);
                                    flag = true;
                                    break;
                                }
                            }
                            if(!flag)
                                Toast.makeText(getApplicationContext(), "Login Failed.\nPlease try again.", Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error at REST API call.", Toast.LENGTH_LONG).show();
            }


        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }



   /*public void loadMenuActivity(View view)
    {
        Intent i = new Intent(getApplicationContext(),MenuActivity.class);
        startActivity(i);
    }
*/

}
