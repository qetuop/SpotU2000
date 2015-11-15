package com.qetuop.spotu2000;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

class WeightRepPair {
    EditText weightEt;
    EditText repsEt;
}

public class WorkoutActivity extends AppCompatActivity {
    protected static final String LOG = "WorkoutActivity";

    private WorkoutDbAdapter mWorkoutDbAdapter;
    private ArrayList<WeightRepPair> weightRepPairList;
    private long workoutId = 0;
    private long exerciseId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mWorkoutDbAdapter = new WorkoutDbAdapter(this);
        try{
            mWorkoutDbAdapter.open();
        }
        catch(SQLException e) {
            Log.e(LOG, "workout table open error");
        }

        long workoutId = 0;
        weightRepPairList = new ArrayList<>();

        Intent intent = getIntent();
        if ( intent.hasExtra(MainActivity.EXTRA_MESSAGE) == true )
            workoutId = intent.getLongExtra(MainActivity.EXTRA_MESSAGE,0);

        // if workoutId == 0, popup selection dialog, else load it to be modified

        System.out.println("WorkoutActivity, workoutId=" + workoutId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout, menu);
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

    // called from button
    public void selectExercise(View view) {
        Intent intent = new Intent(this, ExerciseSelection.class);
        //intent.putExtra(EXTRA_MESSAGE, 0l);
        //startActivity(intent);

        int REQUEST_CODE = 0; // set it to ??? a code to identify which activity is returning?
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setExercise(Exercise exercise) {
        TextView textView = (TextView)findViewById(R.id.exerciseName);
        textView.setText(exercise.getExerciseName());
        exerciseId = exercise.getId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            if ( data.hasExtra("exercise") ) {
                Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
                System.out.println("I JUST GOT AN EXERCISE BACK " + exercise);
                setExercise(exercise);
            }
        }
    } // onActivityResult

    public void addSet(View view) {
        LinearLayout setLayout = (LinearLayout) findViewById(R.id.workoutSetLayout);
        setLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout newSetLayout = new LinearLayout(this);

        WeightRepPair weightRepPair = new WeightRepPair();

        weightRepPair.weightEt = new EditText(this);
        weightRepPair.repsEt = new EditText(this);

        weightRepPair.weightEt.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        weightRepPair.repsEt.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        weightRepPairList.add(weightRepPair);

        weightRepPair.weightEt.setHint("Enter Weight");
        weightRepPair.repsEt.setHint("Enter Reps");

        newSetLayout.addView(weightRepPair.weightEt);
        newSetLayout.addView(weightRepPair.repsEt);

        setLayout.addView(newSetLayout);
    }

    public void workoutDone(View view) {

        // create workout
        if ( workoutId == 0) {
            Workout workout = new Workout(System.currentTimeMillis(), exerciseId);

            StringBuilder weightBuilder = new StringBuilder();
            StringBuilder repBuilder = new StringBuilder();

            for ( WeightRepPair wpr : weightRepPairList ) {
                weightBuilder.append(wpr.weightEt.getText()).append(",");
                repBuilder.append(wpr.repsEt.getText()).append(",");
            }

            workout.setSets(weightRepPairList.size());
            workout.setWeight(weightBuilder.toString());
            workout.setReps(repBuilder.toString());

            mWorkoutDbAdapter.createWorkout(workout);
        }

        super.finish();
    }

}
