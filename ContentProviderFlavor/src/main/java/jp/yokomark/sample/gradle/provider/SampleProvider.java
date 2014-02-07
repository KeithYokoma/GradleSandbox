package jp.yokomark.sample.gradle.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author keishin.yokomaku
 */
public class SampleProvider extends ContentProvider {
    public static final String AUTHORITY = BuildConfig.PROVIDER_SAMPLE_AUTHORITY;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final UriMatcher MATCHER;
    private static final String CONTENT_TYPE_SAMPLES = "vnd.android.cursor.dir/vnd.yokomark.flavor.sample";
    private static final String CONTENT_TYPE_SAMPLE = "vnd.android.cursor.item/vnd.yokomark.flavor.sample";
    private static final String PATH_SAMPLES = "samples";
    private static final String PATH_SAMPLE = PATH_SAMPLES + "/#";
    private static final int TYPE_SAMPLES = 1;
    private static final int TYPE_SAMPLE = 2;
    private SQLiteOpenHelper mDbHelper;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(AUTHORITY, PATH_SAMPLES, TYPE_SAMPLES);
        MATCHER.addURI(AUTHORITY, PATH_SAMPLE, TYPE_SAMPLE);
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new SampleDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = null;
        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String newSelection = buildSelection(uri, selection);
        builder.setTables(SampleTableScheme.TABLE_NAME);
        Cursor c = null;
        try {
            db = mDbHelper.getReadableDatabase();
            c = builder.query(db, projection, newSelection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } catch (RuntimeException e) {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
            throw e;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
        case TYPE_SAMPLE:
            return CONTENT_TYPE_SAMPLE;
        case TYPE_SAMPLES:
            return CONTENT_TYPE_SAMPLES;
        default:
            throw new IllegalArgumentException("unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (MATCHER.match(uri) != TYPE_SAMPLES) {
            throw new IllegalArgumentException("unknown uri: " + uri);
        }
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            long id = db.insert(SampleTableScheme.TABLE_NAME, null, values);
            Uri inserted = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(inserted, null);
            db.setTransactionSuccessful();
            return inserted;
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // not implemented
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // not implemented
        return 0;
    }

    @SuppressLint("NewApi")
    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }

    private String buildSelection(Uri uri, String selection) {
        long id = 0;
        String additionalSelection = null;

        switch (MATCHER.match(uri)) {
            case TYPE_SAMPLE:
                id = ContentUris.parseId(uri);
                additionalSelection = BaseColumns._ID + " = " + id;
                break;
            case TYPE_SAMPLES:
                // do nothing
                break;
            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }

        if (additionalSelection == null) {
            return selection;
        }
        if (selection == null) {
            return additionalSelection;
        }
        return additionalSelection + " AND " + selection;
    }
}
