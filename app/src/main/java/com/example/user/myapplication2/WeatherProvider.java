package com.example.user.myapplication2;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class WeatherProvider {

    public interface CurrentWeatherListener {
        void onReceivedWeather(ApiResponse now);
    }

    private static volatile WeatherProvider instance;

    private WeatherProvider() {}

    public static WeatherProvider getInstance() {
        if (instance == null)
            synchronized (WeatherProvider.class) {
                if (instance == null)
                    instance = new WeatherProvider();
            }
        return instance;
    }

    private WeakReference<CurrentWeatherListener> listener;

    private final String API_KEY = "f24de21d795c6491";

    private final String CLASS_NAME = getClass().getSimpleName();

    private ApiResponse apiResponse;

    private long lastRequestTime;

    public void setListener(CurrentWeatherListener rl) {
        listener = new WeakReference<>(rl);
    }

    public void makeRequest(Location location, final Context context) {

        // Remember last request time
        lastRequestTime = location.getTime();

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        String request_url = String.format(Locale.getDefault(),
                "http://api.wunderground.com/api/%s/%s/q/%f,%f.json",
                API_KEY,
                "conditions/forecast10day/astronomy",
                location.getLatitude(),
                location.getLongitude());

        // RESPONSE LISTENER
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                apiResponse = new GsonBuilder().create().fromJson(response, ApiResponse.class);
                Log.w(CLASS_NAME, "Response object is parsed from JSON");
                listener.get().onReceivedWeather(apiResponse);
            }
        };
        // RESPONSE ERROR LISTENER
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(CLASS_NAME, "Error occurred while waiting for response");
                Toast.makeText(context, "Failed to refresh weather. Try again.",
                        Toast.LENGTH_LONG).show();
            }
        };

        // Request a string response from the provided URL
        StringRequest request = new StringRequest(
                Request.Method.GET,
                request_url,
                responseListener,
                errorListener);

        // Add the request to the RequestQueue.
        queue.add(request);
    }
}
