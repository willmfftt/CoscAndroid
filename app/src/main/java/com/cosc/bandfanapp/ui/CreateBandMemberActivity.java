package com.cosc.bandfanapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.network.model.band.Band;
import com.cosc.bandfanapp.task.CreateBandMemberTask;
import com.cosc.bandfanapp.task.GetBandsTask;

import java.util.ArrayList;
import java.util.List;

public class CreateBandMemberActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private AppCompatSpinner mSpinner;
    private EditText mFirstName;
    private EditText mLastName;
    private DatePicker mDateStart;

    private Context mContext;
    private NavController mNavController;

    private CreateBandMemberTask mTask;

    private List<Band> mBands;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_band_member);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavController = new NavController(this, toolbar);

        mContext = this;

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSpinner = (AppCompatSpinner) findViewById(R.id.spinner);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mDateStart = (DatePicker) findViewById(R.id.date_start);

        initialize();
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.activity_create_band_member, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_create:
                if (mBands != null) {
                    Band band = mBands.get(mSpinner.getSelectedItemPosition());
                    int bandId = band.id;
                    String firstName = mFirstName.getText().toString();
                    String lastName = mLastName.getText().toString();
                    String dateStart = String.valueOf(mDateStart.getYear()) + "-"
                            + (mDateStart.getMonth() + 1) + "-" + mDateStart.getDayOfMonth();

                    if (bandId > -1 && !TextUtils.isEmpty(firstName)
                            && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(dateStart)) {
                        if (mTask == null) {
                            mTask = new CreateBandMemberTask(firstName, lastName, bandId, dateStart
                                    , new CreateBandMemberTask.OnCreateBandMemberListener() {
                                @Override
                                public void onCreateBandMember(int id) {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });
                                }

                                @Override
                                public void onCreateBandMemberFailure() {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });
                                }
                            });
                            mTask.execute((Void) null);
                        }
                    }
                }
                finish();
                break;
            case R.id.menu_cancel:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        showProgressBar(true);

        GetBandsTask getBandsTask = new GetBandsTask(new GetBandsTask.OnGetBandsResponseListener() {
            @Override
            public void onGetBandsResponse(final List<Band> bands) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar(false);
                        mBands = bands;
                        List<String> bandNames = new ArrayList<>();
                        for (Band band : bands) {
                            bandNames.add(band.name);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext
                                , android.R.layout.simple_spinner_dropdown_item, bandNames);
                        mSpinner.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onGetBandsFailure() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar(false);
                        finish();
                    }
                });
            }
        });
        getBandsTask.execute((Void) null);
    }

    private void showProgressBar(boolean shouldShow) {
        if (shouldShow) {
            mSpinner.setVisibility(View.GONE);
            mFirstName.setVisibility(View.GONE);
            mLastName.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSpinner.setVisibility(View.VISIBLE);
            mFirstName.setVisibility(View.VISIBLE);
            mLastName.setVisibility(View.VISIBLE);
        }
    }

}
