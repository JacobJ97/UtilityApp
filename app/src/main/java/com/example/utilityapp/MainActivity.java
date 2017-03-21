package com.example.utilityapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    TextView settingsLabel;
    Button clearButton;
    Button reloadButton;
    String townText;
    String countryText;
    String countryTextSaved;
    String weatherTownText;
    String weatherTemperatureText;
    String weatherForecastText;
    String weatherForecastDescriptionText;
    String timeStampText;
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
        reloadButton = (Button) findViewById(R.id.reloadButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        settingsLabel = (TextView) findViewById(R.id.textSettings);

        SharedPreferences settingsMain = getSharedPreferences("settingsmain", MODE_APPEND);
        Bundle extras = getIntent().getExtras();

        if (settingsMain != null) {
            weatherTownText = settingsMain.getString("weathertowntext", weatherTownText);
            weatherTemperatureText = settingsMain.getString("weathertemperaturetext", weatherTemperatureText);
            weatherForecastText = settingsMain.getString("weatherforecasttext", weatherForecastText);
            weatherForecastDescriptionText = settingsMain.getString("weatherforecastdescriptiontext", weatherForecastDescriptionText);
            timeStampText = settingsMain.getString("timestamptext", timeStampText);
            countryTextSaved = settingsMain.getString("countrytextsaved", countryTextSaved);

            weatherTown.setText(weatherTownText);
            weatherTemperature.setText(weatherTemperatureText);
            weatherForecast.setText(weatherForecastText);
            weatherForecastDescription.setText(weatherForecastDescriptionText);
            timeStamp.setText(timeStampText);

            makeViewsAppear();
        }

        if (extras != null) {
            makeViewsAppear();

            townText = extras.getString("towntext");
            countryText = extras.getString("countrystring");
            countryTextSaved = countryText;
            switchCondition = extras.getBoolean("switchcondition");

            if (townText != null) {
                getWeatherData(townText, countryText);
            }

            if (switchCondition) {
                System.out.println("hey");
            }
        }
    }

    private void getWeatherData(String townTextMethod, String countryTextMethod ) {
        String countryTextAbbreviation = getAbbreviatedCountry(countryTextMethod);
        String townTextMethodFinal = townTextMethod.concat("," + countryTextAbbreviation);
        timeStampSent();
        InitiateTaskWeather task = new InitiateTaskWeather();
        task.execute(townTextMethodFinal);
    }

    private String getAbbreviatedCountry(String countryText) {

        String countryTextAbbr = null;

        switch (countryText) {
            case "Australia": {
                countryTextAbbr = "AU";
                break;
            }
            case "New Zealand": {
                countryTextAbbr = "NZ";
                break;
            }
            case "United Kingdom": {
                countryTextAbbr = "UK";
                break;
            }
            case "United States": {
                countryTextAbbr = "US";
                break;
            }
            case "Canada": {
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

    public void timeStampSent() {
        Calendar calendar = new GregorianCalendar();
        String time = String.format("%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        int amPm = calendar.get(Calendar.AM_PM);
        String amOrPm;
        if (amPm == Calendar.AM) {
            amOrPm = "am";
        } else {
            amOrPm = "pm";
        }
        timeStamp.setText("Request sent at " + time + amOrPm);
    }

    public void goToSettings(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void reloadWeather(View view) {
        makeViewsDisappear();
        getWeatherData(weatherTownText, countryText);
        makeViewsAppear();
    }

    public void clearWeather(View view) {
        makeViewsDisappear();
    }

    private void makeViewsDisappear() {
        imageView.setVisibility(View.GONE);
        weatherForecast.setVisibility(View.GONE);
        weatherForecastDescription.setVisibility(View.GONE);
        weatherTown.setVisibility(View.GONE);
        weatherTemperature.setVisibility(View.GONE);
        timeStamp.setVisibility(View.GONE);
        reloadButton.setVisibility(View.GONE);
        clearButton.setVisibility(View.GONE);

        settingsLabel.setVisibility(View.VISIBLE);
    }

    private void makeViewsAppear() {
        imageView.setVisibility(View.VISIBLE);
        weatherForecast.setVisibility(View.VISIBLE);
        weatherForecastDescription.setVisibility(View.VISIBLE);
        weatherTown.setVisibility(View.VISIBLE);
        weatherTemperature.setVisibility(View.VISIBLE);
        timeStamp.setVisibility(View.VISIBLE);
        reloadButton.setVisibility(View.VISIBLE);
        clearButton.setVisibility(View.VISIBLE);

        settingsLabel.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences settingsMain = getSharedPreferences("settingsmain", MODE_APPEND);
        SharedPreferences.Editor edit = settingsMain.edit();
        edit.clear();
        weatherTownText = weatherTown.getText().toString();
        weatherTemperatureText = weatherTemperature.getText().toString();
        weatherForecastText = weatherForecast.getText().toString();
        weatherForecastDescriptionText = weatherForecastDescription.getText().toString();
        timeStampText = timeStamp.getText().toString();

        edit.putString("weathertowntext", weatherTownText);
        edit.putString("weathertemperaturetext", weatherTemperatureText);
        edit.putString("weatherforecasttext", weatherForecastText);
        edit.putString("weatherforecastdescriptionText", weatherForecastDescriptionText);
        edit.putString("timestamptext", timeStampText);
        edit.putString("countrytextsaved", countryTextSaved);
        edit.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        makeViewsDisappear();
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