package com.qetuop.spotu2000;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by brian on 10/18/15.
 */
public class WorkoutDbAdapter extends AbstractDbAdapter {
    private String[] projection = {
            COLUMN_ID,
            COLUMN_WORKOUT_CREATION_DATE,
            COLUMN_WORKOUT_EXERCISE_ID,
            COLUMN_WORKOUT_SETS,
            COLUMN_WORKOUT_REPS,
            COLUMN_WORKOUT_WEIGHT
    };

    public WorkoutDbAdapter(Context ctx) {
        super(ctx);
    }

    public long createWorkout(Workout workout) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_WORKOUT_CREATION_DATE, workout.getDate());
        args.put(COLUMN_WORKOUT_EXERCISE_ID, workout.getExercise_id());
        args.put(COLUMN_WORKOUT_SETS, workout.getSets());
        args.put(COLUMN_WORKOUT_REPS, workout.getReps());
        args.put(COLUMN_WORKOUT_WEIGHT, workout.getWeight());

        return mDb.insert(TABLE_WORKOUT, null, args);
    }

    // convert a "ptr/cursor" of a table entry to an object
    private Workout cursorToWorkout(Cursor cursor) {
        Workout workout = new Workout();

        workout.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        workout.setDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_CREATION_DATE)));
        workout.setExercise_id(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_EXERCISE_ID)));
        workout.setSets(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_SETS)));
        workout.setReps(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_SETS)));
        workout.setWeight(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_WEIGHT)));

        return workout;
    }

    // READ
    public Workout getWorkout(long id) {

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = mDb.query(
                TABLE_WORKOUT,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();

        Workout workout = cursorToWorkout(cursor);

        cursor.close();

        return workout;
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();

        Cursor cursor = mDb.query(
                TABLE_WORKOUT,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workout workout = cursorToWorkout(cursor);
            workouts.add(workout);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return workouts;
    }

    public Cursor getAllWorkoutsCursor() {
         Cursor cursor = mDb.query(
                TABLE_WORKOUT,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();

        return cursor;
    }

    public Cursor getAllWorkoutsCursorByDate() {
        String sql =  "SELECT * FROM "+ TABLE_WORKOUT + " WHERE date(datetime(" + COLUMN_WORKOUT_CREATION_DATE + " / 1000 , 'unixepoch')) = date('now')";


        Cursor cursor = mDb.rawQuery(sql, null);

        return cursor;
    }

    // TODO - not using date passed in
    public List<Workout> getAllWorkoutsByDate(long date) {
        List<Workout> workouts = new ArrayList<>();
        String sql =  "SELECT * FROM "+ TABLE_WORKOUT + " WHERE date(datetime(" + COLUMN_WORKOUT_CREATION_DATE + " / 1000 , 'unixepoch')) = date('now')";
       // String sql =  "SELECT * FROM "+ TABLE_WORKOUT + " WHERE date(datetime(" + COLUMN_WORKOUT_CREATION_DATE + " / 1000 , 'unixepoch', 'localtime')) = datetime('" + date/1000 + "')";

        Cursor cursor = mDb.rawQuery(sql, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
                Workout workout = cursorToWorkout(cursor);
                workouts.add(workout);
                cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();


/*
        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_WORKOUT_CREATION_DATE + "=?";
        String[] selectionArgs = {String.valueOf(date/1000)};

        // SELECT datetime(1346142933585/1000, 'unixepoch', 'localtime');
        // SELECT * FROM TABLE_WORKOUT WHERE date(datetime(COLUMN_WORKOUT_CREATION_DATE / 1000 , 'unixepoch')) = date('now')

        Cursor cursor = mDb.query(
                TABLE_WORKOUT,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workout workout = cursorToWorkout(cursor);
            workouts.add(workout);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
*/
        return workouts;
    }

    // UPDATE
    public void updateWorkout(long id, Workout workout) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_WORKOUT_CREATION_DATE, workout.getDate());
        values.put(COLUMN_WORKOUT_EXERCISE_ID, workout.getExercise_id());
        values.put(COLUMN_WORKOUT_SETS, workout.getSets());
        values.put(COLUMN_WORKOUT_REPS, workout.getReps());
        values.put(COLUMN_WORKOUT_WEIGHT, workout.getWeight());


        // Which row to update, based on the ID
        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                TABLE_WORKOUT,
                values,
                selection,
                selectionArgs);
    }

    /*
    values.put(FeedEntry.COLUMN_NAME_TITLE, title);

// Which row to update, based on the ID
String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
String[] selectionArgs = { String.valueOf(rowId) };
*/

    // DESTROY - date or _id?
    public void removeWorkout(Long id) {

        // Define 'where' part of query.
        String selection = COLUMN_ID + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};

        // Issue SQL statement.
        mDb.delete(TABLE_WORKOUT, selection, selectionArgs);
    }

    public void removeAllWorkouts() {
        List<Workout> workouts = getAllWorkouts();
        for ( Workout w : workouts ) {
            removeWorkout(w.getId());
        }
    }


}
