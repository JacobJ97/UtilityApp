package com.example.utilityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    Switch switchLocation;
    EditText postCode;
    String postcodeText;
    boolean switchCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences settings = getSharedPreferences("settings", MODE_APPEND);


        switchLocation = (Switch)findViewById(R.id.locationServicesSwitch);
        postCode = (EditText)findViewById(R.id.inputPostcode);

        if (settings != null) {
            postcodeText = settings.getString("postcodetext", postcodeText);
            switchCondition = settings.getBoolean("switchcondition", switchCondition);

            switchLocation.setChecked(switchCondition);

            if (postcodeText != null) {
                postCode.setText(postcodeText);
            }
        }

        if (switchLocation.isChecked()) {
            postCode.setFocusable(false);
        }

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    postCode.setFocusable(false);
                    postCode.setFocusableInTouchMode(false);
                    postCode.setClickable(false);
                    postCode.setText("");
                }
                else {
                    postCode.setFocusable(true);
                    postCode.setFocusableInTouchMode(true);
                    postCode.setClickable(true);
                }
            }
        });
    }

    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences("settings", MODE_APPEND);
        SharedPreferences.Editor edit = settings.edit();
        edit.clear();
        postcodeText = postCode.getText().toString();
        edit.putString("postcodetext", postcodeText);
        switchCondition = switchLocation.isChecked();
        edit.putBoolean("switchcondition", switchCondition);
        edit.commit();
    }
}
