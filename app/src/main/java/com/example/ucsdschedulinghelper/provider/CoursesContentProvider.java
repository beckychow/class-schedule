package com.example.ucsdschedulinghelper.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionContract.Course;
import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionDbHelper;

/**
 * Created by SKE on 6/11/15.
 */
public class CoursesContentProvider extends ContentProvider {

    private CoursesCollectionDbHelper mDbHelper;
    private SQLiteDatabase db;

    private static final int COURSES = 1;
    private static final int COURSES_ITEM_ID = 2;

    private static final String AUTHORITY = "com.example.ucsdschedulinghelper.provider";
    private static final String BASE_PATH = "courses";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/course";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    /**
     * The calls to addURI() go here, for all of the content URI patterns that the provider
     * should recognize.
     */
    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, COURSES);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COURSES_ITEM_ID);
    }

    public boolean onCreate() {
        mDbHelper = new CoursesCollectionDbHelper(getContext());
        return true;
    }

    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        db = mDbHelper.getWritableDatabase();
        values.put(Course.COLUMN_COMPLETED, 0);
        values.put(Course.COLUMN_IN_PROGRESS, 0);
        values.put(Course.COLUMN_SOI, 0);

        switch (sUriMatcher.match(uri)) {
            case COURSES:
                id = db.insert(Course.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (sUriMatcher.match(uri)) {
            case COURSES:
                rowsDeleted = db.delete(Course.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case COURSES_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(Course.TABLE_NAME,
                            Course._ID + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(Course.TABLE_NAME,
                            Course._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = mDbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (sUriMatcher.match(uri)) {
            case COURSES:
                rowsUpdated = db.update(Course.TABLE_NAME, values, selection, selectionArgs);
                break;
            case COURSES_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(Course.TABLE_NAME, values, Course._ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(Course.TABLE_NAME, values,
                            Course._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        // if such course does not exist in the table
        if (rowsUpdated == 0) {
            insert(uri, values);
            // Log.e("TESTING", "executed insertion");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }


    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Course.TABLE_NAME);
        /**
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI.
         */
        switch (sUriMatcher.match(uri)) {
            // If the incoming URI was for all of 'courses'
            case COURSES:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;

            // If the incoming URI was for a single row
            case COURSES_ITEM_ID:
                /**
                 * Because this URI was for a single row, the _ID values part is
                 * present. Get the last path segment from the URI, this is the _ID value.
                 * Then, append the value to the WHERE clause for the query.
                 */
                selection = Course._ID + " = " + uri.getLastPathSegment();
                break;

            default:
                // If the URI is not recognized (error handling)
                break;
        }
        db = mDbHelper.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    public String getType(Uri uri) { return null; }

}
