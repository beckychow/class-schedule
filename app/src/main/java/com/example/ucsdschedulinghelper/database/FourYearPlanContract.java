package com.example.ucsdschedulinghelper.database;

import android.provider.BaseColumns;

/**
 * Created by SKE on 18/11/15.
 */
public final class FourYearPlanContract {
    public FourYearPlanContract() {
    }

    public static abstract class PlanEntry implements BaseColumns {
        public static final String TABLE_NAME = "plan";
        public static final String COLUMN_COURSE_NAME = "course_name";
        public static final String COLUMN_YEAR_DEFAULT = "year_default";
        public static final String COLUMN_QUARTER_DEFAULT = "quarter_default";
        public static final String COLUMN_YEAR_USER = "year_user";
        public static final String COLUMN_QUARTER_USER = "quarter_user";
        public static final String COLUMN_COURSE_ID = "course_id";
    }
}
