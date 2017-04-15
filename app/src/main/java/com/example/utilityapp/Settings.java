package com.example.utilityapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.*;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings extends AppCompatActivity {

    private final int REQUEST_PERMISSION_LOCATION_SERVICES = 1;

    Switch switchLocation;
    EditText town;
    Spinner country;
    String townText;
    TextView copyright;
    int countryInt;
    boolean switchCondition;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        SharedPreferences settingsSettings = getSharedPreferences("settingssettings", MODE_APPEND);

        switchLocation = (Switch) findViewById(R.id.locationServicesSwitch);
        town = (EditText) findViewById(R.id.inputTown);
        country = (Spinner) findViewById(R.id.countrySelector);
        copyright = (TextView) findViewById(R.id.textCopyright);

        //create clickable link for source for icons
        copyright.setText(R.string.reference);
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Linkify.TransformFilter mentionFilter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return "http://www.freepik.com/";
            }
        };
        String scheme = "";
        Linkify.addLinks(copyright, pattern, scheme, null, mentionFilter);


        //dropdown list of countries
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter);
        switchLocation.setEnabled(true);


        //load saved values, if any
        if (settingsSettings != null) {
            townText = settingsSettings.getString("towntext", townText);
            countryInt = settingsSettings.getInt("countryint", countryInt);
            switchCondition = settingsSettings.getBoolean("switchcondition", switchCondition);

            switchLocation.setChecked(switchCondition);

            if (townText != null) {
                town.setText(townText);
                country.setSelection(countryInt);
            }
        }

        //if location switch is set, set text and load functions
        if (switchLocation.isChecked()) {
            town.setEnabled(false);
            town.setText("");
            country.setEnabled(false);

            locationSettings();
        }

        //listener that checks if switch has been clicked
        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    town.setEnabled(false);
                    town.setText("");
                    country.setEnabled(false);
                    locationSettings();
                } else {
                    town.setEnabled(true);
                    country.setEnabled(true);
                }
            }
        });
    }


    //reference android location settings, and find lat and long of phone
    public void locationSettings() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //location permissions popup
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Settings.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_LOCATION_SERVICES);
            return;
        }

        android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    //permissions outcomes
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION_SERVICES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationSettings();
                } else {
                    Toast.makeText(Settings.this, "This app does not have permission for location services.", Toast.LENGTH_SHORT).show();
                    switchLocation.setEnabled(false);
                }
            }
        }
    }

    //sets up menu button (to send user back to mainactivity)
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

        //loads everything into intent and sends it to mainactivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("towntext", townText);
        String countryString = country.getSelectedItem().toString();
        intent.putExtra("countrystring", countryString);
        intent.putExtra("switchcondition", switchCondition);
        intent.putExtra("longitudecoords", longitude);
        intent.putExtra("latitudecoords", latitude);
        startActivity(intent);
    }


    //saves all details of settings into sharedpreference
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settingsSettings = getSharedPreferences("settingssettings", MODE_APPEND);
        SharedPreferences.Editor edit = settingsSettings.edit();
        edit.clear();
        townText = town.getText().toString();
        edit.putString("towntext", townText);
        countryInt = country.getSelectedItemPosition();
        edit.putInt("countryint", countryInt);
        switchCondition = switchLocation.isChecked();
        edit.putBoolean("switchcondition", switchCondition);

        edit.apply();
    }
}
