package com.spb.kbv.ratestest.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spb.kbv.ratestest.R;
import com.spb.kbv.ratestest.infrastructure.RatesLoader;
import com.spb.kbv.ratestest.infrastructure.Utils;
import java.util.HashMap;

public class RateActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<HashMap<String,String>> {
    public static final String EXTRA_DATE = "EXTRA_DATE";

    private static final int LOADER_ID = 1;
    private TextView dollarRate;
    private TextView euroRate;
    private FrameLayout progressFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String date = getIntent().getStringExtra(EXTRA_DATE);

        TextView dateTextView = (TextView) findViewById(R.id.activity_rate_date);
        dateTextView.setText(Utils.DateFormat.infoFormat(date));

        dollarRate = (TextView)findViewById(R.id.acticvity_rate_dol);
        dollarRate.setVisibility(View.INVISIBLE);

        euroRate = (TextView)findViewById(R.id.activity_rate_eur);
        euroRate.setVisibility(View.INVISIBLE);

        progressFrame = (FrameLayout) findViewById(R.id.acticvity_rate_progress_frame);
        progressFrame.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString(RatesLoader.DATE_ARGS, date);

        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View rootView = findViewById(android.R.id.content);
        rootView.setAlpha(0);
        rootView.animate().alpha(1).setDuration(450).start();
    }

    @Override
    public Loader<HashMap<String,String>> onCreateLoader(int id, Bundle args) {
        progressFrame.setVisibility(View.VISIBLE);
        Loader <HashMap<String,String>> mLoader;
        mLoader = new RatesLoader(this, args);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String,String>> loader, HashMap<String,String> data) {
        if (data.get("Error") != null) {
            Toast.makeText(this, "Error on server: " + data.get("Error"), Toast.LENGTH_SHORT).show();
            finish();
        }

        dollarRate.setText("1$ = " + data.get("USD"));
        euroRate.setText("1 EUR = " + data.get("EUR"));

        progressFrame.setVisibility(View.GONE);
        dollarRate.setVisibility(View.VISIBLE);
        euroRate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String,String>> loader) {
    }
}
