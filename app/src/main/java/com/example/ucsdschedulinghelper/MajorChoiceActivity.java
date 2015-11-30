package com.example.ucsdschedulinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import java.util.concurrent.ExecutionException;

import com.example.ucsdschedulinghelper.parser.CourseDescriptionParser;
import com.example.ucsdschedulinghelper.parser.PlanParser;
import com.example.ucsdschedulinghelper.ui.courseListView.ListViewLoader;
import com.example.ucsdschedulinghelper.ui.fourYearPlan.fypView;

public class MajorChoiceActivity extends AppCompatActivity {

    private String majorCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_major_choice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.buttonSubmitInfo).setBackgroundResource(android.R.drawable.btn_default);
        findViewById(R.id.buttonToFourYearPlan).setBackgroundResource(android.R.drawable.btn_default);
        findViewById(R.id.buttonToCourseListView).setBackgroundResource(android.R.drawable.btn_default);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        parseCourses();
    }

    // test parser
    private void parseCourses() {
        CourseDescriptionParser cdp = new CourseDescriptionParser(this, getContentResolver(),
                "http://www.ucsd.edu/catalog/courses/CSE.html");
        cdp.parseContent();
    }

    private void parsePlan(String collegeCode, String majorCode) throws ExecutionException, InterruptedException{
        /*FetchDataFromHttp fetchDataFromHttp = new FetchDataFromHttp(getApplicationContext(),
                "http://www.ucsd.edu/catalog/courses/CSE.html");
        final TextView textView = (TextView) this.findViewById(R.id.message);*/

        PlanParser pdp = new PlanParser(this, getContentResolver(),
                "http://plans.ucsd.edu/controller.php?action=LoadPlans&college="
                + collegeCode + "&year=2015&major=" + majorCode);
        pdp.parseContent();
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

    private static final int REQUEST_CODE = 01;
    public void submitInfo(View view) {
        updateButtons(false, true);
        try {
            parsePlan("WA", majorCode);
        }
        catch (Exception e) {
            Log.e(getLocalClassName(), "parsePlan()" + e);
        }
    }

    public void showCourseList(View view) {
        Intent intent = new Intent(this, ListViewLoader.class);
        startActivity(intent);
    }

    public void showFourYearPlan(View view) {
        Intent intent = new Intent(this, fypView.class);
        startActivity(intent);
    }

    // Selecting major through Radio Buttons
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.cs_button:
                if (checked)
                    majorCode = "CS26";
                    break;
            case R.id.cs_bio_button:
                if (checked)
                    majorCode = "CS27";
                    break;
            case R.id.comp_eng_button:
                if (checked)
                    majorCode = "CS25";
                    break;
            case R.id.cs_ba_button:
                if (checked)
                    majorCode = "CS28";
                    break;
        }
        updateButtons(true, false);
    }

    private void updateButtons(boolean submissionAccess, boolean navigationAccess) {
        findViewById(R.id.buttonSubmitInfo).setEnabled(submissionAccess);
        findViewById(R.id.buttonToCourseListView).setEnabled(navigationAccess);
        findViewById(R.id.buttonToFourYearPlan).setEnabled(navigationAccess);
    }

}
