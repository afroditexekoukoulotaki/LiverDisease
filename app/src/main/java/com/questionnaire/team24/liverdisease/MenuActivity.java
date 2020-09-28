package com.questionnaire.team24.liverdisease;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


    }

    public void loadMeldScoreActivity(View view)
    {
        Intent i1 = getIntent();
        String id = i1.getStringExtra("id");
        String username = i1.getStringExtra("username");

        Intent i2 = new Intent(getApplicationContext(),MeldScoreActivity.class);
        i2.putExtra("userId", id);
        i2.putExtra("username", username);
        startActivity(i2);
    }

    public void loadDemographicsActivity(View view)
    {
        Intent i1 = getIntent();
        String id = i1.getStringExtra("id");
        String username = i1.getStringExtra("username");

        Intent i2 = new Intent(getApplicationContext(),DemographicsActivity.class);
        i2.putExtra("userId", id);
        i2.putExtra("username", username);
        startActivity(i2);
    }

    public void logout(View view) {
        Intent in = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(in);

    }

}
