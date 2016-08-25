package com.spb.kbv.ratestest.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.spb.kbv.ratestest.R;
import com.spb.kbv.ratestest.activities.RateActivity;
import com.spb.kbv.ratestest.datepicker.DateAdapter;
import com.spb.kbv.ratestest.datepicker.MyDatePicker;
import com.spb.kbv.ratestest.infrastructure.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements View.OnClickListener {

    private static final String YEAR_STATE = "YEAR_STATE";
    private static final String LIST_INDEX = "LIST_INDEX";

    private MyDatePicker mList;
    private DateAdapter mAdapter;
    private SharedPreferences mPrefs;

    @BindView(R.id.fragment_main_container) FrameLayout mFrame;
    @BindView(R.id.fragment_main_button_show_rate) Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        mList = new MyDatePicker(getActivity());
        mFrame.addView(mList, 0);
        mAdapter = new DateAdapter(getActivity());
        mButton.setOnClickListener(this);
        mList.setAdapter(mAdapter);

        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       mList.post(() -> {
           if (!mList.getAdapter().isEmpty()) {
               int offset = (mList.getHeight() - mList.getChildAt(0).getHeight()) / 2;
               mList.setSelectionFromTop(mPrefs.getInt(LIST_INDEX, 0), offset);
           }
       });
    }



    @Override
    public void onPause() {
        int index = mList.getCentralItem();
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putInt(LIST_INDEX, index);
        edit.apply();

        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (!mList.getAdapter().isEmpty() && isOnline(getActivity())) {
            int centralItem = mList.getCentralItem();
            Calendar calendar = mAdapter.getItem(centralItem - 1);//Because of added header need to minus 1 position
            final Intent intent = new Intent(getActivity(), RateActivity.class);
            intent.putExtra(RateActivity.EXTRA_DATE, Utils.DateFormat.queryFormatString(calendar.getTime()));
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void setCalendar() {
        int year = mPrefs.getInt(YEAR_STATE, 2015);

        ArrayList<Calendar> dates = datesArray(year);
        mAdapter.clear();
        mAdapter.addAll(dates);
    }


    public ArrayList<Calendar> datesArray(int year){
        int numberOfDays = (year % 4 == 0) ? 366 : 365;
        ArrayList<Calendar> days = new ArrayList<>();
        for (int i = 1; i <= numberOfDays; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_YEAR, i);
            days.add(calendar);
        }
        return days;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
