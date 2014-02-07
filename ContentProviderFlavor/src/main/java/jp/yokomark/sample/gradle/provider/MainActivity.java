package jp.yokomark.sample.gradle.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(SampleTableScheme.COLUMN_NAME, "hogehoge: " + Math.random() * 100);
                resolver.insert(SampleProvider.CONTENT_URI, values);
            }
        });
        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(new SampleCursorAdapter(this, null));

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, SampleProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ListView list = (ListView) findViewById(R.id.listview);
        CursorAdapter adapter = (CursorAdapter) list.getAdapter();
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ListView list = (ListView) findViewById(R.id.listview);
        CursorAdapter adapter = (CursorAdapter) list.getAdapter();
        adapter.swapCursor(null);
    }
}