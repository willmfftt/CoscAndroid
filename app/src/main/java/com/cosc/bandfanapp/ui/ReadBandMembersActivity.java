package com.cosc.bandfanapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.adapter.BandMemberListAdapter;
import com.cosc.bandfanapp.model.BandMember;
import com.cosc.bandfanapp.task.ReadBandMembersTask;

import java.util.List;

public class ReadBandMembersActivity extends AppCompatActivity {

    public static final String BAND_ID = "band_id";

    private int mBandId;

    private ProgressBar mProgressBar;
    private ListView mListView;
    private BandMemberListAdapter mAdapter;

    private Context mContext;

    private ReadBandMembersTask mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_band_members);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mContext = this;

        if (getIntent().getExtras() != null) {
            mBandId = getIntent().getExtras().getInt(BAND_ID);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mListView = (ListView) findViewById(R.id.list_view);

        initialize();
    }

    private void initialize() {
        if (mTask == null) {
            showProgressBar(true);
            mTask = new ReadBandMembersTask(mBandId
                    , new ReadBandMembersTask.OnReadBandMembersListener() {
                @Override
                public void onReadBandMembers(final List<BandMember> bandMembers) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showProgressBar(false);
                            mTask = null;
                            mAdapter = new BandMemberListAdapter(mContext, bandMembers);
                            mListView.setAdapter(mAdapter);
                        }
                    });
                }

                @Override
                public void onReadBandMembersFailure() {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showProgressBar(false);
                            mTask = null;
                            finish();
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
