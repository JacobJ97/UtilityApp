package com.example.utilityapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherParserJSON {
    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();

        JSONObject jsonObject = new JSONObject(data);

        Location location = new Location();

        location.setCity(getString("name", jsonObject));
        weather.location = location;

        JSONArray jsonArray = jsonObject.getJSONArray("weather");

        JSONObject JSONWeather = jsonArray.getJSONObject(0);
        weather.currentCondition.setWeatherCondition(getString("main", JSONWeather));
        weather.currentCondition.setWeatherConditionDescription(getString("description", JSONWeather));


        JSONObject mainObj = getObject("main", jsonObject);
        weather.temperature.setTemperature(getFloat("temp", mainObj));

        return weather;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        return jObj.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }
}
