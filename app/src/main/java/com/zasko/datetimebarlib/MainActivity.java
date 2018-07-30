package com.zasko.datetimebarlib;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        long offsetTime = SimpleDateFormat.getInstance().getTimeZone().getRawOffset();
        mDateTimeBarView.setCurrentTime(System.currentTimeMillis() + offsetTime);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        mDateTimeBarView.setOnTimeBarMoveListener(new DateTimeBarView.OnTimeBarMoveListener() {
            @Override
            public void onTimeBarMove(long time) {
                mTimeTv.setText(simpleDateFormat.format(new Date(time)));
            }
        });

    }

    @BindView(R.id.date_time_bar_view)
    DateTimeBarView mDateTimeBarView;
    @BindView(R.id.time_tv)
    TextView mTimeTv;

}
