package com.example.utilityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;

public class Settings extends AppCompatActivity {

    Switch switchLocation;
    EditText town;
    Spinner country;
    String townText;
    int countryInt;
    boolean switchCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        SharedPreferences settings = getSharedPreferences("settings", MODE_APPEND);

        switchLocation = (Switch)findViewById(R.id.locationServicesSwitch);
        town = (EditText)findViewById(R.id.inputTown);
        country = (Spinner)findViewById(R.id.countrySelector);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter);

        if (settings != null) {
            townText = settings.getString("towntext", townText);
            countryInt = settings.getInt("countryint", countryInt);
            switchCondition = settings.getBoolean("switchcondition", switchCondition);

            switchLocation.setChecked(switchCondition);

            if (townText != null) {
                town.setText(townText);
                country.setSelection(countryInt);
            }
        }

        if (switchLocation.isChecked()) {
            town.setEnabled(false);
            town.setText("");
            country.setEnabled(false);
        }

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    town.setEnabled(false);
                    town.setText("");
                    country.setEnabled(false);
                }
                else {
                    town.setEnabled(true);
                    country.setEnabled(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settings_id:
                goToHome(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToHome(MenuItem item) {
        //retrieves data from switch, spinner and editText, checks to see if input is valid
        townText = town.getText().toString();
        switchCondition = switchLocation.isChecked();
        countryInt = country.getSelectedItemPosition();
        //System.out.println(countryText);
        if (Objects.equals(townText, "") && (!switchCondition)) {
            Toast.makeText(this, "You have not entered a town.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("towntext", townText);
        String countryString = country.getSelectedItem().toString();
        intent.putExtra("countrystring", countryString);
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
        countryInt = country.getSelectedItemPosition();
        edit.putInt("countryint", countryInt);
        switchCondition = switchLocation.isChecked();
        edit.putBoolean("switchcondition", switchCondition);
        edit.commit();
    }
}
