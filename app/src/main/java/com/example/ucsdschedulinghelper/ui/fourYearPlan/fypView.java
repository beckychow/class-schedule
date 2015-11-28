package com.example.ucsdschedulinghelper.ui.fourYearPlan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.ucsdschedulinghelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Freddy on 11/14/2015.
 */
public class fypView extends Activity {
    LinkedHashMap<String, List<String>> quarters;
    List<String> classes;
    ExpandableListView exp_list;
    fypAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_year_plan);
        exp_list = (ExpandableListView) findViewById(R.id.expandableListView);
        quarters = fypData.getInfo(getApplicationContext());
        classes = new ArrayList<String>(quarters.keySet());
        adapter = new fypAdapter(this, quarters, classes);
        exp_list.setAdapter(adapter);
    }

}
