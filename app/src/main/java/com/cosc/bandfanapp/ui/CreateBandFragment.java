package com.cosc.bandfanapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.model.Band;
import com.cosc.bandfanapp.task.CreateBandTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateBandFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateBandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateBandFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private CreateBandTask mTask;

    private EditText mNameEditText;
    private DatePicker mDateStartPicker;

    public CreateBandFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateBandFragment.
     */
    public static CreateBandFragment newInstance() {
        return new CreateBandFragment();
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
        View v = inflater.inflate(R.layout.fragment_create_band, container, false);

        mNameEditText = (EditText) v.findViewById(R.id.name);
        mNameEditText.setError(null);

        mDateStartPicker = (DatePicker) v.findViewById(R.id.date_start);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.fragment_create_band, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_create) {
            // Create menu
            if (mTask == null) {
                String name = mNameEditText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    mNameEditText.setError("Field required");
                    return true;
                }

                String dateStart = String.valueOf(mDateStartPicker.getYear()) + "-"
                        + mDateStartPicker.getMonth() + "-" + mDateStartPicker.getDayOfMonth();

                mTask = new CreateBandTask(name, dateStart, new CreateBandTask.CreateBandListener() {
                    @Override
                    public void onBandCreated(Band band) {
                        mTask = null;
                        mListener.onFragmentInteraction(null);
                    }

                    @Override
                    public void onBandCreateFailed() {
                        mTask = null;
                        mListener.onFragmentInteraction(null);
                    }
                });

                mTask.execute((Void) null);
            }
        }
        if (id == R.id.menu_cancel) {
            mListener.onFragmentInteraction(null);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
