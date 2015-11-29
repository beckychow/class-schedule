package com.example.ucsdschedulinghelper.ui;

/**
 * Created by bharatsubramaniam on 11/29/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ucsdschedulinghelper.MajorChoiceActivity;
import com.example.ucsdschedulinghelper.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {

    //Set waktu lama splashscreen
    private static int splashInterval = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(Splash.this, MajorChoiceActivity.class);
                startActivity(i);
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);

    };

}