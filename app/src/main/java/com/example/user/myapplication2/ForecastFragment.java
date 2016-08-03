package com.example.user.myapplication2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ForecastFragment extends Fragment
        implements WeatherProvider.WeatherListener {

    private TextView forec;
    private TextView f_city;
    private ListView listView;

    private String temp_format;
    private String date_format;

    // List of Forecast Items to be depicted.
    List<HashMap<String, Object>> forecList = new ArrayList<>();
    // Forecast Item's fields
    private final String DAY = "day";
    private final String ICON = "icon";
    private final String TEMP = "temp";
    private final String DATE = "date";

    // How many days should be shown in the list
    private final byte LIST_SIZE = 7;

    // For requesting images
    //private Object icon;
    //private ApiResponse.Forecastday2 day;

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
        fillForecastList(listView);

        temp_format = getString(R.string.forec_item_temp_format);
        date_format = getString(R.string.forec_item_date_format);

        WeatherProvider.getInstance().addListener(this);

        return rootView;
    }

    //
    // This method creates the FAKE list of Forecast Items.
    //
    public void fillForecastList(ListView listView)
    {
        forecList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            addForecastItem("---", android.R.drawable.ic_menu_report_image, "--°C/--°C", "--/--");
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
    // Internal method for filling the forecast list
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
        if (isAdded()) {
            f_city.setText(resp.current_observation.display_location.city);
            forecList = new ArrayList<>();
            String temp_format = getString(R.string.forec_item_temp_format);
            String date_format = getString(R.string.forec_item_date_format);
            ApiResponse.Forecastday2 day;

            for (int day_count = 1; day_count <= LIST_SIZE; day_count++) {
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

            updateList(resp);
        }
    }

    private void updateList(ApiResponse response) {
        View item;
        for (int i = 0; i < LIST_SIZE; i++) {
            item = listView.getChildAt(i);
            if (item == null) {
                Log.e("DEB", "Item is null at index " + i);
                continue;
            }
            final ImageView icon = (ImageView) item.findViewById(R.id.forec_item_ico);
            if (icon == null) {
                Log.e("DEB", "Icon is null");
                continue;
            }
            ImageRequest request = new ImageRequest(
                   response.forecast.simpleforecast.forecastday.get(i + 1).icon_url,
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

//    private void addNext(final ApiResponse resp, final int itemNum){
//        if (itemNum <= LIST_SIZE) {
//            day = resp.forecast.simpleforecast.forecastday.get(itemNum);
//            ImageRequest request = new ImageRequest(
//                        day.icon_url,
//                        new Response.Listener<Bitmap>() {
//                            @Override
//                            public void onResponse(Bitmap bitmap) {
//                                addForecastItem(
//                                        day.date.weekday,
//                                        bitmap,
//                                        String.format(
//                                                Locale.getDefault(),
//                                                temp_format,
//                                                day.high.celsius,
//                                                day.low.celsius
//                                        ),
//                                        String.format(
//                                                Locale.getDefault(),
//                                                date_format,
//                                                day.date.day,
//                                                day.date.month
//                                        )
//                                );
//
//                                addNext(resp, itemNum + 1);
//                            }
//                        }, 0, 0, null, Bitmap.Config.RGB_565,
//                        new Response.ErrorListener() {
//                            public void onErrorResponse(VolleyError error) {
//                                addForecastItem(
//                                        day.date.weekday,
//                                        getResources()
//                                                .getDrawable(android.R.drawable.ic_menu_report_image),
//                                        String.format(
//                                                Locale.getDefault(),
//                                                temp_format,
//                                                day.high.celsius,
//                                                day.low.celsius
//                                        ),
//                                        String.format(
//                                                Locale.getDefault(),
//                                                date_format,
//                                                day.date.day,
//                                                day.date.month
//                                        )
//                                );
//
//                                addNext(resp, itemNum + 1);
//                            }
//                        }
//            );
//
//            Volley.newRequestQueue(getContext()).add(request);
//        }
//    }
}
