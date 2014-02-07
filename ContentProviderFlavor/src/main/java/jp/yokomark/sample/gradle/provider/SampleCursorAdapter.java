package jp.yokomark.sample.gradle.provider;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author keishin.yokomaku
 */
public class SampleCursorAdapter extends CursorAdapter {

    public SampleCursorAdapter(Context context, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(android.R.layout.simple_list_item_1, null, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text = (TextView) view.findViewById(android.R.id.text1);
        text.setText(cursor.getString(cursor.getColumnIndex(SampleTableScheme.COLUMN_NAME)));
    }
}