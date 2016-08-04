package com.example.user.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter{

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<ListItem> objects;

    private View rootView;

    public ListItemAdapter(Context context, ArrayList<ListItem> items) {
        ctx = context;
        objects = items;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ForecastFragment.LIST_SIZE;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        rootView = view;
        if (rootView == null) {
            rootView = lInflater.inflate(R.layout.forecast_item, viewGroup, false);
        }

        ListItem li = (ListItem) getItem(i);

        ((TextView)rootView.findViewById(R.id.forec_item_day)).setText(li.day);
        ((TextView)rootView.findViewById(R.id.forec_item_date)).setText(li.date);
        ((TextView)rootView.findViewById(R.id.forec_item_temp)).setText(li.temp);
        if (li.icon == null)
            ((ImageView)rootView.findViewById(R.id.forec_item_ico))
                    .setImageResource(android.R.drawable.ic_menu_report_image);
        else
            ((ImageView)rootView.findViewById(R.id.forec_item_ico))
                    .setImageBitmap(li.icon);

        return rootView;
    }
}
