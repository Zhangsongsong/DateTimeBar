package com.zasko.datetimebarlib;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        Log.d(TAG, "onCreate: ----->" + SimpleDateFormat.getInstance().getTimeZone().getRawOffset());
        long offsetTime = SimpleDateFormat.getInstance().getTimeZone().getRawOffset();
        mDateTimeBarView.setCurrentTime(System.currentTimeMillis() + offsetTime);

        mDateTimeBarView.setOnTimeBarMoveListener(new DateTimeBarView.OnTimeBarMoveListener() {
            @Override
            public void onTimeBarMove(long time) {

            }
        });

    }

    @BindView(R.id.date_time_bar_view)
    DateTimeBarView mDateTimeBarView;


}
