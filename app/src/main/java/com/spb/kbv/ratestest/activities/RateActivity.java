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

import java.io.BufferedWriter;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<HashMap<String,String>> {

    public static final String EXTRA_DATE = "EXTRA_DATE";

    private static final int LOADER_ID = 1;

    @BindView(R.id.include_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_rate_date) TextView mDateTextView;
    @BindView(R.id.acticvity_rate_dol) TextView mDollarRate;
    @BindView(R.id.activity_rate_eur) TextView mEuroRate;
    @BindView(R.id.acticvity_rate_progress_frame) FrameLayout mProgressFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);

        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        mToolbar.setNavigationOnClickListener(v -> finish());

        String date = getIntent().getStringExtra(EXTRA_DATE);

        mDateTextView.setText(Utils.DateFormat.infoFormat(date));
        mDollarRate.setVisibility(View.INVISIBLE);
        mEuroRate.setVisibility(View.INVISIBLE);

        mProgressFrame.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString(RatesLoader.DATE_ARGS, date);

        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
    }


    @Override
    public Loader<HashMap<String,String>> onCreateLoader(int id, Bundle args) {
        mProgressFrame.setVisibility(View.VISIBLE);
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

        mDollarRate.setText("1$ = " + data.get("USD"));
        mEuroRate.setText("1 EUR = " + data.get("EUR"));

        mProgressFrame.setVisibility(View.GONE);
        mDollarRate.setVisibility(View.VISIBLE);
        mEuroRate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String,String>> loader) {
    }
}
