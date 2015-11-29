package com.example.ucsdschedulinghelper.ui.courseListView;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.support.design.widget.NavigationView;


import com.example.ucsdschedulinghelper.MajorChoiceActivity;
import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.database.CoursesCollectionContract;
import com.example.ucsdschedulinghelper.provider.DbContentProvider;
import com.example.ucsdschedulinghelper.ui.fourYearPlan.fypView;

import java.util.ArrayList;

/**
 * Created by SKE on 5/11/15.
 */
public class ListViewLoader extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
    SearchView mSearchView;
    ListView mListView;

    //There are the Courses rows that we will retrieve
    private static final String[] PROJECTION = new String[] {
            CoursesCollectionContract.Course._ID,
            CoursesCollectionContract.Course.COLUMN_DEPARTMENT,
            CoursesCollectionContract.Course.COLUMN_CODE,
            CoursesCollectionContract.Course.COLUMN_TITLE,
            CoursesCollectionContract.Course.COLUMN_COMPLETED,
            CoursesCollectionContract.Course.COLUMN_IN_PROGRESS
    };

    // This is the select criteria
    private static String SELECTION = "";
    private String[] selectionArgs = null;
    private static String sortOrder = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {
                CoursesCollectionContract.Course.COLUMN_DEPARTMENT,
                CoursesCollectionContract.Course.COLUMN_CODE,
                CoursesCollectionContract.Course.COLUMN_TITLE,
                CoursesCollectionContract.Course._ID };
        int[] toViews = {
                R.id.list_item_text_department,
                R.id.list_item_text_code,
                R.id.list_item_text_title,
                R.id.list_item_text_id};

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this, R.layout.course_list_item, null,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);


        setContentView(R.layout.navigation_drawer);
        mSearchView = (SearchView) findViewById(R.id.list_search);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setTextFilterEnabled(true);
        setupSearchView();


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



    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, DbContentProvider.CONTENT_COURSES_URI,
                PROJECTION, SELECTION, selectionArgs, sortOrder);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, CourseDetailedView.class);
        TextView courseIDView = (TextView) v.findViewById(R.id.list_item_text_id);
        Uri courseUri = Uri.withAppendedPath(DbContentProvider.CONTENT_COURSES_URI, courseIDView.getText().toString());

        //Log.e(getClass().getSimpleName() ,"Uri id = " + courseIDView.getText().toString());

        intent.putExtra(DbContentProvider.CONTENT_COURSES_ITEM_TYPE, courseUri);

        //Log.e(getClass().getSimpleName(), "Uri = " + courseUri.toString());

        startActivity(intent);
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search here");

        //View.OnFocusChangeListener onFocusChangeListener = new FocusChangeListener(this);
        //mSearchView.setOnFocusChangeListener(onFocusChangeListener);
    }

    @Override
    public boolean onQueryTextChange(String filter) {
        if (TextUtils.isEmpty(filter)) {
            SELECTION = "";
            selectionArgs = null;
        }
        else {
            String openPar = "(";
            String closePar = ")";
            String matchKey = "%";
            String likeKey = " LIKE?";
            String orKey = " OR ";
            String andKey = " AND ";

            String[] filterWords = filter.split("\\s");

            ArrayList<String> selArgs = new ArrayList<>();
            boolean isCourseCodeSearch = false;

            SELECTION = "";
            for (int index = 0; index < filterWords.length; index++) {
                if (Character.isDigit(filterWords[index].charAt(0))) {
                    if (index > 0) {
                        int lastOrIndex = SELECTION.lastIndexOf(orKey);
                        int lastAndIndex = SELECTION.lastIndexOf(andKey);
                        int lastKeyIndex = Math.max(lastOrIndex, lastAndIndex);
                        SELECTION = SELECTION.substring(0, lastKeyIndex);
                        SELECTION += andKey;
                    }

                    SELECTION += openPar + CoursesCollectionContract.Course.COLUMN_CODE +
                            likeKey + closePar;
                    isCourseCodeSearch = true;
                    selArgs.add(matchKey + filterWords[index] + matchKey);
                }
                else {
                    SELECTION += openPar + openPar + CoursesCollectionContract.Course.COLUMN_DEPARTMENT +
                            likeKey + closePar + orKey +
                            openPar + CoursesCollectionContract.Course.COLUMN_DESCRIPTION +
                            likeKey + closePar + closePar;

                    selArgs.add(matchKey + filterWords[index] + matchKey);
                    selArgs.add(matchKey + filterWords[index] + matchKey);
                }

                if (index < filterWords.length - 1) {
                    if (isCourseCodeSearch)
                        SELECTION += andKey;
                    else
                        SELECTION += orKey;
                    isCourseCodeSearch = false;
                }
            }

            selectionArgs = new String[ selArgs.size() ];
            selArgs.toArray(selectionArgs);
        }

        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent intent = new Intent(this, MajorChoiceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(this, MajorChoiceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_fyp) {

            Intent intent = new Intent(this, fypView.class);
            startActivity(intent);

        } else if (id == R.id.nav_cadd) {

        } else if (id == R.id.nav_cip) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
