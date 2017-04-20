package com.example.utilityapp;


class Weather {
    Location location;
    CurrentCondition currentCondition = new CurrentCondition();
    Temperature temperature = new Temperature();

    public class CurrentCondition {
        String weatherCondition;
        String weatherConditionDescription;

        String getWeatherCondition() {
            return weatherCondition;
        }

        void setWeatherCondition(String weatherCondition) {
            this.weatherCondition = weatherCondition;
        }

        String getWeatherConditionDescription() {
            return weatherConditionDescription;
        }

        void setWeatherConditionDescription(String weatherConditionDescription) {
            this.weatherConditionDescription = weatherConditionDescription;
        }
    }

    public class Temperature {
        private float temperature;

        float getTemperature() {
            return temperature;
        }

        void setTemperature(float temperature) {
            this.temperature = temperature;
        }
    }
}
