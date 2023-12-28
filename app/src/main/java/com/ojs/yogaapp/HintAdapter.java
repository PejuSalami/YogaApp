package com.ojs.yogaapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HintAdapter extends ArrayAdapter {
    public HintAdapter(@NonNull Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        return position !=0;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        if(position == 0){
            textView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        } else {
            textView.setTextColor(getContext().getResources().getColor(android.R.color.black));
        }
        return view;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        if(position == 0) {
            textView.setText("");
            textView.setHint((CharSequence) getItem(position));
        }
        return view;
    }
}
