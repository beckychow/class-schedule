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
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_QUARTER = "quarter";
        public static final String COLUMN_COURSE_ID = "course_id";
    }
}
