package com.example.utilityapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by outba on 19/03/2017.
 */

public class WeatherAPIConnection {

    String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=";
    String apiKey = "ebe567c3dc8e521a15cc1034f8a5cc29";

    public String retrieveWeatherData(String location) throws IOException {

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(weatherURL + location + "&appid=" + apiKey)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuilder buffer = new StringBuilder();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line).append("\r\n");
            }
            inputStream.close();
            connection.disconnect();
            return buffer.toString();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //inputStream.close();
            //connection.disconnect();
        }
        return null;
    }
}

