package com.zasko.datetimebarlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zasko on 2018/3/23.
 */

public class TestActivity extends AppCompatActivity {



    private Button mButton;


    String string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_layout);


        mButton = (Button)findViewById(R.id.button01);


    }
}
