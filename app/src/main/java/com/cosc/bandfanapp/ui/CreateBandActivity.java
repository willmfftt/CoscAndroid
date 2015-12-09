package com.cosc.bandfanapp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import com.cosc.bandfanapp.R;
import com.cosc.bandfanapp.model.Band;
import com.cosc.bandfanapp.task.CreateBandTask;

public class CreateBandActivity extends AppCompatActivity {

    private CreateBandTask mTask;

    private EditText mNameEditText;
    private DatePicker mDateStartPicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_band);

        mNameEditText = (EditText) findViewById(R.id.name);
        mNameEditText.setError(null);

        mDateStartPicker = (DatePicker) findViewById(R.id.date_start);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.clear();
        inflater.inflate(R.menu.activity_create_band, menu);

        return true;
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
                        + (mDateStartPicker.getMonth() + 1) + "-" + mDateStartPicker.getDayOfMonth();

                mTask = new CreateBandTask(name, dateStart, new CreateBandTask.CreateBandListener() {
                    @Override
                    public void onBandCreated(Band band) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTask = null;
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onBandCreateFailed() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTask = null;
                                finish();
                            }
                        });
                    }
                });

                mTask.execute((Void) null);
            }
        }
        if (id == R.id.menu_cancel) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
