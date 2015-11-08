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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionContract;
import com.example.ucsdschedulinghelper.provider.CoursesContentProvider;

/**
 * Created by SKE on 5/11/15.
 */
public class ListViewLoader extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;

    //There are the Courses rows that we will retrieve
    static final String[] PROJECTION = new String[] {
            CoursesCollectionContract.Course._ID,
            CoursesCollectionContract.Course.COLUMN_DEPARTMENT,
            CoursesCollectionContract.Course.COLUMN_CODE,
            CoursesCollectionContract.Course.COLUMN_TITLE };

    // This is the select criteria
    static final String SELECTION = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                R.id.list_text_department,
                R.id.list_text_code,
                R.id.list_text_title,
                R.id.list_text_id };

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this, R.layout.course_list_view, null,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, CoursesContentProvider.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
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
        TextView courseIDView = (TextView) v.findViewById(R.id.list_text_id);
        Uri courseUri = Uri.withAppendedPath(CoursesContentProvider.CONTENT_URI, courseIDView.getText().toString());

        //Log.e(getClass().getSimpleName() ,"Uri id = " + courseIDView.getText().toString());

        intent.putExtra(CoursesContentProvider.CONTENT_ITEM_TYPE, courseUri);

        //Log.e(getClass().getSimpleName(), "Uri = " + courseUri.toString());

        startActivity(intent);
    }


}
