package com.example.user.myapplication2;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

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
    private ImageView icon;
    // Now tab's additional fields (port)
    private TextView humidity;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.now_tab, container, false);

        // Now tab's fields (land)
		city = (TextView) rootView.findViewById(R.id.city);
		main_temp = (TextView) rootView.findViewById(R.id.main_temp);
		mm_temp = (TextView) rootView.findViewById(R.id.appr_temp);
		descr = (TextView) rootView.findViewById(R.id.description);
        icon = (ImageView) rootView.findViewById(R.id.weather_icon);
        // Now tab's additional fields (port)
		humidity = (TextView) rootView.findViewById(R.id.humidity_val);
		wind = (TextView) rootView.findViewById(R.id.wind_val);
		sunrise = (TextView) rootView.findViewById(R.id.sunrise_val);
		sunset = (TextView) rootView.findViewById(R.id.sunset_val);

        WeatherProvider.getInstance().addListener(this);

        return rootView;
    }

    @Override
    public void onReceivedWeather(ApiResponse now) {

        if (isAdded()) {

            requestImage(now.current_observation.icon_url);

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

    private void requestImage(String url){
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        icon.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        icon.setImageResource(android.R.drawable.ic_menu_report_image);
                    }
                }
        );

        Volley.newRequestQueue(getContext()).add(request);
    }
}
