package com.example.ucsdschedulinghelper.ui.fourYearPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.ucsdschedulinghelper.MajorChoiceActivity;
import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.ui.courseListView.ListViewLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Freddy on 11/14/2015.
 */
public class fypView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    LinkedHashMap<String, List<String>> quarters;
    List<String> classes;
    ExpandableListView exp_list;
    fypAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setContentView(R.layout.nav_drawer_fyp);
        exp_list = (ExpandableListView) findViewById(R.id.expandableListView);
        quarters = fypData.getInfo(getApplicationContext());
        classes = new ArrayList<String>(quarters.keySet());
        adapter = new fypAdapter(this, quarters, classes);
        exp_list.setAdapter(adapter);

        /* Navigation Drawer */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_course_list) {
            Intent intent = new Intent(this, ListViewLoader.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, MajorChoiceActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_fyp) {
            // already in this view
        }
        else if (id == R.id.nav_courses_completed) {
            Intent intent = new Intent(this, ListViewLoader.class);
            intent.putExtra(ListViewLoader.CONFIG, ListViewLoader.SHOW_ONLY_COMPLETED);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_courses_interested) {
            Intent intent = new Intent(this, ListViewLoader.class);
            intent.putExtra(ListViewLoader.CONFIG, ListViewLoader.SHOW_ONLY_INTERESTED);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
