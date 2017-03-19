package com.example.utilityapp.com.example.utilityapp.model;

/**
 * Created by outba on 19/03/2017.
 */

public class Weather {
    public Location location;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();

    public class CurrentCondition {
        private String weatherCondition;
        private String weatherConditionDescription;

        public String getWeatherCondition() {
            return weatherCondition;
        }

        public void setWeatherCondition(String weatherCondition) {
            this.weatherCondition = weatherCondition;
        }

        public String getWeatherConditionDescription() {
            return weatherConditionDescription;
        }

        public void setWeatherConditionDescription(String weatherConditionDescription) {
            this.weatherConditionDescription = weatherConditionDescription;
        }
    }

    public class Temperature {
        private float temperature;

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }
    }
}
