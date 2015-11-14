package com.qetuop.spotu2000;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    protected static final String LOG = "MainActivity";

    // Database accessors
    private ExerciseDbAdapter mExerciseDbAdapter;
    private UserDbAdapter mUserDbAdapter;
    private WorkoutDbAdapter mWorkoutDbAdapter;

    // how to store
    private long mUserId = 0;
    private User mUser = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseSetup();

        hardcodedSetup();

        setContentView(R.layout.activity_main);

        // have a way to select user (dworpdown which returns _id/user) - hardcode for now
        TextView userNameTv = (TextView) findViewById(R.id.activity_main_tv_username);
        userNameTv.setText(mUser.getUserName());

        // date - make this a more complex control, calendar, next/last wo, etc
        DateFormat df = DateFormat.getDateInstance();
        df.setTimeZone(TimeZone.getTimeZone("EST"));
        TextView dateTv = (TextView) findViewById(R.id.activity_main_tv_date);
        dateTv.setText(df.format(new Date(System.currentTimeMillis())));

        // load all Workouts for date
        update();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void databaseSetup() {
        mUserDbAdapter = new UserDbAdapter(this);
        try{
            mUserDbAdapter.open();
        }
        catch(SQLException e) {
            Log.e(LOG, "user table open error");
        }

        mExerciseDbAdapter = new ExerciseDbAdapter(this);
        try{
            mExerciseDbAdapter.open();
        }
        catch(SQLException e) {
            Log.e(LOG, "exercise table open error");
        }

        mWorkoutDbAdapter = new WorkoutDbAdapter(this);
        try{
            mWorkoutDbAdapter.open();
        }
        catch(SQLException e) {
            Log.e(LOG, "workout table open error");
        }
    }

    private void hardcodedSetup() {
        ////// Hardcode one and only user //////
        mUser = mUserDbAdapter.getUser("Brono");
        mUserId = mUser.getId();

        if ( mUser.getUserName() == null ) {
            mUser.setFirstName("Arnold");
            mUser.setLastName("Braunschweiger");
            mUser.setUserName("Brono");
            mUserId = mUserDbAdapter.createUser(mUser);
            mUser.setId(mUserId);
        }
        ////// --------------------------- //////

        ////// Hardcode some exercieses //////
        // Create Exercise
        Exercise ex1 = mExerciseDbAdapter.getExercise("Bench Press");
        long ex_id1 = 0;
        if ( ex1.getExerciseName() == null ) {
            ex1 = new Exercise("Bench Press", "Chest", mUserId);

            // Inserting in db
            ex_id1 = mExerciseDbAdapter.createExercise(ex1);
            ex1.setId(ex_id1);
            //long ex_id2 = mExerciseDbAdapter.createExercise(ex2);
            //ex2.setId(ex_id2);
        }
        else
            ex_id1 = ex1.getId();

        //Exercise ex2; = new Exercise("Tricep Push Down", "Tricep", mUserId);

        ////// --------------------------- //////

        ////// Hardcode some workouts //////
        mWorkoutDbAdapter.removeAllWorkouts();

        // Create Workout
        Workout w1 = new Workout(System.currentTimeMillis(), ex_id1);
        Workout w2 = new Workout(System.currentTimeMillis()-1000000000, ex_id1);
        Workout w3 = new Workout(System.currentTimeMillis(), ex_id1);
        System.out.println(w1.getExercise_id());
        //w1.setExercise_id(ex_id1);
        w1.setReps("10,8,6");
        w1.setSets(3);
        w1.setWeight("81,90,100");
        System.out.println(w1.getExercise_id());
        //w2.setExercise_id(ex_id1);
        w2.setReps("20,16,12");
        w2.setSets(3);
        w2.setWeight("40,45,50");

        /*w3.setExercise_id(ex_id1);
        w3.setReps("10,8,6");
        w3.setSets(3);
        w3.setWeight("80,90,100");*/

        long w_id1 = mWorkoutDbAdapter.createWorkout(w1);
        System.out.println(w1.getExercise_id());
        long w_id2 = mWorkoutDbAdapter.createWorkout(w2);
        long w_id3 = mWorkoutDbAdapter.createWorkout(w3);

        ////// --------------------------- //////


    }

    private void update() {
        // all
        List<Workout> workouts = mWorkoutDbAdapter.getAllWorkouts();
        System.out.println("---All Workouts---");
        for (  Workout wo : workouts ) {
            System.out.println(wo);
            DateFormat df2 = DateFormat.getDateTimeInstance();
            df2.setTimeZone(TimeZone.getTimeZone("EST"));
            System.out.println(df2.format(new Date(wo.getDate())));
        }
        System.out.println("-----------------");

        workouts = mWorkoutDbAdapter.getAllWorkoutsByDate(System.currentTimeMillis());
        System.out.println("---Todays Workouts---");
        for (  Workout wo : workouts ) {
            System.out.println(wo);
            DateFormat df2 = DateFormat.getDateTimeInstance();
            df2.setTimeZone(TimeZone.getTimeZone("EST"));
            System.out.println(df2.format(new Date(wo.getDate())));
        }
        System.out.println("-----------------");

        //ArrayList<Workout> workoutList;
        //mExerciseArrayList = (ArrayList) mExerciseDataSource.getAllExercises();
        //mExerciseAdapter = new ExerciseAdapter(this, mExerciseArrayList);

        final ListView listview = (ListView) findViewById(R.id.activity_main_lv_workouts);

        Cursor workoutCursor = mWorkoutDbAdapter.getAllWorkoutsCursorByDate();

        // Setup cursor adapter using cursor from last step
        WorkoutCursorAdapter workoutAdapter = new WorkoutCursorAdapter(this, workoutCursor,0);

        // Attach cursor adapter to the ListView
        listview.setAdapter(workoutAdapter);


    }

    public void newWorkout(View view) {
        System.out.println("NEW WORKOUT MAKE IT");
    }
}
