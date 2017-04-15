package com.example.utilityapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data that is retrieved from the api is in JSON format, and will be converted into Objects.
 */

public class WeatherParserJSON {
    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();

        JSONObject jsonObject = new JSONObject(data);

        Location location = new Location();

        //example: "name" is the name in the name/value pair
        //thus, "jsonObject" is the value.
        //JSON is converted into a location object, since it is retrieving city name.
        location.setCity(getString("name", jsonObject));
        weather.location = location;

        //"weather" is the name, and the value is an array.
        JSONArray jsonArray = jsonObject.getJSONArray("weather");

        //retrieves weather conditions and it's description from the JSON array
        JSONObject JSONWeather = jsonArray.getJSONObject(0);
        weather.currentCondition.setWeatherCondition(getString("main", JSONWeather));
        weather.currentCondition.setWeatherConditionDescription(getString("description", JSONWeather));

        //retrieves temperature
        JSONObject mainObj = getObject("main", jsonObject);
        weather.temperature.setTemperature(getFloat("temp", mainObj));

        return weather;
    }

    //retrieves JSON data that contain multiple values of same type
    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        return jObj.getJSONObject(tagName);
    }

    //retrieves JSON data that are string values
    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    //retrieves JSON data that are float values
    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }
}
