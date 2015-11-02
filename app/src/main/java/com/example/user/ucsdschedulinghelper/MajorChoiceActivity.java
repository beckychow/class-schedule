package com.example.user.ucsdschedulinghelper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MajorChoiceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_major_choice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            changeText();
        } catch (Exception e)
        {
            Log.e("MajorChoiceActivity", "changeText()" + e);
        }
    }

    private void changeText() throws ExecutionException, InterruptedException{
        /*FetchDataFromHttp fetchDataFromHttp = new FetchDataFromHttp(getApplicationContext(),
                "http://www.ucsd.edu/catalog/courses/CSE.html");
        final TextView textView = (TextView) this.findViewById(R.id.message);
        textView.setText(fetchDataFromHttp.getResults());*/
        CourseDescriptionParser cdp = new CourseDescriptionParser(this,
                                            "http://www.ucsd.edu/catalog/courses/CSE.html");
        cdp.parseContentToDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_major_choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
