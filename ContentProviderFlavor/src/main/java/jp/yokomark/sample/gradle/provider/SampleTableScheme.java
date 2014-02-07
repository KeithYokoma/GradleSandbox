package jp.yokomark.sample.gradle.provider;

import android.provider.BaseColumns;

/**
 * @author keishin.yokomaku
 */
public class SampleTableScheme implements BaseColumns{
    public static final String TABLE_NAME = "sample_table";
    public static final String COLUMN_NAME = "name";

    public static String buildCreateStatement() {
        return "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL";
    }

    public static String buildDropStatement() {
        return "DROP TABLE " + TABLE_NAME;
    }
}
