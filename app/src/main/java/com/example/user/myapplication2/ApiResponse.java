package com.example.user.myapplication2;

import java.util.ArrayList;

public class ApiResponse {

    public CurrentObservation current_observation;
    public Forecast forecast;
    public SunPhase sun_phase;
    
    public class DisplayLocation {
        public String city;
    }

    public class CurrentObservation {
        public DisplayLocation display_location;
        public String weather;
        public double temp_c;
        public String relative_humidity;
        public String wind_dir;
        public double wind_kph;
        public String icon_url;
    }

    public class Date {
        public int day;
        public int month;
        public String weekday;
    }

    public class High {
        public String celsius;
    }

    public class Low {
        public String celsius;
    }

    public class Forecastday2 {
        public Date date;
        public High high;
        public Low low;
        public String conditions;
        public String icon_url;
        public int avehumidity;
    }

    public class Simpleforecast {
        public ArrayList<Forecastday2> forecastday;
    }

    public class Forecast {
        public Simpleforecast simpleforecast;
    }

    public class SunRise {
        public String hour;
        public String minute;
    }

    public class Sunset{
        public String hour;
        public String minute;
    }

    public class SunPhase {
        public Sunset sunset;
        public SunRise sunrise;
    }
}
