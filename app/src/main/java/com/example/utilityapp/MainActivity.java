package com.example.utilityapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView weatherForecast;
    TextView weatherForecastDescription;
    TextView weatherTemperature;
    TextView weatherTown;
    TextView timeStamp;
    String townText;
    String countryText;
    Boolean switchCondition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        weatherForecast = (TextView) findViewById(R.id.textWeatherForecast);
        weatherForecastDescription = (TextView) findViewById(R.id.textWeatherForecastDetailed);
        weatherTemperature = (TextView) findViewById(R.id.textTemp);
        weatherTown = (TextView) findViewById(R.id.townText);
        timeStamp = (TextView) findViewById(R.id.timeText);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            townText = extras.getString("towntext");
            countryText = extras.getString("countrystring");
            switchCondition = extras.getBoolean("switchcondition");
            if (townText != null) {
                String countryTextAbbreviation = getAbbreviatedCountry(countryText);
                townText = townText.concat("," + countryTextAbbreviation);
                InitiateTaskWeather task = new InitiateTaskWeather();
                task.execute(townText);
                timeStampCompletion();
            }

            if (switchCondition) {
                System.out.println("hey");
            }
        }
    }

    private String getAbbreviatedCountry(String countryText) {

        String countryTextAbbr = null;

        switch (countryText) {
            case "Australia" : {
                countryTextAbbr = "AU";
                break;
            }
            case "New Zealand" : {
                countryTextAbbr = "NZ";
                break;
            }
            case "United Kingdom" : {
                countryTextAbbr = "UK";
                break;
            }
            case "United States" : {
                countryTextAbbr = "US";
                break;
            }
            case "Canada" : {
                countryTextAbbr = "CA";
                break;
            }
        }
        return countryTextAbbr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settings_id:
                goToSettings(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void timeStampCompletion() {
        Calendar calendar = new GregorianCalendar();
        String time = String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        int amPm = calendar.get(Calendar.AM_PM);
        String amOrPm;
        if (amPm == Calendar.AM) {
            amOrPm = "am";
        } else {
            amOrPm = "pm";
        }
        timeStamp.setText("Data retrieved at " + time + amOrPm);
    }

    public void goToSettings(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private class InitiateTaskWeather extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = null;
            String data = null;

            try {
                data = ((new WeatherAPIConnection()).retrieveWeatherData(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                weather = WeatherParserJSON.getWeather(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            weatherTown.setText(weather.location.getCity());
            weatherForecast.setText(weather.currentCondition.getWeatherCondition());
            weatherForecastDescription.setText(weather.currentCondition.getWeatherConditionDescription());
            float weatherTemperatureFloat = weather.temperature.getTemperature();
            String weatherTemperatureString = String.format("%.0f", weatherTemperatureFloat - 273.15);
            weatherTemperature.setText(weatherTemperatureString + " \u2103");
        }
    }
}