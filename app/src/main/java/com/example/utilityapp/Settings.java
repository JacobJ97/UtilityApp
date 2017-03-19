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
    EditText town;
    String townText;
    boolean switchCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences settings = getSharedPreferences("settings", MODE_APPEND);


        switchLocation = (Switch)findViewById(R.id.locationServicesSwitch);
        town = (EditText)findViewById(R.id.inputPostcode);

        if (settings != null) {
            townText = settings.getString("towntext", townText);
            switchCondition = settings.getBoolean("switchcondition", switchCondition);

            switchLocation.setChecked(switchCondition);

            if (townText != null) {
                town.setText(townText);
            }
        }

        if (switchLocation.isChecked()) {
            town.setFocusable(false);
        }

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    town.setFocusable(false);
                    town.setFocusableInTouchMode(false);
                    town.setClickable(false);
                    town.setText("");
                }
                else {
                    town.setFocusable(true);
                    town.setFocusableInTouchMode(true);
                    town.setClickable(true);
                }
            }
        });
    }

    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        townText = town.getText().toString();
        switchCondition = switchLocation.isChecked();
        intent.putExtra("towntext", townText);
        intent.putExtra("switchcondition", switchCondition);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences("settings", MODE_APPEND);
        SharedPreferences.Editor edit = settings.edit();
        edit.clear();
        townText = town.getText().toString();
        edit.putString("towntext", townText);
        switchCondition = switchLocation.isChecked();
        edit.putBoolean("switchcondition", switchCondition);
        edit.commit();
    }
}
