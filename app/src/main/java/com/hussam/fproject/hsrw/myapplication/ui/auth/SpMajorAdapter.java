package com.hussam.fproject.hsrw.myapplication.ui.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hussam.fproject.hsrw.myapplication.R;

import java.util.List;


public class SpMajorAdapter extends ArrayAdapter<String> {

    public SpMajorAdapter(Context context, List<String> majorList) {
        super(context, R.layout.item_spinner_row, majorList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return firstView(position, convertView, parent);
    }

    private View firstView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_spinner_header, parent, false
            );
        }

        TextView textViewName = view.findViewById(R.id.tv_item_spinner);

        if (getItem(position) != null) {
            textViewName.setText(getItem(position));
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_spinner_row, parent, false
            );
        }

        TextView textViewName = view.findViewById(R.id.tv_item_spinner);

        if (getItem(position) != null) {
            textViewName.setText(getItem(position));
        }

        return view;
    }


}