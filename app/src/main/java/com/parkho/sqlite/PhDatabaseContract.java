package com.parkho.sqlite;

import android.provider.BaseColumns;

public final class PhDatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PhDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "student";
        public static final String GRADE = "column_grade";
        public static final String NUMBER = "column_number";
        public static final String NAME = "column_name";
    }

}