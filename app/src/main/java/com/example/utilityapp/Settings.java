package com.example.utilityapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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


        copyright.setText("Weather icons designed by Titusurya / Freepik");
        Pattern pattern = Pattern.compile("[a-zA-Z]");

        Linkify.TransformFilter mentionFilter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return "http://www.freepik.com/";
            }
        };

        String scheme = "";

        Linkify.addLinks(copyright, pattern, scheme, null, mentionFilter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter);
        switchLocation.setEnabled(true);

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

        if (switchLocation.isChecked()) {
            town.setEnabled(false);
            town.setText("");
            country.setEnabled(false);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Settings.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_PERMISSION_LOCATION_SERVICES);

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            final LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    town.setEnabled(false);
                    town.setText("");
                    country.setEnabled(false);
                } else {
                    town.setEnabled(true);
                    country.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(Settings.this, "Permission denied to locations", Toast.LENGTH_SHORT).show();
                    switchLocation.setEnabled(false);
                }
            }
        }
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
        intent.putExtra("longitudecoords", longitude);
        intent.putExtra("latitudecoords", latitude);
        startActivity(intent);
    }

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

        edit.commit();
    }
}
