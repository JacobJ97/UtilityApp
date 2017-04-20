package com.example.utilityapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The data needed is contained within an API, and thus a request is needed to be sent in order
 * for the application to show users weather information.
 */

class WeatherAPIConnection {

    String weatherURL = "http://api.openweathermap.org/data/2.5/weather?";
    String apiKey = "ebe567c3dc8e521a15cc1034f8a5cc29";

    String retrieveWeatherData(String location) throws IOException {

        HttpURLConnection connection;
        InputStream inputStream;

        try {
            //constructs url and sends a request
            connection = (HttpURLConnection) (new URL(weatherURL + location + "&appid=" + apiKey)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //stringbuilder used as fast/low memory way of storing retrieved request data, and is turned into regular string
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

        return null;
    }
}

