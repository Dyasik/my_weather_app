package com.example.user.myapplication2;
import java.util.ArrayList;
public class ApiResponse {

    public Response response;
    public CurrentObservation current_observation;
    public Forecast forecast;
    public MoonPhase moon_phase;
    public SunPhase sun_phase;
    
    public class Features
    {
        public int conditions;
        public int forecast10day;
    }

    public class Response
    {
        public String version;
        public String termsofService;
        public Features features;
    }

    public class Image
    {
        public String url;
        public String title;
        public String link;
    }

    public class DisplayLocation
    {
        public String full;
        public String city;
        public String state;
        public String state_name;
        public String country;
        public String country_iso3166;
        public String zip;
        public String magic;
        public String wmo;
        public String latitude;
        public String longitude;
        public String elevation;
    }

    public class ObservationLocation
    {
        public String full;
        public String city;
        public String state;
        public String country;
        public String country_iso3166;
        public String latitude;
        public String longitude;
        public String elevation;
    }

    public class Estimated
    {}

    public class CurrentObservation
    {
        public Image image;
        public DisplayLocation display_location;
        public ObservationLocation observation_location;
        public Estimated estimated;
        public String station_id;
        public String observation_time;
        public String observation_time_rfc822;
        public String observation_epoch;
        public String local_time_rfc822;
        public String local_epoch;
        public String local_tz_short;
        public String local_tz_long;
        public String weather;
        public String temperature_string;
        public double temp_f;
        public double temp_c;
        public String relative_humidity;
        public String wind_string;
        public String wind_dir;
        public int wind_degrees;
        public double wind_mph;
        public String wind_gust_mph;
        public double wind_kph;
        public String wind_gust_kph;
        public String pressure_mb;
        public String pressure_in;
        public String pressure_trend;
        public String dewpoint_string;
        public int dewpoint_f;
        public int dewpoint_c;
        public String heat_index_string;
        public String heat_index_f;
        public String heat_index_c;
        public String windchill_string;
        public String windchill_f;
        public String windchill_c;
        public String feelslike_string;
        public String feelslike_f;
        public String feelslike_c;
        public String visibility_mi;
        public String visibility_km;
        public String solarradiation;
        public String UV;
        public String precip_1hr_string;
        public String precip_1hr_in;
        public String precip_1hr_metric;
        public String precip_today_string;
        public String precip_today_in;
        public String precip_today_metric;
        public String icon;
        public String icon_url;
        public String forecast_url;
        public String history_url;
        public String ob_url;
        public String nowcast;
    }

    public class Forecastday
    {
        public int period;
        public String icon;
        public String icon_url;
        public String title;
        public String fcttext;
        public String fcttext_metric;
        public String pop;
    }

    public class TxtForecast
    {
        public String date;
        public ArrayList<Forecastday> forecastday;
    }

    public class Date
    {
        public String epoch;
        public String pretty;
        public int day;
        public int month;
        public int year;
        public int yday;
        public int hour;
        public String min;
        public int sec;
        public String isdst;
        public String monthname;
        public String monthname_short;
        public String weekday_short;
        public String weekday;
        public String ampm;
        public String tz_short;
        public String tz_long;
    }

    public class High
    {
        public String fahrenheit;
        public String celsius;
    }

    public class Low
    {
        public String fahrenheit;
        public String celsius;
    }

    public class QpfAllday
    {
        public double in;
        public int mm;
    }

    public class QpfDay
    {
        public double in;
        public int mm;
    }

    public class QpfNight
    {
        public double in;
        public int mm;
    }

    public class SnowAllday
    {
        public double in;
        public double cm;
    }

    public class SnowDay
    {
        public double in;
        public double cm;
    }

    public class SnowNight
    {
        public double in;
        public double cm;
    }

    public class Maxwind
    {
        public int mph;
        public int kph;
        public String dir;
        public int degrees;
    }

    public class Avewind
    {
        public int mph;
        public int kph;
        public String dir;
        public int degrees;
    }

    public class Forecastday2
    {
        public Date date;
        public int period;
        public High high;
        public Low low;
        public String conditions;
        public String icon;
        public String icon_url;
        public String skyicon;
        public int pop;
        public QpfAllday qpf_allday;
        public QpfDay qpf_day;
        public QpfNight qpf_night;
        public SnowAllday snow_allday;
        public SnowDay snow_day;
        public SnowNight snow_night;
        public Maxwind maxwind;
        public Avewind avewind;
        public int avehumidity;
        public int maxhumidity;
        public int minhumidity;
    }

    public class Simpleforecast
    {
        public ArrayList<Forecastday2> forecastday;
    }

    public class Forecast
    {
        public TxtForecast txt_forecast;
        public Simpleforecast simpleforecast;
    }

    public class MoonPhase {  }

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
