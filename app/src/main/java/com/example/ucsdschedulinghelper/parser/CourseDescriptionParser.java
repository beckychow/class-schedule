package com.example.ucsdschedulinghelper.parser;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.example.ucsdschedulinghelper.provider.DbContentProvider;

import com.example.ucsdschedulinghelper.database.CoursesCollectionContract.Course;

/**
 * Created by Huayin Zhou on 10/24/15.
 * To parse html url of course description.
 * example: "http://www.ucsd.edu/catalog/courses/CSE.html"
 */

//TODO: in parseContentToDB()

public class CourseDescriptionParser extends MyHtmlParser {

    private ContentResolver contentResolver;

    public CourseDescriptionParser(Activity activity, ContentResolver contentResolver, String url) {
        super(activity, url);
        this.contentResolver = contentResolver;
    }

    public void parseContent() {
        new ParseContentToDBTask().execute(fetchDataFromHttp);

        // test
        /*final TextView textView = (TextView) myActivity.findViewById(R.id.message);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(newString);*/

    }

    private class ParseContentToDBTask extends ParseTask {

        @Override
        public void onPreExecute() {

        }

        @Override
        public Void doInBackground(FetchDataFromHttp... params) {

            // get text format of the html file
            String content = params[0].getResults();

            // Tags for parsing
            final String DIVISION_TAG = "<h2 class=\"course-subhead-1\">";
            final String ID_TAG = "id=\"";
            final String NAME_TAG = "<p class=\"course-name\">";
            final String DESCRIPTION_TAG = "<p class=\"course-descriptions\">";
            final String PREREQUISITES_TAG = "</strong>";

            // Lower division, Upper division, Graduate
            int index = content.indexOf(DIVISION_TAG);
            final String[] division = {"Lower", "Upper", "Graduate"};
            // int divi = 0;
            prepareDatabaseBeforeUpdate();
            while (index != -1) {
                int next_idx = content.indexOf(DIVISION_TAG, index + DIVISION_TAG.length());
                // get description for each class in one division
                while (index < next_idx) {
                    int tmp_idx;
                    String id, fullCode, title, description, units, prerequisites;

                    // get course id
                    index = content.indexOf(ID_TAG, index) + ID_TAG.length();
                    tmp_idx = content.indexOf('"', index);
                    id = content.substring(index, tmp_idx);

                    // get course fullCode
                    index = content.indexOf(NAME_TAG, index) + NAME_TAG.length();
                    tmp_idx = content.indexOf('.', index);
                    fullCode = content.substring(index, tmp_idx);
                    fullCode = removeExtraSpaces(fullCode);

                    // get course title
                    index = tmp_idx + 2;
                    tmp_idx = content.indexOf('(', index);
                    title = content.substring(index, tmp_idx - 1);
                    title = removeExtraSpaces(title);

                    // get course units
                    index = tmp_idx + 1;
                    tmp_idx = content.indexOf(')', index);
                    units = content.substring(index, tmp_idx);

                    // get course description
                    index = content.indexOf(DESCRIPTION_TAG, index) + DESCRIPTION_TAG.length();
                    tmp_idx = content.indexOf("<strong", index) - 1;
                    description = content.substring(index, tmp_idx);
                    description = removeInsideTags(description);
                    description = removeExtraSpaces(description);

                    // get prerequisites
                    index = content.indexOf(PREREQUISITES_TAG, index) + PREREQUISITES_TAG.length();
                    tmp_idx = content.indexOf("</p>", index);
                    prerequisites = removeExtraSpaces(content.substring(index, tmp_idx));

                    // TODO: division category

                    /** Additions by SKE **/
                    int indexOfTheDivider = fullCode.indexOf(" ");
                    String department = fullCode.substring(0, indexOfTheDivider);
                    String code = fullCode.substring(indexOfTheDivider + 1);

                    //changeText(id, department, code, title, description, units);
                    updateDatabase(id, department, code, title, description, units, prerequisites);
                }
                index = next_idx;
            }
            deleteOldEntriesFromDatabase();
            return null;
        }

        private void prepareDatabaseBeforeUpdate() {
            contentResolver.update(Uri.withAppendedPath(DbContentProvider.CONTENT_COURSES_URI,
                    DbContentProvider.RESET_UPDATED_PATH_AUX), null, null, null);
        }

        private void updateDatabase(String id, String department, String code, String title,
                                   String description, String units, String prerequisites) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Course.COLUMN_ENTRY_ID, id);
            values.put(Course.COLUMN_DEPARTMENT, department);
            values.put(Course.COLUMN_CODE, code);
            values.put(Course.COLUMN_TITLE, title);
            values.put(Course.COLUMN_DESCRIPTION, description);
            values.put(Course.COLUMN_UNITS, units);
            values.put(Course.COLUMN_PREREQUISITES, prerequisites);

            String selection = Course.COLUMN_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {id};
            contentResolver.update(DbContentProvider.CONTENT_COURSES_URI, values,
                    selection, selectionArgs);
        }

        private void deleteOldEntriesFromDatabase() {
            contentResolver.delete(Uri.withAppendedPath(DbContentProvider.CONTENT_COURSES_URI,
                    DbContentProvider.DELETE_OLD_PATH_AUX), null, null);
        }

        private void changeText(String id, String department, String code, String title,
                                String description, String units) {
            String newString = //newString + "\n" +
                    " --  ID: " + id + "\n" +
                    " --DEPT: " + department + "\n" +
                    " --CODE: " + code + "\n" +
                    " --NAME: " + title + "\n" +
                    " --DESC: " + description + "\n" +
                    " --UNIT: " + units + "\n" +
                    "  <<-->>\n";
            Log.d("CourseDescriptionParser", newString);
        }

    }


}
