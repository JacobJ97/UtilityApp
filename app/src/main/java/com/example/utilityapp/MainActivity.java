package com.example.utilityapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utilityapp.com.example.utilityapp.model.Weather;

import org.json.JSONException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    String townText;
    ImageView imageView;
    TextView weatherForecast;
    TextView weatherTemperature;
    TextView weatherTown;
    TextView timeStamp;
    Boolean switchCondition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        weatherForecast = (TextView)findViewById(R.id.textWeatherForecast);
        weatherTemperature = (TextView)findViewById(R.id.textTemp);
        weatherTown = (TextView)findViewById(R.id.townText);
        timeStamp = (TextView)findViewById(R.id.timeText);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            townText = extras.getString("towntext");
            switchCondition = extras.getBoolean("switchcondition");
            if (townText != null) {
                townText = townText.concat(",AU");
                InitiateTaskWeather task = new InitiateTaskWeather();
                task.execute(townText);
                Calendar calendar = new GregorianCalendar();
                String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                int amPm = calendar.get(Calendar.AM_PM);
                String amOrPm;
                if (amPm == Calendar.AM) {
                    amOrPm = "am";
                }
                else {
                    amOrPm = "pm";
                }
                timeStamp.setText("Data retrieved at " + time + amOrPm);
            }

            if (switchCondition) {
                System.out.println("hey");
            }
        }
    }

    public void goToSettings(View view) {
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
            weatherForecast.setText(weather.currentCondition.getWeatherCondition() + " - " + weather.currentCondition.getWeatherConditionDescription());
            float weatherTemperatureFloat = weather.temperature.getTemperature();
            String weatherTemperatureString = String.format("%.0f", weatherTemperatureFloat - 273.15);
            weatherTemperature.setText(weatherTemperatureString + " \u2103");
        }
    }
}



