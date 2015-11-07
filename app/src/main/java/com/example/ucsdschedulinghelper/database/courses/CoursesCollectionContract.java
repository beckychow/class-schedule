package com.example.ucsdschedulinghelper.database.courses;

import android.provider.BaseColumns;

/**
 * Created by SKE on 4/11/15.
 */

public final class CoursesCollectionContract {
    // empty constructor
    public CoursesCollectionContract() {}

    public static abstract class Course implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_ENTRY_ID = "entryid";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_UNITS = "units";
    }
}