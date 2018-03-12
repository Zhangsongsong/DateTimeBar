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


    @OnClick(R.id.open_apk_btn)
    void onClickApk(View view) {

        String path = Environment.getExternalStorageDirectory().getPath() + "/JAGles/test.apk";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }


}
