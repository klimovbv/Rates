package com.spb.kbv.ratestest.datepicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spb.kbv.ratestest.R;
import com.spb.kbv.ratestest.infrastructure.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class DateAdapter extends ArrayAdapter<Calendar>{
    private ArrayList<Calendar> dates;
    private LayoutInflater inflater;
    public ViewHolder holder;
    public DateAdapter(Activity activity) {
        super(activity, 0);
        inflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Calendar calendar = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.date_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        //sets red if weekend
        if (dayOfWeek == 1 || dayOfWeek == 7){
            holder.dateText.setTextColor(Color.RED);
        } else {
            holder.dateText.setTextColor(Color.BLACK);
        }

        //sets month name if last day of month
        if (dayOfMonth == 31 || (dayOfMonth - getItem(position + 1).get(Calendar.DAY_OF_MONTH)) > 1) {
            holder.dateMonthText.setText(Utils.DateFormat.addedMonthFormat(calendar.getTime()) + " ");
        } else {
            holder.dateMonthText.setText("");
        }

        holder.dateText.setText(Utils.DateFormat.listFormat(calendar.getTime()));
        holder.dateYearText.setText(Utils.DateFormat.addedCentralFormat(calendar.getTime()));
        holder.dateYearText.setVisibility(View.INVISIBLE);

        return convertView;
    };

    private class ViewHolder {
        public TextView dateText;
        public TextView dateMonthText;
        public TextView dateYearText;

        public ViewHolder (View view) {
            dateText = (TextView) view.findViewById(R.id.date_list_item_date);
            dateMonthText = (TextView)view.findViewById(R.id.date_list_item_month);
            dateYearText = (TextView) view.findViewById(R.id.date_list_item_year);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
