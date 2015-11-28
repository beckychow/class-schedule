package com.example.ucsdschedulinghelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ucsdschedulinghelper.database.CoursesCollectionContract.Course;
import com.example.ucsdschedulinghelper.database.FourYearPlanContract.PlanEntry;

/**
 * Created by SKE on 4/11/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TEXT_TYPE = " TEXT";
    public static final String INT_TYPE = " INTEGER";
    public static final String COMMA_SEP = ", ";
    public static final String SQL_COURSE_CREATE_ENTRIES = "CREATE TABLE " + Course.TABLE_NAME + " (" +
            Course._ID + " INTEGER PRIMARY KEY, " +
            Course.COLUMN_ENTRY_ID + INT_TYPE + COMMA_SEP +
            Course.COLUMN_DEPARTMENT + TEXT_TYPE + COMMA_SEP +
            Course.COLUMN_CODE + TEXT_TYPE + COMMA_SEP +
            Course.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
            Course.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            Course.COLUMN_UNITS + TEXT_TYPE + COMMA_SEP +
            Course.COLUMN_COMPLETED + INT_TYPE + COMMA_SEP +
            Course.COLUMN_IN_PROGRESS + INT_TYPE + COMMA_SEP +
            Course.COLUMN_SOI + INT_TYPE + COMMA_SEP +
            Course.COLUMN_PREREQUISITES + TEXT_TYPE + COMMA_SEP +
            Course.COLUMN_IS_UPDATED + INT_TYPE +
            " )";
    private static final String SQL_PLAN_CREATE_ENTRIES = "CREATE TABLE " + PlanEntry.TABLE_NAME + " (" +
            PlanEntry._ID + " INTEGER PRIMARY KEY, " +
            PlanEntry.COLUMN_COURSE_NAME + TEXT_TYPE + COMMA_SEP +
            PlanEntry.COLUMN_YEAR_DEFAULT + INT_TYPE + COMMA_SEP +
            PlanEntry.COLUMN_QUARTER_DEFAULT + INT_TYPE + COMMA_SEP +
            PlanEntry.COLUMN_YEAR_USER + INT_TYPE + COMMA_SEP +
            PlanEntry.COLUMN_QUARTER_USER + INT_TYPE + COMMA_SEP +
            PlanEntry.COLUMN_COURSE_ID + INT_TYPE + COMMA_SEP +
            Course.COLUMN_IS_UPDATED + INT_TYPE +
            " )";

    public static final String SQL_COURSE_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Course.TABLE_NAME;
    public static final String SQL_PLAN_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PlanEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "CoursesCollectionAndPlan.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_COURSE_CREATE_ENTRIES);
        db.execSQL(SQL_PLAN_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL(SQL_COURSE_DELETE_ENTRIES);
        db.execSQL(SQL_PLAN_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
