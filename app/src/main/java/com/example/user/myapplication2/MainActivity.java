package com.example.user.myapplication2;

import android.Manifest;
import android.R.drawable;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements WeatherProvider.CurrentWeatherListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /* This one is needed to receive location */
    private LocationManager locationManager;
    private Location loca;

    private final String PROVIDER = LocationManager.NETWORK_PROVIDER;
    private final long TIME_BW_REQS = 0; // minimum time between requests
    private final float DIST = 0; // distance to do new request

    /* Class' name for Log */
    private final String CLASS_NAME = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /* This one is needed to receive location */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        // List of Forecast Items to be depicted.
        ArrayList<HashMap<String, Object>> forecList = new ArrayList<>();
        // Forecast Item's fields
        private static final String DAY = "day";
        private static final String ICON = "icon";
        private static final String TEMP = "temp";
        private static final String DATE = "date";
        //
        // This method creates the list of Forecast Items.
        // If some items must be added/edited/deleted, do it here.
        //
        public void fillForecastList(ListView listView)
        {
            addForecastItem("Monday", drawable.ic_menu_report_image, "5°C/-2°C", "31/12");
            addForecastItem("Tuesday", drawable.ic_menu_report_image, "0°C/-13°C", "1/01");
            addForecastItem("Wednesday", drawable.ic_menu_report_image, "-10°C/-29°C", "2/01");
            addForecastItem("Thursday", drawable.ic_menu_report_image, "100°F/55°F", "15/02");
            addForecastItem("Friday", drawable.ic_menu_report_image, "25°C/13°C", "3/07");
            addForecastItem("Saturday", drawable.ic_menu_report_image, "25°C/13°C", "5/11");
            addForecastItem("Sunday", drawable.ic_menu_report_image, "25°C/13°C", "30/02");

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

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            // switching the tab number

            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case (1): // processing the "NOW" tab
                    rootView = inflater.inflate(R.layout.now_tab, container, false);
                    TextView appr = (TextView) rootView.findViewById(R.id.appr_temp);
                    appr.setText(getString(R.string.main_temp_1_2_format,
                            getString(R.string.test_temp1),
                            getString(R.string.test_temp2)));
                    break;
                case (2): // processing the "FORECAST" tab
                    rootView = inflater.inflate(R.layout.forecast_tab, container, false);
                    //"Weekly forecast for"
                    TextView forec = (TextView) rootView.findViewById(R.id.forec_label);
                    forec.setText(getString(R.string.forecast_label));
                    // "%CITY_NAME"
                    TextView f_city = (TextView) rootView.findViewById(R.id.forec_city);
                    f_city.setText(getString(R.string.test_city));
                    // Forecast List
                    ListView listView = (ListView) rootView.findViewById(R.id.forec_items_list);
                    fillForecastList(listView);
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 (no, 2) total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab1);
                case 1:
                    return getString(R.string.tab2);
                /*case 2:
                    return "SECTION 3";*/
            }
            return null;
        }
    }

    public void refresh(View ImgBtn){
        Log.w(CLASS_NAME, "Refresh started");

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                loca = location;
                Log.w("Debug", "Position received");

                if (loca != null) {
                    Log.w(CLASS_NAME, "Latitude: " + loca.getLatitude());
                    Log.w(CLASS_NAME, "Longitude: " + loca.getLongitude());
                    Log.w(CLASS_NAME, "Time: " + new Date(loca.getTime()));
                } else {
                    Log.e(CLASS_NAME, "Location is NULL");
                }

                try {
                    locationManager.removeUpdates(this);
                } catch (SecurityException e) {
                    Log.e(CLASS_NAME, e.getMessage());
                }

                requestWeather();
            }

            @Override
            public void onProviderDisabled(String provider) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1);
        }

        try {
            locationManager
                    .requestLocationUpdates(
                            PROVIDER,
                            TIME_BW_REQS,
                            DIST,
                            locationListener
                    );
        }
        catch (SecurityException ex){
            Log.e(CLASS_NAME, ex.getMessage());
        }
    }

    private void requestWeather() {
        WeatherProvider weatherProvider = WeatherProvider.getInstance();
        weatherProvider.setListener(this);
        weatherProvider.makeRequest(loca, this);
    }

    @Override
    public void onReceivedWeather(ApiResponse weather) {
        redrawNowFromResponse(weather);
    }

    public void redrawNowFromResponse(ApiResponse weather) {

        Log.w(CLASS_NAME, "I am in redrawNowFromResponse()");

        TextView city = (TextView) findViewById(R.id.city);
        TextView main_temp = (TextView) findViewById(R.id.main_temp);
        TextView descr = (TextView) findViewById(R.id.description);
        TextView mm_temp = (TextView) findViewById(R.id.appr_temp);

        String main_temp_format = getString(R.string.main_temp_format);
        String main_wind_format = getString(R.string.main_wind_format);
        String mm_temp_format = getString(R.string.main_temp_1_2_format);

        city.setText(weather.current_observation.display_location.city);

        main_temp.setText(String.format(
                Locale.getDefault(),
                main_temp_format,
                Math.round(weather.current_observation.temp_c))
        );

        mm_temp.setText(String.format(
                Locale.getDefault(),
                mm_temp_format,
                weather.forecast.simpleforecast.forecastday.get(0).low.celsius,
                weather.forecast.simpleforecast.forecastday.get(0).high.celsius)
        );

        descr.setText(weather.current_observation.weather);

        // PORTRAIT ORIENTATION
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            TextView humidity = (TextView) findViewById(R.id.humidity_val);
            TextView wind = (TextView) findViewById(R.id.wind_val);
            TextView sunrise = (TextView) findViewById(R.id.sunrise_val);
            TextView sunset = (TextView) findViewById(R.id.sunset_val);

            String time_format = getString(R.string.time_h_m_format);

            humidity.setText(weather.current_observation.relative_humidity);

            wind.setText(String.format(
                    Locale.getDefault(),
                    main_wind_format,
                    weather.current_observation.wind_dir,
                    weather.current_observation.wind_kph)
            );

            sunrise.setText(String.format(
                    Locale.getDefault(),
                    time_format,
                    weather.sun_phase.sunrise.hour,
                    weather.sun_phase.sunrise.minute)
            );

            sunset.setText(String.format(
                    Locale.getDefault(),
                    time_format,
                    weather.sun_phase.sunset.hour,
                    weather.sun_phase.sunset.minute)
            );
        }
    }

}
