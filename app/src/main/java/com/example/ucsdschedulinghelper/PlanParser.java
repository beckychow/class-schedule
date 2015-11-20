package com.example.ucsdschedulinghelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.database.FourYearPlanContract.PlanEntry;
import com.example.ucsdschedulinghelper.database.CoursesCollectionContract.Course;
import com.example.ucsdschedulinghelper.provider.DbContentProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Huayin Zhou on 11/15/15.
 */
public class PlanParser extends MyHtmlParser {


    private ContentResolver contentResolver;

    public PlanParser(Activity activity, ContentResolver contentResolver, String url) {
        super(activity, url);
        this.contentResolver = contentResolver;
    }

    void parseContentToDB() { new ParseContentToDBTask().execute(fetchDataFromHttp); }

    private class ParseContentToDBTask extends ParseTask {

        @Override
        public void onPreExecute() {

        }

        @Override
        public Void doInBackground(FetchDataFromHttp... params) {
            try {
                JSONArray jsonArray = new JSONArray(fetchDataFromHttp.getResults());
                JSONObject plan = jsonArray.getJSONObject(0);
                JSONArray courseList = plan.getJSONArray("courses");
                for (int i_courseList = 0; i_courseList < courseList.length(); ++i_courseList) {
                    JSONArray yearList = courseList.getJSONArray(i_courseList);
                    for (int i_yearList = 0; i_yearList < yearList.length(); ++i_yearList) {
                        JSONArray quarterList = yearList.getJSONArray(i_yearList);
                        for (int i_quarterList = 0; i_quarterList < yearList.length(); ++i_quarterList) {
                            // get one course info
                            JSONObject course = quarterList.getJSONObject(i_quarterList);
                            String course_name = course.getString("course_name");
                            String year_taken = course.getString("year_taken");
                            String quarter_taken = course.getString("quarter_taken");
                            // test
                            //changeText(course_name, year_taken, quarter_taken);
                            addToDatabase(course_name, year_taken, quarter_taken);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
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
    private void addToDatabase(String courseName, String year, String quarter) {
        ContentValues values = new ContentValues();

        courseName = courseName.trim();
        values.put(PlanEntry.COLUMN_COURSE_NAME, courseName);
        values.put(PlanEntry.COLUMN_YEAR_DEFAULT, year);
        values.put(PlanEntry.COLUMN_YEAR_USER, year);
        values.put(PlanEntry.COLUMN_QUARTER_DEFAULT, quarter);
        values.put(PlanEntry.COLUMN_QUARTER_USER, quarter);
        values.put(PlanEntry.COLUMN_COURSE_ID, findCorrespondingCourseId(courseName));

        String selection = PlanEntry.COLUMN_COURSE_NAME + " LIKE ?";
        String[] selectionArgs = {courseName};

        contentResolver.update(DbContentProvider.CONTENT_PLAN_URI, values,
                selection, selectionArgs);
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
