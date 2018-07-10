package com.app.knbs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.knbs.R;
import com.app.knbs.adapter.model.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Developed by Rodney on 11/11/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<Report> {
    private Context mContext;
    private ArrayList<Report> listState;
    private SpinnerAdapter spinnerAdapter;
    private boolean isFromView = false;

    private ArrayList<String> listChecked = new ArrayList<>();
    public static String listString = "";

    public SpinnerAdapter(Context context, int resource, List<Report> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<Report>) objects;
        this.spinnerAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              final ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                listString = "";
                if (listChecked.contains(listState.get(getPosition).getTitle())) {
                    listChecked.remove(listState.get(getPosition).getTitle());
                } else {
                    listChecked.add(listState.get(getPosition).getTitle());
                }

                for (String s : listChecked){
                    listString += s + ",";
                }
                if(listString.endsWith(",")) {
                    listString= listString.substring(0, listString.length() - 1);
                }
                Toast.makeText(parent.getContext(), listString, Toast.LENGTH_LONG).show();

            }
        });


        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}