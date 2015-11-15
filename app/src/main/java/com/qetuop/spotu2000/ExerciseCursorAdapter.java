package com.qetuop.spotu2000;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.sql.SQLException;

/**
 * Created by brian on 11/14/15.
 */
public class ExerciseCursorAdapter extends CursorAdapter {
    protected static final String LOG = "ExerciseCursorAdapter";

    public ExerciseCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_exercise, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView exerciseNameTv = (TextView) view.findViewById(R.id.row_exercise_name_tv);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(AbstractDbAdapter.COLUMN_EXERCISE_NAME));

        // Populate fields with extracted properties
        exerciseNameTv.setText(name);
    }
}
