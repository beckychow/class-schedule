package com.example.ucsdschedulinghelper.ui.courseListView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.database.CoursesCollectionContract;
import com.example.ucsdschedulinghelper.provider.DbContentProvider;
import com.example.ucsdschedulinghelper.ui.cape.CapeMainActivity;

/**
 * Created by SKE on 8/11/15.
 */
public class CourseDetailedView extends Activity {

    private Uri courseUri;
    private boolean completed;
    private boolean interested;
    private Button courseCompletionButton, courseInterestedButton, courseCapeButton;
    private SpannableString prerequisitesWithLinks;
    private String department, code;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detailed_view);

        courseCompletionButton = (Button) findViewById(R.id.course_detailed_button_completion);
        courseInterestedButton = (Button) findViewById(R.id.course_detailed_button_inprogress);

        courseCapeButton = (Button) findViewById(R.id.button_cape);
        courseCapeButton.setBackgroundResource(android.R.drawable.btn_default);
        courseUri = getIntent().getExtras().getParcelable(DbContentProvider.CONTENT_COURSES_ITEM_TYPE);
        fillData(courseUri);
        updateButtons();
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
                CoursesCollectionContract.Course.COLUMN_SOI,
                CoursesCollectionContract.Course.COLUMN_PREREQUISITES };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            department = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_DEPARTMENT));
            code = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_CODE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_DESCRIPTION));
            String units = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_UNITS));
            String prerequisites = cursor.getString(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_PREREQUISITES));

            prerequisitesWithLinks = new SpannableString(prerequisites);

            if (cursor.getInt(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_COMPLETED)) == 0)
                completed = false;
            else
                completed = true;

            if (cursor.getInt(cursor.getColumnIndexOrThrow(
                    CoursesCollectionContract.Course.COLUMN_SOI)) == 0)
                interested = false;
            else
                interested = true;

            TextView courseDepartment = (TextView) findViewById(R.id.course_detailed_text_department);
            TextView courseCode = (TextView) findViewById(R.id.course_detailed_text_code);
            TextView courseTitle = (TextView) findViewById(R.id.course_detailed_text_title);
            TextView courseDescription = (TextView) findViewById(R.id.course_detailed_text_description);
            TextView courseUnits = (TextView) findViewById(R.id.course_detailed_text_units);
            TextView coursePrerequisites = (TextView) findViewById(R.id.course_detailed_text_prerequisites);

            updateButtons();

            courseDepartment.append(department);
            courseCode.append(code);
            courseTitle.append(title);
            courseDescription.append(description);
            courseUnits.append(units);

            createLinksToPrerequisites(prerequisites);
            coursePrerequisites.append(prerequisitesWithLinks);
            coursePrerequisites.setMovementMethod(LinkMovementMethod.getInstance());

            cursor.close();
        }
    }

    public void createLinksToPrerequisites(String prerequisites) {
        int capitalLetterIndex = -1;
        int codeCharIndex = -1;
        int linkStart = 0;
        boolean foundDepartment = false;
        boolean foundCode = false;
        String possibleDepartment = null;
        String possibleCode = null;
        String[] projection = {
                CoursesCollectionContract.Course._ID,
                CoursesCollectionContract.Course.COLUMN_ENTRY_ID,
                CoursesCollectionContract.Course.COLUMN_DEPARTMENT,
                CoursesCollectionContract.Course.COLUMN_CODE,
                CoursesCollectionContract.Course.COLUMN_COMPLETED};

        for (int currentIndex = 0; currentIndex < prerequisites.length(); currentIndex++) {

            Character currentChar = prerequisites.charAt(currentIndex);
            // Log.e("-- CURRENT CHAR", currentChar.toString());
            if (Character.isLetter(currentChar)) {
                // look for the department unless it was found
                if (!foundDepartment) {
                    if (Character.isUpperCase(currentChar)) {
                        if (capitalLetterIndex < 0)
                            capitalLetterIndex = currentIndex;
                    } else {
                        /*
                         * department key should not exceed 4 letters (check)
                         * the word can be ignored, reset the index
                         */
                        if (currentIndex - capitalLetterIndex > 4) {
                            capitalLetterIndex = -1;
                        }
                    }
                }
                // department was found, looking for code now
                else {
                    // if not immediately followed by a digit, it was not a department
                    if (codeCharIndex < 0) {
                        foundDepartment = false;
                        capitalLetterIndex = -1;
                    } else {
                        if (currentIndex - codeCharIndex > 5) {
                            codeCharIndex = -1;
                        }
                    }
                }
            }
            else if (Character.isDigit(currentChar)) {
                // look for code only if a department was found
                if (foundDepartment) {
                    if (codeCharIndex < 0) {
                        codeCharIndex = currentIndex;
                    } else {
                        /*
                         * course code should not exceed 5 characters (check)
                         * the code can be ignored, reset the index
                         */
                        if (currentIndex - codeCharIndex > 5) {
                            codeCharIndex = -1;
                        }
                    }
                }
            }
            // if not a letter or a digit
            else {
                if (capitalLetterIndex >= 0) {
                    possibleDepartment = prerequisites.substring(capitalLetterIndex, currentIndex);
                    linkStart = capitalLetterIndex;
                    capitalLetterIndex = -1;
                    foundDepartment = true;
                } else if (codeCharIndex >= 0) {
                    possibleCode = prerequisites.substring(codeCharIndex, currentIndex);
                    codeCharIndex = -1;
                    foundCode = true;
                }
            }

            /* Log.e(" -- PREREQUISITE LINKS", "Department: " + possibleDepartment +
                    " , Code: " + possibleCode); */

            if (foundDepartment && foundCode) {
                String tag = "%";
                String selection =
                        CoursesCollectionContract.Course.COLUMN_DEPARTMENT + " LIKE ? AND " +
                        CoursesCollectionContract.Course.COLUMN_CODE + " LIKE ?" ;
                String space = " ";

                String[] selectionArgs = {tag + possibleDepartment + tag, possibleCode};

                Cursor cursor = getContentResolver().query(DbContentProvider.CONTENT_COURSES_URI,
                        projection, selection, selectionArgs, null);

                // if only 1 existing match was found
                if (cursor != null && cursor.moveToFirst() && cursor.isLast()) {
                    /* Log.e(" -- DB QUERY", cursor.getString(cursor.getColumnIndexOrThrow(
                            CoursesCollectionContract.Course.COLUMN_ENTRY_ID))); */

                    String courseId = cursor.getString(cursor.getColumnIndexOrThrow(
                            CoursesCollectionContract.Course._ID));
                    String courseTitle = department + space + code;
                    int completed = cursor.getInt(cursor.getColumnIndexOrThrow(
                            CoursesCollectionContract.Course.COLUMN_COMPLETED));

                    prerequisitesWithLinks.setSpan(new MyClickableSpan(courseId, courseTitle, completed),
                            linkStart, currentIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                cursor.close();

                /* (Log.e(" -- PREREQUISITE LINKS", "Department: " + possibleDepartment +
                        " , Code: " + possibleCode); */

                foundDepartment = foundCode = false;
            }
        }
    }

    public void courseSetCompleted(View v) {
        ContentValues values = new ContentValues();
        values.put(CoursesCollectionContract.Course.COLUMN_COMPLETED, !completed);
        if (!completed) {
            values.put(CoursesCollectionContract.Course.COLUMN_IN_PROGRESS, false);
            values.put(CoursesCollectionContract.Course.COLUMN_SOI, false);
            interested = false;
        }
        getContentResolver().update(courseUri, values, null, null);
        completed = !completed;
        updateButtons();
    }

    public void courseSetInterested(View v) {
        ContentValues values = new ContentValues();
        values.put(CoursesCollectionContract.Course.COLUMN_SOI, !interested);
        getContentResolver().update(courseUri, values, null, null);
        interested = !interested;
        updateButtons();
    }

    public void updateButtons() {
        String addToCompleted = "Add to completed";
        String removeFromCompleted = "Remove from completed";
        String addToInterested = "Add to interested";
        String removeFromInterested = "Remove from interested";

        if (!completed) {
            courseCompletionButton.setText(addToCompleted);
            courseCompletionButton.setBackgroundResource(android.R.drawable.btn_default);
            courseInterestedButton.setEnabled(true);
        }
        else {
            courseCompletionButton.setText(removeFromCompleted);
            courseInterestedButton.setEnabled(false);
            courseCompletionButton.setBackgroundColor(Color.parseColor("#32CD32"));
        }

        if (!interested) {
            courseInterestedButton.setText(addToInterested);
            courseInterestedButton.setBackgroundResource(android.R.drawable.btn_default);
        }
        else {
            courseInterestedButton.setText(removeFromInterested);
            courseInterestedButton.setBackgroundColor(Color.parseColor("#F0DD0E"));
        }
    }

    private Context getContext() {
        return this;
    }

    class MyClickableSpan extends ClickableSpan {
        private String id, str;
        private int status;

        public MyClickableSpan(String id, String str, int status) {
            this.id = id;
            this.str = str;
            this.status = status;
        }

        @Override
        public void onClick(View textView) {
            Intent intent = new Intent(getContext(), CourseDetailedView.class);
            Uri courseUri = Uri.withAppendedPath(DbContentProvider.CONTENT_COURSES_URI, id);
            intent.putExtra(DbContentProvider.CONTENT_COURSES_ITEM_TYPE, courseUri);
            startActivity(intent);
            finish();

            Toast.makeText(CourseDetailedView.this, "From " + str, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            if (status == 0)
                ds.setColor(Color.parseColor("#0099FF")); // set default text color
            else
                ds.setColor(Color.parseColor("#32CD32"));
            ds.setUnderlineText(false); // remove the underline
        }
    }

    // goto cape activity for cape button
    /** added by Huayin Zhou on 27/11/15 */
    public void showCape(View view) {
        Intent intent = new Intent(this, CapeMainActivity.class);
        intent.putExtra(CoursesCollectionContract.Course.COLUMN_DEPARTMENT, department);
        intent.putExtra(CoursesCollectionContract.Course.COLUMN_CODE, code);
        startActivity(intent);
    }
}
