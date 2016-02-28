package com.spb.kbv.ratestest.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.spb.kbv.ratestest.R;
import com.spb.kbv.ratestest.datepicker.DateAdapter;
import com.spb.kbv.ratestest.datepicker.MyDatePicker;
import com.spb.kbv.ratestest.infrastructure.Utils;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String YEAR_STATE = "YEAR_STATE";

    private ArrayList<Calendar> dates;
    private MyDatePicker list;
    private DateAdapter adapter;
    private View rootView;
    private int year;
    private SharedPreferences shPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

       /* toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        shPrefs = getPreferences(MODE_PRIVATE);
        rootView = findViewById(android.R.id.content);

        FrameLayout container = (FrameLayout)findViewById(R.id.activity_main_container);
        list = new MyDatePicker(this);
        container.addView(list, 0);
        adapter = new DateAdapter(this);

        Button button = (Button)findViewById(R.id.activity_main_button_show_rate);
        button.setOnClickListener(this);

        setCalendar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rootView.setAlpha(0);
        rootView.animate().alpha(1).setDuration(450).start();
    }

    @Override
    public void onClick(View v) {
        if (isOnline(this)) {
            rootView.animate().alpha(0).setDuration(450).start();
            int centralItem = list.getCentralItem();
            Calendar calendar = adapter.getItem(centralItem);
            final Intent intent = new Intent(this, RateActivity.class);
            intent.putExtra(RateActivity.EXTRA_DATE, Utils.DateFormat.queryFormatString(calendar.getTime()));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_change_year, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_main_change_year) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Change Year To:");
            final EditText editYear = new EditText(this);
            editYear.setInputType(InputType.TYPE_CLASS_NUMBER);
            dialog.setView(editYear);
            dialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    changeYear(Integer.parseInt(editYear.getText().toString()));
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
            return true;
        }
        return false;
    }

    private void changeYear(int year) {
        dates = datesArray(year);
        adapter.clear();
        adapter.addAll(dates);
        saveYear();
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

    private void saveYear() {
        SharedPreferences.Editor editor = shPrefs.edit();
        editor.putInt(YEAR_STATE, year);
        editor.apply();
    }

    private void setCalendar() {
        if (shPrefs != null)
            year = shPrefs.getInt(YEAR_STATE, 2015);
        else
            year = 2015;

        dates = datesArray(year);
        adapter.addAll(dates);
        list.setAdapter(adapter);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
