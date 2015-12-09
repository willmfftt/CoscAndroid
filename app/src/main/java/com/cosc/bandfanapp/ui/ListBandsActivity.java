package com.cosc.bandfanapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.adapter.BandListAdapter;
import com.cosc.bandfanapp.network.model.band.Band;
import com.cosc.bandfanapp.task.GetBandsTask;

import java.util.List;

public class ListBandsActivity extends AppCompatActivity {

    private ListView mListView;
    private BandListAdapter mAdapter;
    private ProgressBar mProgressBar;

    private GetBandsTask mTask;
    private Context mContext;
    private NavController mNavController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bands);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavController = new NavController(this, toolbar);

        mContext = this;

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mAdapter != null) {
                    Band band = mAdapter.getItem(i);

                    Intent intent = new Intent(ListBandsActivity.this, ReadBandMembersActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(ReadBandMembersActivity.BAND_ID, band.id);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        initialize();
    }

    private void initialize() {
        if (mTask == null) {
            showProgressBar(true);

            mTask = new GetBandsTask(new GetBandsTask.OnGetBandsResponseListener() {
                @Override
                public void onGetBandsResponse(final List<Band> bands) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTask = null;
                            mAdapter = new BandListAdapter(mContext, bands);
                            mListView.setAdapter(mAdapter);
                            showProgressBar(false);
                        }
                    });
                }

                @Override
                public void onGetBandsFailure() {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTask = null;
                            showProgressBar(false);
                        }
                    });
                }
            });

            mTask.execute((Void) null);
        }
    }

    private void showProgressBar(boolean shouldShow) {
        if (shouldShow) {
            mListView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

}
