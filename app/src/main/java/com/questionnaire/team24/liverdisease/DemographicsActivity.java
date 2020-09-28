package com.questionnaire.team24.liverdisease;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DemographicsActivity extends AppCompatActivity {
    boolean demogDataExists = false;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        //int id = Integer.valueOf(str);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.91.190.186/lesson/api.php/demographics/" + id;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        try {
                            JSONObject json = new JSONObject(response);
                            TextView nameTextV = (TextView) findViewById(R.id.editFirstName);
                            TextView lastnameTextV = (TextView) findViewById(R.id.editLastName);
                            TextView genderTextV = (TextView) findViewById(R.id.editGender);
                            TextView dateV = (TextView) findViewById(R.id.editBirthday);
                            TextView cityV = (TextView) findViewById(R.id.editCity);
                            TextView ethnicityV = (TextView) findViewById(R.id.editEthnicity);

                            nameTextV.setText(json.get("name").toString());
                            lastnameTextV.setText(json.get("lastname").toString());
                            genderTextV.setText(json.get("gender").toString());
                            dateV.setText(json.get("birthday").toString());
                            cityV.setText(json.get("city").toString());
                            ethnicityV.setText(json.get("ethnicity").toString());
                            demogDataExists = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please provide your demographics data.", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void logout(View view) {
        Intent in = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(in);

    }
// /*
    public void updateDemographics(View view){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.91.190.186/lesson/api.php/demographics/" + id;
        // Request a string response from the provided URL.
        int method = Request.Method.PUT;
        if (!demogDataExists) {
            url = "http://139.91.190.186/lesson/api.php/demographics/";
            method = Request.Method.POST;
        }
        StringRequest stringRequest = new StringRequest(method, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Demographics data updated.", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String responseBody = error.networkResponse.data.toString();
            Toast.makeText(getApplicationContext(), "Error saving your data to the DB.\nPlease try again.\n"+responseBody, Toast.LENGTH_LONG).show();
        }
    }){
            @Override
            protected Map<String, String> getParams() {
                TextView nameTextV = (TextView) findViewById(R.id.editFirstName);
                TextView lastnameTextV = (TextView) findViewById(R.id.editLastName);
                TextView genderTextV = (TextView) findViewById(R.id.editGender);
                TextView dateV = (TextView) findViewById(R.id.editBirthday);
                TextView cityV = (TextView) findViewById(R.id.editCity);
                TextView ethnicityV = (TextView) findViewById(R.id.editEthnicity);


                Map<String, String> params= new HashMap<String, String>();
                params.put("id", id);
                params.put("userID", id);
                params.put("name", nameTextV.getText().toString() );
                params.put("lastname",lastnameTextV.getText().toString());
                params.put("birthday",dateV.getText().toString());
                params.put("gender",genderTextV.getText().toString());
                params.put("city",cityV.getText().toString());
                params.put("ethnicity",ethnicityV.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
//    */
}


