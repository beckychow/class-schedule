package com.example.ucsdschedulinghelper.database.courses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ucsdschedulinghelper.database.courses.CoursesCollectionContract.Course;


/**
 * Created by SKE on 4/11/15.
 */
public class CoursesCollectionDbHelper extends SQLiteOpenHelper {
    public static final String TEXT_TYPE = " TEXT";
    public static final String INT_TYPE = " INTEGER";
    public static final String COMMA_SEP = ", ";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Course.TABLE_NAME + " (" +
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
            Course.COLUMN_PREREQUISITES + TEXT_TYPE +
            " )";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Course.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CoursesCollection.db";

    public CoursesCollectionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CoursesCollectionDbHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
