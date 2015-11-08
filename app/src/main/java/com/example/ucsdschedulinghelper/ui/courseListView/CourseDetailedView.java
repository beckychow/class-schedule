package com.example.ucsdschedulinghelper.ui.courseListView;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionContract;
import com.example.ucsdschedulinghelper.provider.CoursesContentProvider;

/**
 * Created by Sp0t on 8/11/15.
 */
public class CourseDetailedView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detailed_view);

        Uri courseUri = getIntent().getExtras().getParcelable(CoursesContentProvider.CONTENT_ITEM_TYPE);
        fillData(courseUri);
    }

    private void fillData(Uri uri) {
        String[] projection  = new String[] {
                CoursesCollectionContract.Course._ID,
                CoursesCollectionContract.Course.COLUMN_DEPARTMENT,
                CoursesCollectionContract.Course.COLUMN_CODE,
                CoursesCollectionContract.Course.COLUMN_TITLE,
                CoursesCollectionContract.Course.COLUMN_DESCRIPTION,
                CoursesCollectionContract.Course.COLUMN_UNITS };
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

            TextView courseDepartment = (TextView) findViewById(R.id.single_course_text_department);
            TextView courseCode = (TextView) findViewById(R.id.single_course_text_code);
            TextView courseTitle = (TextView) findViewById(R.id.single_course_text_title);
            TextView courseDescription = (TextView) findViewById(R.id.single_course_text_description);
            TextView courseUnits = (TextView) findViewById(R.id.single_course_text_units);

            courseDepartment.append(department);
            courseCode.append(code);
            courseTitle.append(title);
            courseDescription.append(description);
            courseUnits.append(units);

            cursor.close();
        }
    }
}
