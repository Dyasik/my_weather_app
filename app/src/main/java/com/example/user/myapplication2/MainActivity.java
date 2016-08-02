package com.example.user.myapplication2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

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

        refresh(null);
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

                WeatherProvider.getInstance().makeRequest(loca, getApplicationContext());
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
}
