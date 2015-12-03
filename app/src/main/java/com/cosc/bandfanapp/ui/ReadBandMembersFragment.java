package com.cosc.bandfanapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.adapter.BandMemberListAdapter;
import com.cosc.bandfanapp.model.BandMember;
import com.cosc.bandfanapp.task.ReadBandMembersTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadBandMembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadBandMembersFragment extends Fragment {

    private static final String BAND_ID = "band_id";

    private OnFragmentInteractionListener mListener;
    private int mBandId;

    private ProgressBar mProgressBar;
    private ListView mListView;
    private BandMemberListAdapter mAdapter;

    private Context mContext;

    private ReadBandMembersTask mTask;

    public ReadBandMembersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReadBandMembersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadBandMembersFragment newInstance(int bandId) {
        ReadBandMembersFragment fragment = new ReadBandMembersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BAND_ID, bandId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if (getArguments() != null) {
            mBandId = getArguments().getInt(BAND_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_read_band_members, container, false);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mListView = (ListView) v.findViewById(R.id.list_view);

        initialize();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
                            mListener.onFragmentInteraction();
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
