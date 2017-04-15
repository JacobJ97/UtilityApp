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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView weatherForecast;
    TextView weatherForecastDescription;
    TextView weatherTemperature;
    TextView weatherTown;
    TextView timeStamp;
    TextView settingsLabel;
    Button clearButton;
    String townText;
    String countryText;
    String countryTextSaved;
    String weatherTownText;
    String weatherTemperatureText;
    String weatherForecastText;
    String weatherForecastDescriptionText;
    String timeStampText;
    String latitudeText;
    String longitudeText;
    boolean switchCondition;
    double longitude;
    double latitude;

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
        clearButton = (Button) findViewById(R.id.clearButton);
        settingsLabel = (TextView) findViewById(R.id.textSettings);

        SharedPreferences settingsMain = getSharedPreferences("settingsmain", 0);
        Bundle extras = getIntent().getExtras();

        if (settingsMain != null) {
            weatherTownText = settingsMain.getString("weathertowntext", weatherTownText);
            weatherTemperatureText = settingsMain.getString("weathertemperaturetext", weatherTemperatureText);
            weatherForecastText = settingsMain.getString("weatherforecasttext", weatherForecastText);
            weatherForecastDescriptionText = settingsMain.getString("weatherforecastdescriptiontext", weatherForecastDescriptionText);
            timeStampText = settingsMain.getString("timestamptext", timeStampText);
            countryTextSaved = settingsMain.getString("countrytextsaved", countryTextSaved);
            latitudeText = settingsMain.getString("latitudetext", latitudeText);
            longitudeText = settingsMain.getString("longitudetext", longitudeText);
            switchCondition = settingsMain.getBoolean("switchcondition", switchCondition);

            weatherTown.setText(weatherTownText);
            weatherTemperature.setText(weatherTemperatureText);
            weatherForecast.setText(weatherForecastText);
            weatherForecastDescription.setText(weatherForecastDescriptionText);
            timeStamp.setText(timeStampText);

            if (weatherForecastDescriptionText != null) {
                findWeatherImage(weatherForecastDescriptionText);
            }
        }

        if (extras != null) {
            townText = extras.getString("towntext");
            countryText = extras.getString("countrystring");
            countryTextSaved = countryText;
            switchCondition = extras.getBoolean("switchcondition");
            longitude = extras.getDouble("longitudecoords");
            latitude = extras.getDouble("latitudecoords");

            if (townText != null && countryText != null) {
                getWeatherDataTownCountry(townText, countryText);
            }
            if (switchCondition) {
                getWeatherDataCoordinates(longitude, latitude);
            }

            System.out.println(switchCondition);
            //makes all labels and views appear
            makeViewsAppear();
        }
    }

    //constructs lat+long string, sends string to be used in api
    private void getWeatherDataCoordinates(double longitude, double latitude) {
        String convertCoords = "lat=" + String.valueOf(latitude) + "&lon=" + String.valueOf(longitude);
        timeStampSent();
        InitiateTaskWeather task = new InitiateTaskWeather();
        task.execute(convertCoords);
    }

    //constructs town+country string, sends string to be used in api
    private void getWeatherDataTownCountry(String townTextMethod, String countryTextMethod ) {
        String countryTextAbbreviation = getAbbreviatedCountry(countryTextMethod);
        String townTextMethodFinal = "q=" + townTextMethod.concat("," + countryTextAbbreviation);
        timeStampSent();
        InitiateTaskWeather task = new InitiateTaskWeather();
        task.execute(townTextMethodFinal);
    }

    //finds country matching selected country in settings, converts it to abbreviation
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

    //sets up menu setting (to send user to settings)
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

    //before api request is sent, timestamp is generated
    public void timeStampSent() {
        Calendar calendar = new GregorianCalendar();
        String time = String.format(Locale.ENGLISH, "%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        int amPm = calendar.get(Calendar.AM_PM);
        String amOrPm;
        if (amPm == Calendar.AM) {
            amOrPm = "am";
        } else {
            amOrPm = "pm";
        }
        timeStampText = "Request sent at " + time + amOrPm;
        timeStamp.setText(timeStampText);
    }

    public void goToSettings(MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void clearWeather(View view) {
        //makes everything disappear, returns to single label
        makeViewsDisappear();
    }

    private void makeViewsDisappear() {
        imageView.setVisibility(View.GONE);
        weatherForecast.setVisibility(View.GONE);
        weatherForecastDescription.setVisibility(View.GONE);
        weatherTown.setVisibility(View.GONE);
        weatherTemperature.setVisibility(View.GONE);
        timeStamp.setVisibility(View.GONE);
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

        edit.putString("weathertowntext", weatherTownText);
        edit.putString("weathertemperaturetext", weatherTemperatureText);
        edit.putString("weatherforecasttext", weatherForecastText);
        edit.putString("weatherforecastdescriptiontext", weatherForecastDescriptionText);
        edit.putString("timestamptext", timeStampText);
        edit.putBoolean("switchcondition", switchCondition);
        edit.apply();
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
                //background operation to call api and get weather data json based on input
                data = ((new WeatherAPIConnection()).retrieveWeatherData(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //background operation to deconstruct json file into object
                weather = WeatherParserJSON.getWeather(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            //sets various info (temperature, condition, etc)
            weatherTown.setText(weather.location.getCity());
            weatherForecast.setText(weather.currentCondition.getWeatherCondition());
            weatherForecastDescription.setText(weather.currentCondition.getWeatherConditionDescription());
            float weatherTemperatureFloat = weather.temperature.getTemperature();
            String weatherTemperatureString = String.format(Locale.ENGLISH, "%.0f", weatherTemperatureFloat - 273.15);
            weatherTemperature.setText(weatherTemperatureString + " \u2103");

            //sets weather condition icon
            retrieveWeatherDescription(weather);
        }
    }

    private void retrieveWeatherDescription(Weather weather) {
        String weatherConditionDescriptionText = weather.currentCondition.getWeatherConditionDescription();
        findWeatherImage(weatherConditionDescriptionText);
    }

    private void findWeatherImage(String weatherDescription) {
        switch (weatherDescription) {
            //clear conditions
            case "clear sky": {
                imageView.setImageResource(R.drawable.sunny_day);
                break;
            }

            //sunny/cloudy
            case "few clouds": {
                imageView.setImageResource(R.drawable.cloudy_sunny);
                break;
            }

            //cloudy
            case "scattered clouds": {
                imageView.setImageResource(R.drawable.cloudy);
                break;
            }

            //very cloudy
            case "broken clouds":
            case "overcast clouds": {
                imageView.setImageResource(R.drawable.overcast);
                break;
            }

            //light rain
            case "light rain":
            case "light intensity drizzle":
            case "drizzle":
            case "heavy intensity drizzle":
            case "light intensity drizzle rain":
            case "drizzle rain":
            case "heavy intensity drizzle rain":
            case "shower rain and drizzle":
            case "light intensity shower rain":
            case "shower drizzle": {
                imageView.setImageResource(R.drawable.cloudy_rain);
                break;
            }


            //heavy rain
            case "heavy intensity rain":
            case "very heavy rain":
            case "extreme rain":
            case "heavy intensity shower rain": {
                imageView.setImageResource(R.drawable.heavy_rain);
                break;
            }

            //moderate rain
            case "moderate rain":
            case "freezing rain":
            case "shower rain":
            case "ragged shower rain":
            case "heavy shower rain and drizzle": {
                imageView.setImageResource(R.drawable.rainy);
                break;
            }

            //thunderstorm
            case "thunderstorm with light rain":
            case "thunderstorm with rain":
            case "thunderstorm with heavy rain":
            case "light thunderstorm":
            case "heavy thunderstorm":
            case "rugged thunderstorm":
            case "thunderstorm with light drizzle":
            case "thunderstorm with drizzle":
            case "thunderstorm with heavy drizzle":
            case "thunderstorm": {
                imageView.setImageResource(R.drawable.thunderstorms);
                break;
            }

            //snow
            case "heavy snow":
            case "light snow":
            case "sleet":
            case "shower sleet":
            case "light rain and snow":
            case "rain and snow":
            case "light shower snow":
            case "shower snow":
            case "heavy shower snow":
            case "snow": {
                imageView.setImageResource(R.drawable.snowy);
                break;
            }

            //hail
            case "hail": {
                imageView.setImageResource(R.drawable.hail);
                break;
            }

            //wind
            case "tornado":
            case "windy":
            case "light breeze":
            case "gentle breeze":
            case "moderate breeze":
            case "freeze breeze":
            case "strong breeze":
            case "high wind, near gale":
            case "gale":
            case "severe gale":
            case "storm":
            case "violent storm":
            case "hurricane": {
                imageView.setImageResource(R.drawable.windy);
                break;
            }

            //atmospheric conditions
            case "mist":
            case "smoke":
            case "haze":
            case "sand, dust whirls":
            case "fog":
            case "sand":
            case "dust":
            case "volcanic ash":
            case "squalls": {
                imageView.setImageResource(R.drawable.windy);
                break;
            }

        }

    }
}