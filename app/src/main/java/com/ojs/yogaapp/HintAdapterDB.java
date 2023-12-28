package com.ojs.yogaapp;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HintAdapterDB extends ArrayAdapter<String> {

    public HintAdapterDB(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        // Disable the first item
        return position != 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        if (position == 0) {
            // Set the hint text color
            textView.setTextColor(Color.GRAY);
        } else {
            textView.setTextColor(Color.BLACK);
        }
        return view;
    }
}
