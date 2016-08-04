package com.example.user.myapplication2;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class ListPicturesProvider {

    private ArrayList<String> urls;
    private PicturesListener listener;
    private Object[] pics;

    public interface PicturesListener {
        void onReceivedPictures(Object[] pics);
    }
    
    private static ListPicturesProvider instance;
    
    private ListPicturesProvider(ArrayList<String> urls) {
        this.urls = urls;
    }
    
    public static ListPicturesProvider getInstance(ArrayList<String> urls) {
        if (instance == null) {
            instance = new ListPicturesProvider(urls);
        }
        return instance;
    }
    
    public void setListener(PicturesListener listener) {
        this.listener = listener;
    }
    
    public void startQueue(final Context context) {
        pics = new Object[urls.size()];
        for (int i = 0; i < pics.length; i++) {
            pics[i] = new Object();
        }
        makeRequest(0, context);
    }

    private void makeRequest(final int index, final Context context) {

        ImageRequest req = new ImageRequest(
                urls.get(index),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        pics[index] = response;
                        if (index == urls.size() - 1) {
                            listener.onReceivedPictures(pics);
                        } else
                            makeRequest(index + 1, context);
                    }
                }, 0, 0, null, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pics[index] = null;
                        if (index == urls.size() - 1) {
                            listener.onReceivedPictures(pics);
                        } else
                            makeRequest(index + 1, context);
                    }
                }
        );

        Volley.newRequestQueue(context).add(req);

    }
    
}
