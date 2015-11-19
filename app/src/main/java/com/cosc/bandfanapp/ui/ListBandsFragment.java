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
import com.cosc.bandfanapp.adapter.BandListAdapter;
import com.cosc.bandfanapp.network.model.band.Band;
import com.cosc.bandfanapp.task.GetBandsTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListBandsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListBandsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private BandListAdapter mAdapter;
    private ProgressBar mProgressBar;

    private GetBandsTask mTask;

    public ListBandsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListBandsFragment.
     */
    public static ListBandsFragment newInstance() {
        ListBandsFragment fragment = new ListBandsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_bands, container, false);

        mListView = (ListView) v.findViewById(R.id.list_view);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                            mAdapter = new BandListAdapter(getContext(), bands);
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
