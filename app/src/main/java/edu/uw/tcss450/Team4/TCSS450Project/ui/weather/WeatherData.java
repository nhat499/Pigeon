package edu.uw.tcss450.Team4.TCSS450Project.ui.weather;

import androidx.annotation.NonNull;

public class WeatherData {
    /**
     * A class that represents the current weather data.
     * NOT FINISHED!!!
     */
    public class Weather {

        private final String condition;
        //private final int sunrise;
      //  private final int sunset;
       // private final double feels_like;

        private final double temp;
        private final double humidity;
        private final double windspeed;
        private final String description;
        private final String icon;
       // private final int sunset;
       // private final int


        /**
         * A contructor for weather
         *
         * @param condition the condition
         * @param temp the temp

         * @param humidity the humidity
         * @param icon an icon
         */
        public Weather(String condition, double temp, String description,
                       double humidity, double windspeed, String icon){

            this.condition = condition;
            this.temp = temp;
            this.description = description;
            this.humidity = humidity;
            this.icon  = icon;
            this.windspeed = windspeed;
        }

        /**
         * This method gets the condition
         * @return the condition
         */
        public String getCondition(){
            return this.condition;
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
        public double getHumidity() {
            return this.humidity; }

        public double getWindSpeed(){
            return this.windspeed;
        }

        public String getDescription(){
            return getDescription();
        }


        @NonNull
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("Condition: " + getCondition() + "\n");
            result.append("Temperature: " + getTemp() + "\n");
            result.append("Wind speed is:" + getWindSpeed() + "\n");
            result.append("Description: " + getDescription() + "\n");
            result.append("Humidity: " + getHumidity() + "\n");
            return result.toString();
        }
    }
}
