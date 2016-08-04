package com.example.user.myapplication2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ForecastFragment extends Fragment
        implements WeatherProvider.WeatherListener, ListPicturesProvider.PicturesListener {

    private TextView forec;
    private TextView f_city;
    private ListView listView;
    private ApiResponse resp;

    private String temp_format;
    private String date_format;

    // List of Forecast Items to be depicted.
    ArrayList<ListItem> forecList;

    // How many days should be shown in the list
    public static final byte LIST_SIZE = 7;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_tab, container, false);

        //"Weekly forecast for"
        forec = (TextView) rootView.findViewById(R.id.forec_label);
        forec.setText(getString(R.string.forecast_label));
        // "%CITY_NAME"
        f_city = (TextView) rootView.findViewById(R.id.forec_city);
        f_city.setText("---");
        // Forecast list
        listView = (ListView) rootView.findViewById(R.id.forec_items_list);
        forecList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            forecList.add(new ListItem("---", "--/--", "--°C/--°C", null));
        }

        listView.setAdapter(new ListItemAdapter(
                getContext(),
                forecList
        ));

        temp_format = getString(R.string.forec_item_temp_format);
        date_format = getString(R.string.forec_item_date_format);

        WeatherProvider.getInstance().addListener(this);

        return rootView;
    }

    @Override
    public void onReceivedWeather(ApiResponse resp) {
        if (isAdded()) {
            this.resp = resp;

            ArrayList<String> urls = new ArrayList<>();

            for (int i = 1; i <= LIST_SIZE; i++) {
                urls.add(resp.forecast.simpleforecast.forecastday.get(i).icon_url);
            }

            ListPicturesProvider lpp = ListPicturesProvider.getInstance(urls);
            lpp.setListener(this);
            lpp.startQueue(getContext());
        }
    }

    @Override
    public void onReceivedPictures(Object[] pics) {
        f_city.setText(resp.current_observation.display_location.city);
        forecList = new ArrayList<>();
        String temp_format = getString(R.string.forec_item_temp_format);
        String date_format = getString(R.string.forec_item_date_format);
        ApiResponse.Forecastday2 day;

        for (int day_count = 1; day_count <= LIST_SIZE; day_count++) {
            day = resp.forecast.simpleforecast.forecastday.get(day_count);
            forecList.add(new ListItem(
                    day.date.weekday,
                    String.format(
                            Locale.getDefault(),
                            date_format,
                            day.date.day,
                            day.date.month
                    ),
                    String.format(
                            Locale.getDefault(),
                            temp_format,
                            day.high.celsius,
                            day.low.celsius
                    ),
                    pics[day_count - 1] == null ? null : (Bitmap)pics[day_count - 1]
            ));
        }

        listView.setAdapter(new ListItemAdapter(
                getContext(),
                forecList
        ));
    }
}
