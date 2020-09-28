package com.questionnaire.team24.liverdisease;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
//import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Date.*;
//import org.joda.time.DateTime;
//import org.joda.time;

import static java.lang.Math.log;
import static java.lang.Math.round;

public class MeldScoreActivity extends AppCompatActivity {
    boolean dataExists = false;
    String id = "", username = "";
    String strResults = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meldscore);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        username = i.getStringExtra("username");
    }
    public void logout(View view) {        Intent in = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(in);

    }

    public void calculate(View view){
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox);
        EditText creatinine = (EditText) findViewById(R.id.editCreatine);
        EditText bilirubin = (EditText) findViewById(R.id.editBilirubin);
        EditText inr = (EditText) findViewById(R.id.editINR);
        EditText sodium = (EditText) findViewById(R.id.editSodium);

        //String strCheckBox = checkBox1.getText().toString();
        boolean bCheckBox = checkBox1.isChecked();
        String strCreatinine = creatinine.getText().toString();
        String strBilirubin = bilirubin.getText().toString();
        String strInr = inr.getText().toString();
        String strSodium = sodium.getText().toString();

        try {
            //double dCreatinine = (new Double(strCreatinine)).doubleValue();
            double dCreatinine = Double.valueOf(strCreatinine);
            double dBilirubin = Double.valueOf(strBilirubin);
            double dInr = Double.valueOf(strInr);
            double dSodium = Double.valueOf(strSodium);

            //Additional rules:
            if(dBilirubin < 1)
                dBilirubin = 1.;
            if(dCreatinine < 1)
                dCreatinine = 1.;
            if(dInr < 1)
                dInr = 1.;
            if(dCreatinine > 4 || bCheckBox)
                dCreatinine = 4.;
            if(dSodium < 125 )
                dSodium = 125;
            else if(dSodium > 137)
                dSodium = 137;

            double meld = 0.957 * log(dCreatinine) + 0.378 * log(dBilirubin) + 1.120 * log(dInr) + 0.643;
            //Then, round to the tenth decimal place and multiply by 10
            double meldTemp = meld * 10000000000.0;
            meldTemp = round(meldTemp);
            meld = meldTemp / 10000000000.0;
            meld = meld * 10;

            //If MELD(i) > 11, perform additional MELD calculation as follows:
            //MELD = MELD(i) + 1.32 × (137 – Na) –  [ 0.033 × MELD(i) × (137 – Na) ]
            if(meld > 11)
                meld = meld + 1.32 * (137 - dSodium) - ( 0.033 * meld * (137 - dSodium) );

            meldTemp = meld * 10000000000.0;
            meldTemp = round(meldTemp);
            meld = meldTemp / 10000000000.0;

            //Toast.makeText(getApplicationContext() , meld + "", Toast.LENGTH_SHORT).show();
            int meldint = (int) round(meld);
            //Toast.makeText(getApplicationContext() , meldint + "", Toast.LENGTH_SHORT).show();

            String mortality;
            if(meldint <= 9)
                mortality = "1.9%";
            else if( meldint <= 19)
                mortality = "6%";
            else if( meldint <= 29)
                mortality = "19.6%";
            else if( meldint <= 39)
                mortality = "52.6%";
            else
                mortality = "71.3%";

            strResults = "MELD score " + meldint + " Mortality " + mortality;
            Toast.makeText(getApplicationContext() , "" + strResults, Toast.LENGTH_LONG).show();


            //update results to mdcalcresults

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://139.91.190.186/lesson/api.php/mdcalcresults/" + id;
            // Request a string response from the provided URL.

            int method = Request.Method.PUT;
            /*
            if (!dataExists) {
                url = "http://139.91.190.186/lesson/api.php/mdcalcresults";
                method = Request.Method.POST;
            }
*/

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "MELD score results updated.", Toast.LENGTH_LONG).show();
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
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
                    Date now = new Date();
                    String strDate = sdfDate.format(now);

                    Map<String, String> params= new HashMap<String, String>();
                    //params.put("id", id);
                    params.put("userId", id);
                    params.put("username", username );
                    params.put("date", strDate);
                    params.put("results",strResults);
                    params.put("calculatorName","MELD Score (Model For End-Stage Liver Disease) (12 and older)");
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

            }

            catch(NumberFormatException ex){
                Toast.makeText(getApplicationContext() , "Please provide numbers as input.", Toast.LENGTH_LONG).show();
            }

    }

}
