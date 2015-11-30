package com.example.ucsdschedulinghelper.parser;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.database.FourYearPlanContract.PlanEntry;
import com.example.ucsdschedulinghelper.database.CoursesCollectionContract.Course;
import com.example.ucsdschedulinghelper.provider.DbContentProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huayin Zhou on 11/15/15.
 */
public class PlanParser extends MyHtmlParser {


    private ContentResolver contentResolver;

    public PlanParser(Activity activity, ContentResolver contentResolver, String url) {
        super(activity, url);
        this.contentResolver = contentResolver;
        Log.d("parse", url);
    }

    public void parseContent() { new ParseContentToDBTask().execute(fetchDataFromHttp); }

    private class ParseContentToDBTask extends ParseTask {

        @Override
        public void onPreExecute() {

        }

        @Override
        public Void doInBackground(FetchDataFromHttp... params) {
            ArrayList<String[]> data = new ArrayList<>();
            boolean success = true;
            try {
                JSONArray jsonArray = new JSONArray(fetchDataFromHttp.getResults());
                JSONObject plan = jsonArray.getJSONObject(0);
                JSONArray courseList = plan.getJSONArray("courses");
                for (int i_courseList = 0; i_courseList < courseList.length(); ++i_courseList) {
                    JSONArray yearList = courseList.getJSONArray(i_courseList);
                    for (int i_yearList = 0; i_yearList < yearList.length(); ++i_yearList) {
                        JSONArray quarterList = yearList.getJSONArray(i_yearList);
                        for (int i_quarterList = 0; i_quarterList < quarterList.length(); ++i_quarterList) {
                            // get one course info
                            JSONObject course = quarterList.getJSONObject(i_quarterList);
                            String course_name = course.getString("course_name");
                            String year_taken = course.getString("year_taken");
                            String quarter_taken = course.getString("quarter_taken");
                            // test
                            //changeText(course_name, year_taken, quarter_taken);
                            data.add(new String[] {course_name, year_taken, quarter_taken});
                            //Log.e("PARSE", course_name + " | " + year_taken + " | " + quarter_taken);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                success = false;
            }

            if (success) {
                // prepareDatabaseBeforeUpdate();
                deletePreviousEntriesFromDatabase();
                for (String[] planEntryData : data) {
                    updateDatabase(planEntryData[0], planEntryData[1], planEntryData[2]);
                }
                // deleteOldEntriesFromDatabase();
            }

            return null;
        }

    }

    private void changeText(String course_name, String year_taken, String quarter_taken) {
        final TextView textView = (TextView) myActivity.findViewById(R.id.message);
        textView.setMovementMethod(new ScrollingMovementMethod());
        String newString = textView.getText() + "\n" +
                " -- course_name: " + course_name + "\n" +
                " -- year_taken: " + year_taken + "\n" +
                " -- quarter_taken: " + quarter_taken + "\n" +
                "  <<-->>\n";
        textView.setText(newString);
    }

    /** Additions by SKE **/
    private void prepareDatabaseBeforeUpdate() {
        contentResolver.update(Uri.withAppendedPath(DbContentProvider.CONTENT_PLAN_URI,
                DbContentProvider.RESET_UPDATED_PATH_AUX), null, null, null);
    }

    private void deletePreviousEntriesFromDatabase() {
        contentResolver.delete(Uri.withAppendedPath(DbContentProvider.CONTENT_PLAN_URI,
                DbContentProvider.ALL_PATH_AUX), null, null);
    }

    private void updateDatabase(String courseName, String year, String quarter) {
        ContentValues values = new ContentValues();

        courseName = courseName.trim();
        values.put(PlanEntry.COLUMN_COURSE_NAME, courseName);
        values.put(PlanEntry.COLUMN_YEAR_DEFAULT, year);
        values.put(PlanEntry.COLUMN_YEAR_USER, year);
        values.put(PlanEntry.COLUMN_QUARTER_DEFAULT, quarter);
        values.put(PlanEntry.COLUMN_QUARTER_USER, quarter);
        values.put(PlanEntry.COLUMN_COURSE_ID, findCorrespondingCourseId(courseName));

        // String selection = PlanEntry.COLUMN_COURSE_NAME + " LIKE ?";
        // String[] selectionArgs = {courseName};

        // contentResolver.update(DbContentProvider.CONTENT_PLAN_URI, values,
        //        selection, selectionArgs);
        contentResolver.insert(DbContentProvider.CONTENT_PLAN_URI, values);
    }

    private void deleteOldEntriesFromDatabase() {
        contentResolver.delete(Uri.withAppendedPath(DbContentProvider.CONTENT_PLAN_URI,
                DbContentProvider.DELETE_OLD_PATH_AUX), null, null);
    }

    private int findCorrespondingCourseId(String courseName) {
        int nameLengthLimit = 9; // 4 for dept + 5 for code
        int courseId = 0;

        courseName = courseName.replaceAll("\\s", "");
        courseName = courseName.replaceAll("\\*", "");

        if (courseName.length() <= nameLengthLimit) {
            String[] projection = {Course._ID, Course.COLUMN_ENTRY_ID};
            String selection = Course.COLUMN_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {courseName};
            Cursor cursor = contentResolver.query(DbContentProvider.CONTENT_COURSES_URI,
                    projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst() && cursor.isLast()) {
                courseId = cursor.getInt(cursor.getColumnIndexOrThrow(Course._ID));
            }
            cursor.close();
        }
        return courseId;
    }
}
