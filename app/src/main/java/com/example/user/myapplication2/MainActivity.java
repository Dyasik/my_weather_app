package com.example.user.myapplication2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements WeatherProvider.WeatherListener {

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

    /* Vars needed to receive location */
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
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                getResources());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /* This one is needed to receive location */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        /*getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;*/
//        return false; // Menu is not needed yet
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void refresh(View ImgBtn){
        Log.w(CLASS_NAME, "Refresh started");

        Toast.makeText(this, R.string.refreshing, Toast.LENGTH_SHORT).show();

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
        weatherProvider.addListener(this);
        weatherProvider.makeRequest(loca, this);
    }

    @Override
    public void onReceivedWeather(ApiResponse weather) {
        //redrawNowFromResponse(weather);
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
