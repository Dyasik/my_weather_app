package com.example.user.myapplication2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ForecastFragment extends Fragment
        implements WeatherProvider.WeatherListener {

    private TextView forec;
    private ListView listView;

    // List of Forecast Items to be depicted.
    List<HashMap<String, Object>> forecList = new ArrayList<>();
    // Forecast Item's fields
    private static final String DAY = "day";
    private static final String ICON = "icon";
    private static final String TEMP = "temp";
    private static final String DATE = "date";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_tab, container, false);

        WeatherProvider wp = WeatherProvider.getInstance();
        wp.addListener(this);

        //"Weekly forecast for"
        forec = (TextView) rootView.findViewById(R.id.forec_label);
        forec.setText(getString(R.string.forecast_label));
        // "%CITY_NAME"
        TextView f_city = (TextView) rootView.findViewById(R.id.forec_city);
        f_city.setText(getString(R.string.test_city));
        // Forecast list
        listView = (ListView) rootView.findViewById(R.id.forec_items_list);
        fillForecastList(listView);

        return rootView;
    }

    //
    // This method creates the list of Forecast Items.
    // If some items must be added/edited/deleted, do it here.
    //
    public void fillForecastList(ListView listView)
    {
        addForecastItem("Monday", android.R.drawable.ic_menu_report_image, "5°C/-2°C", "31/12");
        addForecastItem("Tuesday", android.R.drawable.ic_menu_report_image, "0°C/-13°C", "1/01");
        addForecastItem("Wednesday", android.R.drawable.ic_menu_report_image, "-10°C/-29°C", "2/01");
        addForecastItem("Thursday", android.R.drawable.ic_menu_report_image, "100°F/55°F", "15/02");
        addForecastItem("Friday", android.R.drawable.ic_menu_report_image, "25°C/13°C", "3/07");
        addForecastItem("Saturday", android.R.drawable.ic_menu_report_image, "25°C/13°C", "5/11");
        addForecastItem("Sunday", android.R.drawable.ic_menu_report_image, "25°C/13°C", "30/02");

        SimpleAdapter adapter = new SimpleAdapter(
                super.getContext(),
                forecList,
                R.layout.forecast_item,
                new String[]{DAY, ICON, TEMP, DATE},
                new int[]{R.id.forec_item_day, R.id.forec_item_ico,
                        R.id.forec_item_temp, R.id.forec_item_date}
        );

        listView.setAdapter(adapter);
    }
    // Internal method for fillForecastList()
    public void addForecastItem(String day, Object icon, String temp, String date)
    {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(DAY, day);
        hm.put(ICON, icon);
        hm.put(TEMP, temp);
        hm.put(DATE, date);
        forecList.add(hm);
    }

    @Override
    public void onReceivedWeather(ApiResponse resp) {
        forecList = new ArrayList<>();
        String temp_format = getString(R.string.forec_item_temp_format);
        String date_format = getString(R.string.forec_item_date_format);
        ApiResponse.Forecastday2 day;

        for (int day_count = 1; day_count < 8; day_count++) {
            day = resp.forecast.simpleforecast.forecastday.get(day_count);
            addForecastItem(
                    day.date.weekday,
                    android.R.drawable.ic_menu_report_image,
                    String.format(
                            Locale.getDefault(),
                            temp_format,
                            day.high.celsius,
                            day.low.celsius
                    ),
                    String.format(
                            Locale.getDefault(),
                            date_format,
                            day.date.day,
                            day.date.month
                    )
            );
        }

        SimpleAdapter adapter = new SimpleAdapter(
                super.getContext(),
                forecList,
                R.layout.forecast_item,
                new String[]{DAY, ICON, TEMP, DATE},
                new int[]{R.id.forec_item_day, R.id.forec_item_ico,
                        R.id.forec_item_temp, R.id.forec_item_date}
        );

        listView.setAdapter(adapter);
    }
}
