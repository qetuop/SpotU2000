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
 * Created by brian on 11/12/15.
 */
public class WorkoutCursorAdapter  extends CursorAdapter {
    protected static final String LOG = "WorkoutCursorAdapter";
    private ExerciseDbAdapter mExerciseDbAdapter;

    public WorkoutCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);

        mExerciseDbAdapter = new ExerciseDbAdapter(context);;
        try{
            mExerciseDbAdapter.open();
        }
        catch(SQLException e) {
            Log.e(LOG, "exercise table open error");
        }
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_workout, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView exerciseTv = (TextView) view.findViewById(R.id.row_workout_exercise_tv);
        TextView weightTv = (TextView) view.findViewById(R.id.row_workout_weight_tv);
        TextView repsTv = (TextView) view.findViewById(R.id.row_workout_reps_tv);

        // Extract properties from cursor
        long exerciseId = cursor.getLong(cursor.getColumnIndexOrThrow(AbstractDbAdapter.COLUMN_WORKOUT_EXERCISE_ID));
        String weight = cursor.getString(cursor.getColumnIndexOrThrow(AbstractDbAdapter.COLUMN_WORKOUT_WEIGHT));
        //int set = cursor.getInt((cursor.getInt(cursor.getColumnIndexOrThrow(AbstractDbAdapter.COLUMN_WORKOUT_SETS))));
        String reps = cursor.getString(cursor.getColumnIndexOrThrow(AbstractDbAdapter.COLUMN_WORKOUT_REPS));

        Exercise exercise = mExerciseDbAdapter.getExercise(exerciseId);

        // Populate fields with extracted properties
        exerciseTv.setText(String.valueOf(exercise.getExerciseName()));
        weightTv.setText(String.valueOf(weight));
        //setTv.setText(String.valueOf(set));
        repsTv.setText(reps);
    }
}
