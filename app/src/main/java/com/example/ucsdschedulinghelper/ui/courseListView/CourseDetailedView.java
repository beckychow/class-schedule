package com.example.ucsdschedulinghelper.ui.courseListView;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionContract;
import com.example.ucsdschedulinghelper.provider.CoursesContentProvider;

/**
 * Created by Sp0t on 8/11/15.
 */
public class CourseDetailedView extends Activity {

    private Uri courseUri;
    private boolean completed;
    private boolean inProgress;
    private Button courseCompletionButton, courseInProgressButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detailed_view);

        courseCompletionButton = (Button) findViewById(R.id.course_detailed_button_completion);
        courseInProgressButton = (Button) findViewById(R.id.course_detailed_button_inprogress);
        courseUri = getIntent().getExtras().getParcelable(CoursesContentProvider.CONTENT_ITEM_TYPE);
        fillData(courseUri);
    }

    private void fillData(Uri uri) {
        String[] projection = new String[]{
                CoursesCollectionContract.Course._ID,
                CoursesCollectionContract.Course.COLUMN_DEPARTMENT,
                CoursesCollectionContract.Course.COLUMN_CODE,
                CoursesCollectionContract.Course.COLUMN_TITLE,
                CoursesCollectionContract.Course.COLUMN_DESCRIPTION,
                CoursesCollectionContract.Course.COLUMN_UNITS,
                CoursesCollectionContract.Course.COLUMN_COMPLETED,
                CoursesCollectionContract.Course.COLUMN_IN_PROGRESS};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            String department = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_DEPARTMENT));
            String code = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_CODE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_DESCRIPTION));
            String units = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_UNITS));

            if (cursor.getInt(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_COMPLETED)) == 0)
                completed = false;
            else
                completed = true;

            if (cursor.getInt(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_IN_PROGRESS)) == 0)
                inProgress = false;
            else
                inProgress = true;

            TextView courseDepartment = (TextView) findViewById(R.id.course_detailed_text_department);
            TextView courseCode = (TextView) findViewById(R.id.course_detailed_text_code);
            TextView courseTitle = (TextView) findViewById(R.id.course_detailed_text_title);
            TextView courseDescription = (TextView) findViewById(R.id.course_detailed_text_description);
            TextView courseUnits = (TextView) findViewById(R.id.course_detailed_text_units);

            if (completed)
                courseCompletionButton.setText("Remove from completed");
            if (inProgress)
                courseInProgressButton.setText("Remove from in progress");

            courseDepartment.append(department);
            courseCode.append(code);
            courseTitle.append(title);
            courseDescription.append(description);
            courseUnits.append(units);

            cursor.close();
        }
    }

    public void courseSetCompleted(View v) {
        ContentValues values = new ContentValues();
        values.put(CoursesCollectionContract.Course.COLUMN_COMPLETED, !completed);
        if (!completed) {
            values.put(CoursesCollectionContract.Course.COLUMN_IN_PROGRESS, false);
            inProgress = false;
        }
        getContentResolver().update(courseUri, values, null, null);
        completed = !completed;
        updateButtons();
    }

    public void courseSetInProgress(View v) {
        ContentValues values = new ContentValues();
        values.put(CoursesCollectionContract.Course.COLUMN_IN_PROGRESS, !inProgress);
        getContentResolver().update(courseUri, values, null, null);
        inProgress = !inProgress;
        updateButtons();
    }

    public void updateButtons() {
        if (!completed) {
            courseCompletionButton.setText("Add to completed");
            courseInProgressButton.setEnabled(true);
        }
        else {
            courseCompletionButton.setText("Remove from completed");
            courseInProgressButton.setEnabled(false);
        }
        if (!inProgress)
            courseInProgressButton.setText("Set in progress");
        else
            courseInProgressButton.setText("Remove from in progress");
    }
}
