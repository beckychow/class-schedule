package com.example.ucsdschedulinghelper.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.ucsdschedulinghelper.database.CoursesCollectionContract.Course;
import com.example.ucsdschedulinghelper.database.DatabaseHelper;
import com.example.ucsdschedulinghelper.database.FourYearPlanContract.PlanEntry;

/**
 * Created by SKE on 6/11/15.
 */
public class DbContentProvider extends ContentProvider {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase db;

    private static final String AND = " AND ";
    private static final String EQUALS = " = ";
    private static final String UNKNOWN_URI = "Unknown URI: ";

    private static final int COURSES = 00;
    private static final int COURSES_ITEM_ID = 01;
    private static final int COURSES_RESET_UPDATED = 03;
    private static final int COURSES_DELETE_OLD = 04;

    private static final int PLAN = 10;
    private static final int PLAN_ITEM_ID = 11;
    private static final int PLAN_ALL = 12;
    private static final int PLAN_RESET_UPDATED = 13;
    private static final int PLAN_DELETE_OLD = 14;

    private static final int FALSE = 0;

    private static final String AUTHORITY = "com.example.ucsdschedulinghelper.provider";
    private static final String COURSES_BASE_PATH = "courses";
    private static final String PLAN_BASE_PATH = "plan";

    public static final Uri CONTENT_COURSES_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSES_BASE_PATH);
    public static final String CONTENT_COURSES_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/course";

    public static final Uri CONTENT_PLAN_URI = Uri.parse("content://" + AUTHORITY + "/" + PLAN_BASE_PATH);
    public static final String CONTENT_PLAN_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/plan";

    public static final String ALL_PATH_AUX = "all";
    public static final String RESET_UPDATED_PATH_AUX = "reset_updated";
    public static final String DELETE_OLD_PATH_AUX = "delete_old";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    /**
     * The calls to addURI() go here, for all of the content URI patterns that the provider
     * should recognize.
     */
    static {
        sUriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH, COURSES);
        sUriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH + "/#", COURSES_ITEM_ID);
        sUriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH + "/" + RESET_UPDATED_PATH_AUX, COURSES_RESET_UPDATED);
        sUriMatcher.addURI(AUTHORITY, COURSES_BASE_PATH + "/" + DELETE_OLD_PATH_AUX, COURSES_DELETE_OLD);
        sUriMatcher.addURI(AUTHORITY, PLAN_BASE_PATH, PLAN);
        sUriMatcher.addURI(AUTHORITY, PLAN_BASE_PATH + "/#", PLAN_ITEM_ID);
        sUriMatcher.addURI(AUTHORITY, PLAN_BASE_PATH + "/" + ALL_PATH_AUX, PLAN_ALL);
        sUriMatcher.addURI(AUTHORITY, PLAN_BASE_PATH + "/" + RESET_UPDATED_PATH_AUX, PLAN_RESET_UPDATED);
        sUriMatcher.addURI(AUTHORITY, PLAN_BASE_PATH + "/" + DELETE_OLD_PATH_AUX, PLAN_DELETE_OLD);
    }

    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    public Uri insert(Uri uri, ContentValues values) {
        boolean isCourse = false;
        long id = 0;
        db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case COURSES:
                values.put(Course.COLUMN_COMPLETED, 0);
                values.put(Course.COLUMN_IN_PROGRESS, 0);
                values.put(Course.COLUMN_SOI, 0);
                id = db.insert(Course.TABLE_NAME, null, values);
                isCourse = true;
                break;
            case PLAN:
                id = db.insert(PlanEntry.TABLE_NAME, null, values);
                isCourse = false;
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        if (isCourse)
            return Uri.parse(COURSES_BASE_PATH + "/" + id);
        else
            return Uri.parse(PLAN_BASE_PATH + "/" + id);
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
                String courseId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(Course.TABLE_NAME,
                            Course._ID + EQUALS + courseId, null);
                } else {
                    rowsDeleted = db.delete(Course.TABLE_NAME,
                            Course._ID + EQUALS + courseId + AND + selection, selectionArgs);
                }
                break;
            case COURSES_DELETE_OLD:
                db.delete(Course.TABLE_NAME, Course.COLUMN_IS_UPDATED + EQUALS + FALSE, null);
                break;

            case PLAN:
                rowsDeleted = db.delete(PlanEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case PLAN_ITEM_ID:
                String planEntryId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(PlanEntry.TABLE_NAME,
                            PlanEntry._ID + EQUALS + planEntryId, null);
                } else {
                    rowsDeleted = db.delete(PlanEntry.TABLE_NAME,
                            PlanEntry._ID + EQUALS + planEntryId + AND + selection, selectionArgs);
                }
                break;
            case PLAN_DELETE_OLD:
                db.delete(PlanEntry.TABLE_NAME, PlanEntry.COLUMN_IS_UPDATED + EQUALS + FALSE, null);
                break;

            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = mDbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        boolean requiresInsertion = false;
        switch (sUriMatcher.match(uri)) {
            case COURSES:
                values.put(Course.COLUMN_IS_UPDATED, true);
                rowsUpdated = db.update(Course.TABLE_NAME, values, selection, selectionArgs);
                requiresInsertion = true;
                break;

            case COURSES_ITEM_ID:
                String courseId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(Course.TABLE_NAME, values, Course._ID + EQUALS + courseId, null);
                } else {
                    rowsUpdated = db.update(Course.TABLE_NAME, values,
                            Course._ID + EQUALS + courseId + AND + selection, selectionArgs);
                }
                break;

            case COURSES_RESET_UPDATED:
                if (values != null)
                    values.clear();
                else
                    values = new ContentValues();
                values.put(Course.COLUMN_IS_UPDATED, false);
                db.update(Course.TABLE_NAME, values, null, null);
                break;

            case PLAN:
                values.put(PlanEntry.COLUMN_IS_UPDATED, true);
                rowsUpdated = db.update(PlanEntry.TABLE_NAME, values, selection, selectionArgs);
                requiresInsertion = true;
                break;

            case PLAN_ITEM_ID:
                String planEntryId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(PlanEntry.TABLE_NAME, values, PlanEntry._ID + EQUALS + planEntryId, null);
                } else {
                    rowsUpdated = db.update(PlanEntry.TABLE_NAME, values,
                            PlanEntry._ID + EQUALS + planEntryId + AND + selection, selectionArgs);
                }
                break;

            case PLAN_RESET_UPDATED:
                if (values != null)
                    values.clear();
                else
                    values = new ContentValues();
                values.put(PlanEntry.COLUMN_IS_UPDATED, false);
                db.update(PlanEntry.TABLE_NAME, values, null, null);
                break;

            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }
        // if such entry does not exist in the table
        if (rowsUpdated == 0 && requiresInsertion) {
            insert(uri, values);
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

            // If the incoming URI was for a single row of 'courses'
            case COURSES_ITEM_ID:
                /**
                 * Because this URI was for a single row, the _ID values part is
                 * present. Get the last path segment from the URI, this is the _ID value.
                 * Then, append the value to the WHERE clause for the query.
                 */
                selection = Course._ID + EQUALS + uri.getLastPathSegment();
                break;

            case PLAN:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;

            case PLAN_ITEM_ID:
                selection = PlanEntry._ID + EQUALS + uri.getLastPathSegment();
                break;

            case PLAN_ALL:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                projection = new String[] {PlanEntry._ID, PlanEntry.COLUMN_COURSE_NAME,
                        PlanEntry.COLUMN_YEAR_USER, PlanEntry.COLUMN_QUARTER_USER,
                        PlanEntry.COLUMN_COURSE_ID};
                break;

            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }
        db = mDbHelper.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    public String getType(Uri uri) { return null; }

    /* returns a cursor with all plan entries */
    /* NOT USED */
    public Cursor getAllPlanEntries() {
        String[] projection = { PlanEntry._ID, PlanEntry.COLUMN_COURSE_NAME,
                PlanEntry.COLUMN_YEAR_USER, PlanEntry.COLUMN_QUARTER_USER,
                PlanEntry.COLUMN_COURSE_ID };
        return query(CONTENT_PLAN_URI, projection, null, null, null);
    }

    /* resets user-configured plan to its default layout */
    /* TODO: make it usable with getContentResolver() */
    public void resetPlanToDefault() {
        Cursor cursor = getAllPlanEntries();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String planEntryId = cursor.getString(cursor.getColumnIndexOrThrow(PlanEntry._ID));
            Uri uri = Uri.withAppendedPath(CONTENT_PLAN_URI, planEntryId);

            int planEntryYearDefault = cursor.getInt(cursor.getColumnIndexOrThrow(
                    PlanEntry.COLUMN_YEAR_USER));
            int planEntryQuarterDefault = cursor.getInt(cursor.getColumnIndexOrThrow(
                    PlanEntry.COLUMN_QUARTER_DEFAULT));

            ContentValues values = new ContentValues();
            values.put(PlanEntry.COLUMN_YEAR_USER, planEntryYearDefault);
            values.put(PlanEntry.COLUMN_QUARTER_USER, planEntryQuarterDefault);
            update(uri, values, null, null);
            cursor.moveToNext();
        }
    }
}
