package com.example.user.myapplication2;

import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class NowFragment extends Fragment implements
        WeatherProvider.WeatherListener {

    // Class name for Log
    private final String CLASS_NAME = getClass().getSimpleName();

    // Now tab's fields (land)
    private TextView city;
    private TextView main_temp;
    private TextView mm_temp;
    private TextView descr;
    // Now tab's additional fields (port)
    private TextView humidity;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.now_tab, container, false);

        WeatherProvider wp = WeatherProvider.getInstance();
        wp.addListener(this);

        // Now tab's fields (land)
		city = (TextView) rootView.findViewById(R.id.city);
		main_temp = (TextView) rootView.findViewById(R.id.main_temp);
		mm_temp = (TextView) rootView.findViewById(R.id.appr_temp);
		descr = (TextView) rootView.findViewById(R.id.description);
        // Now tab's additional fields (port)
		humidity = (TextView) rootView.findViewById(R.id.humidity_val);
		wind = (TextView) rootView.findViewById(R.id.wind_val);
		sunrise = (TextView) rootView.findViewById(R.id.sunrise_val);
		sunset = (TextView) rootView.findViewById(R.id.sunset_val);
        
        return rootView;
    }

    @Override
    public void onReceivedWeather(ApiResponse now) {

        Log.w(CLASS_NAME, "Refreshing fields contents");

        String main_temp_format = getString(R.string.main_temp_format);
        String main_wind_format = getString(R.string.main_wind_format);
        String mm_temp_format = getString(R.string.main_temp_1_2_format);

        city.setText(now.current_observation.display_location.city);

        main_temp.setText(String.format(
                Locale.getDefault(),
                main_temp_format,
                Math.round(now.current_observation.temp_c))
        );

        mm_temp.setText(String.format(
                Locale.getDefault(),
                mm_temp_format,
                now.forecast.simpleforecast.forecastday.get(0).low.celsius,
                now.forecast.simpleforecast.forecastday.get(0).high.celsius)
        );

        descr.setText(now.current_observation.weather);

        // PORTRAIT ORIENTATION
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            String time_format = getString(R.string.time_h_m_format);

            humidity.setText(now.current_observation.relative_humidity);

            wind.setText(String.format(
                    Locale.getDefault(),
                    main_wind_format,
                    now.current_observation.wind_dir,
                    now.current_observation.wind_kph)
            );

            sunrise.setText(String.format(
                    Locale.getDefault(),
                    time_format,
                    now.sun_phase.sunrise.hour,
                    now.sun_phase.sunrise.minute)
            );

            sunset.setText(String.format(
                    Locale.getDefault(),
                    time_format,
                    now.sun_phase.sunset.hour,
                    now.sun_phase.sunset.minute)
            );
        }
    }
}
