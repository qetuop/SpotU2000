package com.qetuop.spotu2000;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseSelection extends AppCompatActivity {
    protected static final String LOG = "ExerciseSelection";
    private ExerciseDbAdapter mExerciseDbAdapter;
    private String mExerciseType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection);

        mExerciseDbAdapter = new ExerciseDbAdapter(this);
        try{
            mExerciseDbAdapter.open();
        }
        catch(SQLException e) {
            Log.e(LOG, "exercise table open error");
        }

        List<String> typeNames = mExerciseDbAdapter.getExerciseTypes();

        final CharSequence[] tn = typeNames.toArray(new CharSequence[typeNames.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Exercise Type");

        builder.setItems(tn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                setType(String.valueOf(tn[item]));

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setType(String type) {
        mExerciseType = type;

        final ListView listView = (ListView) findViewById(R.id.activity_exercise_selection_lv);
        Cursor exerciseCursor = mExerciseDbAdapter.getAllExercisesOfTypeCursor(type);
        ExerciseCursorAdapter exerciseAdapter = new ExerciseCursorAdapter(this, exerciseCursor, 0);
        listView.setAdapter(exerciseAdapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                exerciseSelected(cursor);
            }
        });
    }


    public void exerciseSelected(Cursor cursor){
        String name = cursor.getString(cursor.getColumnIndexOrThrow(AbstractDbAdapter.COLUMN_EXERCISE_NAME));
        long _id = cursor.getLong(cursor.getColumnIndexOrThrow(AbstractDbAdapter.COLUMN_ID));
        System.out.println("I JUST SELECTED=" + name + ", id= " + _id);

        // return to other activity
        Intent intent = new Intent(this,WorkoutActivity.class);
        Exercise exercise = mExerciseDbAdapter.cursorToExercise(cursor);
        intent.putExtra("exercise", exercise);
        // Activity finished ok, return the data
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
