package com.example.utilityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    String postCodeText;
    Boolean switchCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            postCodeText = extras.getString("postcodtext", postCodeText);
            switchCondition = extras.getBoolean("switchcondition", switchCondition);
            if (postCodeText != null) {
                // TODO: 18/03/2017 send postcode over httprequest to weather site, return with result
            }
            if (switchCondition) {
                // TODO: 18/03/2017  use location services to find weather conditions and return results
            }
        }
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
