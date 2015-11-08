package com.example.ucsdschedulinghelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.provider.CoursesContentProvider;

import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionDbHelper;
import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionContract.Course;

/**
 * Created by Huayin Zhou on 10/24/15.
 * To parse html url of course description.
 * example: "http://www.ucsd.edu/catalog/courses/CSE.html"
 */

//TODO: in parseContentToDB()

public class CourseDescriptionParser extends MyHtmlParser {

    private Context context;
    private CoursesCollectionDbHelper mDbHelper;
    private SQLiteDatabase db;
    private ContentResolver contentResolver;

    boolean once = true;

    public CourseDescriptionParser(Activity activity, Context context,
                                   ContentResolver contentResolver, String url) {
        super(activity, url);
        this.context = context;
        this.contentResolver = contentResolver;
        mDbHelper = new CoursesCollectionDbHelper(context);
        db = mDbHelper.getWritableDatabase();
    }

    void parseContentToDB() {

        // Tags for parsing
        final String DIVISION_TAG = "<h2 class=\"course-subhead-1\">";
        final String ID_TAG = "<a id=\"";
        final String NAME_TAG = "<p class=\"course-name\">";
        final String DESCRIPTION_TAG = "<p class=\"course-descriptions\">";

        // Lower division, Upper division, Graduate
        int index = content.indexOf(DIVISION_TAG);
        final String[] division = {"Lower", "Upper", "Graduate"};
        // int divi = 0;
        while (index != -1) {
            int next_idx = content.indexOf(DIVISION_TAG, index + DIVISION_TAG.length());
            // get description for each class in one division
            while (index < next_idx) {
                int tmp_idx;
                String id, fullCode, title, description, units;
                // int unit;

                // get course id
                index = content.indexOf(ID_TAG, index) + ID_TAG.length();
                tmp_idx = content.indexOf('"', index);
                id = content.substring(index, tmp_idx);

                // get course fullCode
                index = content.indexOf(NAME_TAG, index) + NAME_TAG.length();
                tmp_idx = content.indexOf('.', index);
                fullCode = content.substring(index, tmp_idx);
                fullCode = removeExtraSpace(fullCode);

                // get course title
                index = tmp_idx + 2;
                tmp_idx = content.indexOf('(', index);
                title = content.substring(index,tmp_idx - 1);
                title = removeExtraSpace(title);

                // get course units
                // TODO: resolve issue of varied unit in same course (using String now)
                index = tmp_idx + 1;
                tmp_idx = content.indexOf(')', index);
                units = content.substring(index, tmp_idx);
                /** unit = Integer.parseInt(content.substring(index, tmp_idx)); */

                // get course description
                index = content.indexOf(DESCRIPTION_TAG, index) + DESCRIPTION_TAG.length();
                tmp_idx = content.indexOf("<strong", index) - 1;
                description = content.substring(index, tmp_idx);
                description = removeExtraSpace(description);
                // TODO: get prerequisites
                // TODO: division category
                // test

                /** Additions by SKE **/
                int indexOfTheDivider = fullCode.indexOf(" ");
                String department = fullCode.substring(0, indexOfTheDivider);
                String code = fullCode.substring(indexOfTheDivider + 1);

                changeText(id, department, code, title, description, units);
                addToDatabase(id, department, code, title, description, units, false);
            }
            index = next_idx;
        }
    }

    private String removeExtraSpace(String s) {
        boolean space = false;
        char[] charArray = new char[s.length()];
        int len = 0;
        for (int i = 0; i < s.length(); ++i)
        {
            int tmp = s.charAt(i);
            if (s.charAt(i) <= ' ' ||
                    s.charAt(i) > '~')
            {
                if (space)
                    continue;
                space = true;
                charArray[len++] = ' ';
            } else
            {
                space = false;
                charArray[len++] = s.charAt(i);
            }
        }
        return String.valueOf(charArray, 0, len);
    }


    private void changeText(String id, String department, String code, String title,
                            String description, String units) {
        final TextView textView = (TextView) myActivity.findViewById(R.id.message);
        textView.setMovementMethod(new ScrollingMovementMethod());
        String newString = textView.getText() + "\n" +
                " --  ID: " + id + "\n" +
                " --DEPT: " + department + "\n" +
                " --CODE: " + code + "\n" +
                " --NAME: " + title + "\n" +
                " --DESC: " + description + "\n" +
                " --UNIT: " + units + "\n" +
                "  <<-->>\n";
        textView.setText(newString);
    }

    private void addToDatabase(String id, String department, String code, String title,
                               String description, String units, boolean update) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Course.COLUMN_ENTRY_ID, id);
        values.put(Course.COLUMN_DEPARTMENT, department);
        values.put(Course.COLUMN_CODE, code);
        values.put(Course.COLUMN_TITLE, title);
        values.put(Course.COLUMN_DESCRIPTION, description);
        values.put(Course.COLUMN_UNITS, units);

        // Insert the new row, returning the primary key value of the new row
        // long newRowId = db.insert(Course.TABLE_NAME, "null", values);
        /*if (once) {
            long newRowId = db.insert(Course.TABLE_NAME, "null", values);
            once = false;
        }*/
        String selection = "entryid LIKE ?";
        String[] selectionArgs = { id };
        contentResolver.update(CoursesContentProvider.CONTENT_URI, values,
                selection, selectionArgs);

    }
}
