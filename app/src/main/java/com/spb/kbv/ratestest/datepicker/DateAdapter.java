package com.spb.kbv.ratestest.datepicker;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spb.kbv.ratestest.R;
import com.spb.kbv.ratestest.infrastructure.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateAdapter extends ArrayAdapter<Calendar>{
    private LayoutInflater mInflater;
    private ViewHolder mHolder;
    public DateAdapter(Activity activity) {
        super(activity, 0);
        mInflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Calendar calendar = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.date_list_item, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder)convertView.getTag();
        }

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        //sets red if weekend
        if (dayOfWeek == 1 || dayOfWeek == 7){
            mHolder.dateText.setTextColor(Color.RED);
        } else {
            mHolder.dateText.setTextColor(Color.BLACK);
        }

        //sets month name if last day of month
        if (dayOfMonth == 31 || (dayOfMonth - getItem(position + 1).get(Calendar.DAY_OF_MONTH)) > 1) {
            mHolder.dateMonthText.setText(Utils.DateFormat.addedMonthFormat(calendar.getTime()) + " ");
        } else {
            mHolder.dateMonthText.setText("");
        }

        mHolder.dateText.setText(Utils.DateFormat.listFormat(calendar.getTime()));
        mHolder.dateYearText.setText(Utils.DateFormat.addedCentralFormat(calendar.getTime()));
        mHolder.dateYearText.setVisibility(View.INVISIBLE);

        return convertView;
    };

    public class ViewHolder {
        @BindView(R.id.date_list_item_date) TextView dateText;
        @BindView(R.id.date_list_item_month) TextView dateMonthText;
        @BindView(R.id.date_list_item_year) TextView dateYearText;

        public ViewHolder (View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
