package jp.yokomark.sample.gradle.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author keishin.yokomaku
 */
public class SampleDatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = SampleDatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "sample.db";
    private static final int VERSION = 1;

    public SampleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTable(db);
    }

    @SuppressLint("NewApi")
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        dropTable(db);
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL(SampleTableScheme.buildCreateStatement());
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL(SampleTableScheme.buildDropStatement());
    }
}
