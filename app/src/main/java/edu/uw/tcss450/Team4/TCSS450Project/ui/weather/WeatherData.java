package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class WeatherData {
    /**
     * A class that represents the current weather data.
     * NOT FINISHED!!!
     */
    public class Weather {

        private final double lat;
        private final double lon;
        private final int dt;
        private final double temp;
        private final String description;
        private final double humidity;
        private final double windspeed;
        private final int sunrise;
        private final int sunset;
        private final double feels_like;
        private final int uvi;
        private final int visibility;
       // private final ArrayList<Weather> weather;
        private final String icon;

        /**
         * A Constructor for weather
         * @param lat
         * @param lon
         * @param dt
         * @param temp
         * @param description
         * @param sunrise
         * @param sunset
         * @param feels_like
         * @param humidity
         * @param windspeed
         * @param uvi
         * @param visibility
         * @param icon
         */
        public Weather(double lat, double lon, int dt, double temp, String description,int sunrise, int sunset, double feels_like,
                       double humidity, double windspeed, int uvi, int visibility, String icon){
            this.lat = lat;
            this.lon=lon;
            this.dt= dt;
            this.temp = temp;
            this.description = description;
            this.sunrise=sunrise;
            this.sunset = sunset;
            this.feels_like=feels_like;
            this.humidity = humidity;
            this.windspeed = windspeed;
            this.uvi= uvi;
            this.visibility = visibility;
            this.icon  = icon;

        }


        /**
         * This method gets the temp
         * @return the temp
         */
        public double getTemp() {
            return this.temp;
        }


        /**
         * This method gets thehumidity
         * @return the humidity.
         */
        public double getHumidity() { return this.humidity; }

        /**
         * Gets the windspeed
         * @return
         */
        public double getWindSpeed(){ return this.windspeed; }

        /**
         * Gets the description
         * @return
         */
        public String getDescription(){ return getDescription(); }

        /**
         * Gets the latitude
         * @return
         */
        public double getLat() { return lat; }

        /**
         * Gets the longitude
         * @return
         */
        public double getLon() { return lon; }

        /**
         * Gets the unix time
         * @return
         */
        public int getDt() { return dt; }


        public int getSunrise() {
            return sunrise;
        }

        public int getSunset() {
            return sunset;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public int getUvi() {
            return uvi;
        }

        public int getVisibility() {
            return visibility;
        }

        public String getIcon() {
            return icon;
        }

        @NonNull
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            result.append("Temperature: " + getTemp() + "\n");
            result.append("Wind speed is:" + getWindSpeed() + "\n");
            result.append("Description: " + getDescription() + "\n");
            result.append("Humidity: " + getHumidity() + "\n");
            return result.toString();
        }
    }
}
