package com.example.travelmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.travelmate.database.LocationModel;

import java.util.List;

public class CustomLocationAdapter extends ArrayAdapter<LocationModel> {

    private List<LocationModel> locations;

    public CustomLocationAdapter(Context context, int resource, List<LocationModel> locations) {
        super(context, resource, locations);
        this.locations = locations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        LocationModel location = locations.get(position);
        if (location != null) {
            TextView textView = view.findViewById(android.R.id.text1);
            if (textView != null) {
                // Ustaw widoczną nazwę jako adres
                textView.setText(location.getAddress());
            }
        }

        return view;
    }
}
