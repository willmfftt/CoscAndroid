package com.cosc.bandfanapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateBandMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBandMemberFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ProgressBar mProgressBar;
    private AppCompatSpinner mSpinner;
    private EditText mFirstName;
    private EditText mLastName;
    private DatePicker mDateStart;

    private CreateBandMemberTask mTask;

    private List<Band> mBands;

    public CreateBandMemberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateBandMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateBandMemberFragment newInstance() {
        return new CreateBandMemberFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_create_band_member, container, false);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mSpinner = (AppCompatSpinner) v.findViewById(R.id.spinner);
        mFirstName = (EditText) v.findViewById(R.id.first_name);
        mLastName = (EditText) v.findViewById(R.id.last_name);
        mDateStart = (DatePicker) v.findViewById(R.id.date_start);

        initialize();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.fragment_create_band_member, menu);
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
                                    mListener.onFragmentInteraction();
                                }

                                @Override
                                public void onCreateBandMemberFailure() {
                                    mListener.onFragmentInteraction();
                                }
                            });
                            mTask.execute((Void) null);
                        }
                    }
                }
                mListener.onFragmentInteraction();
                break;
            case R.id.menu_cancel:
                mListener.onFragmentInteraction();
                break;
        }

        return super.onOptionsItemSelected(item);
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext()
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
                        mListener.onFragmentInteraction();
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
