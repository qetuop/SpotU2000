package com.qetuop.spotu2000;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 10/11/15.
 */
public class ExerciseDbAdapter extends AbstractDbAdapter {
    private String[] projection = {
            COLUMN_ID,
            COLUMN_EXERCISE_NAME,
            COLUMN_EXERCISE_TYPE,
            COLUMN_EXERCISE_USER_ID
    };

    public ExerciseDbAdapter(Context ctx) {
        super(ctx);
    }

    public long createExercise(Exercise exercise) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_EXERCISE_NAME, exercise.getExerciseName());
        args.put(COLUMN_EXERCISE_TYPE, exercise.getExerciseType());
        args.put(COLUMN_EXERCISE_USER_ID, exercise.getUserId());

        return mDb.insert(TABLE_EXERCISE, null, args);
    }

    // convert a "ptr/cursor" of a table entry to an object
    public Exercise cursorToExercise(Cursor cursor) {
        Exercise exercise = new Exercise();

        exercise.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))); // why can't get?
        exercise.setExerciseName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE_NAME)));
        exercise.setExerciseType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE_TYPE)));
        exercise.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE_USER_ID)));

        return exercise;
    }

    // READ
    public Exercise getExercise(long id) {
        Exercise exercise = new Exercise();


        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = mDb.query(
                TABLE_EXERCISE,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();
        if ( cursor.moveToFirst() == true ) // not empty
            exercise = cursorToExercise(cursor);

        cursor.close();

        return exercise;
    }

    public Exercise getExercise(String name) {
        Exercise exercise = new Exercise();

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_EXERCISE_NAME + "=?";
        String[] selectionArgs = {name};

        Cursor cursor = mDb.query(
                TABLE_EXERCISE,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();
        if ( cursor.moveToFirst() == true ) // not empty
            exercise = cursorToExercise(cursor);

        cursor.close();

        return exercise;
    }


    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();

        Cursor cursor = mDb.query(
                TABLE_EXERCISE,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            exercises.add(exercise);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return exercises;
    }

    public Cursor getAllExercisesOfTypeCursor(String type) {
        //String sql =  "SELECT * FROM "+ TABLE_EXERCISE + " WHERE " + COLUMN_EXERCISE_TYPE + "  = " + type;

        //Cursor cursor = mDb.rawQuery(sql, null);

        // How you want the results sorted in the resulting Cursor
        String sortOrder = COLUMN_EXERCISE_NAME;
        String selection = COLUMN_EXERCISE_TYPE + "=?";
        String[] selectionArgs = {type};

        Cursor cursor = mDb.query(
                TABLE_EXERCISE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.moveToFirst();

        return cursor;
    }

    public List<Exercise> getAllExercisesByType(String type) {
        List<Exercise> exercises = new ArrayList<>();

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_EXERCISE_TYPE + "=?";
        String[] selectionArgs = {type};

        Cursor cursor = mDb.query(
                TABLE_EXERCISE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            exercises.add(exercise);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return exercises;
    }

    /*
distinct	true if you want each row to be unique, false otherwise.
table	The table name to compile the query against.
columns	A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
selection	A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
selectionArgs	You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
groupBy	A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
having	A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
orderBy	How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
limit	Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null denotes no LIMIT clause.
   */
    public List<String> getExerciseTypes() {
        List<String> exerciseTypes = new ArrayList<>();

        String[] projection = {
                COLUMN_EXERCISE_TYPE
        };
        Cursor cursor = mDb.query(
                true,
                TABLE_EXERCISE,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            exerciseTypes.add( cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE_TYPE)) );
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return exerciseTypes;
    }

    // UPDATE
    public void updateExercise(long id, Exercise exercise) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, exercise.getExerciseName());
        values.put(COLUMN_EXERCISE_TYPE, exercise.getExerciseType());
        values.put(COLUMN_EXERCISE_USER_ID, exercise.getUserId());

        // Which row to update, based on the ID
        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                TABLE_EXERCISE,
                values,
                selection,
                selectionArgs);
    }

    // DESTROY - name
    public void removeExercise(String exerciseName) {

        // Define 'where' part of query.
        String selection = COLUMN_EXERCISE_NAME + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = {exerciseName};

        // Issue SQL statement.
        mDb.delete(TABLE_EXERCISE, selection, selectionArgs);
    }

    // DESTROY - id
    public void removeExercise(long id) {

        // Define 'where' part of query.
        String selection = COLUMN_ID + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};

        // Issue SQL statement.
        mDb.delete(TABLE_EXERCISE, selection, selectionArgs);
    }

    public void removeAllExercises() {
        List<Exercise> exercises = getAllExercises();
        for ( Exercise ex : exercises ) {
            removeExercise(ex.getExerciseName());
        }
    }
}
