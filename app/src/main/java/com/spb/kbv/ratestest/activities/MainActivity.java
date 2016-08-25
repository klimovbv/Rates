package com.spb.kbv.ratestest.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.spb.kbv.ratestest.R;
import com.spb.kbv.ratestest.datepicker.DateAdapter;
import com.spb.kbv.ratestest.datepicker.MyDatePicker;
import com.spb.kbv.ratestest.fragments.MainFragment;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    public static final String YEAR_STATE = "YEAR_STATE";

    private SharedPreferences mPrefs;

    private MainFragment mListFragment;

    @BindView(R.id.include_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mListFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_main_fragment);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        mPrefs = getPreferences(MODE_PRIVATE);

        mListFragment.setCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_change_year, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_main_change_year) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            EditText editYear = new EditText(this);
            editYear.setInputType(InputType.TYPE_CLASS_NUMBER);
            dialogBuilder.setView(editYear);
            dialogBuilder.setMessage("Change Year To:");
            dialogBuilder.setPositiveButton("Change", (dialogInterface, i) -> {
                        int newYear = Integer.parseInt(editYear.getText().toString());
                        saveYear(newYear);
                        mListFragment.setCalendar();
                    }
            );
            dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {});

            AlertDialog dialog = dialogBuilder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            //make positive dialog's button disabled until some text entered
            editYear.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    if (s.length() < 1) {
                        positiveButton.setEnabled(false);
                    } else {
                        positiveButton.setEnabled(true);
                    }
                }
            });
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            return true;
        }
        return false;
    }

    private void saveYear(int year) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(YEAR_STATE, year);
        editor.apply();
    }
}
