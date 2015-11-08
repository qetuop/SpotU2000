package com.qetuop.spotu2000;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;

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

        if ( mUser.getUserName() == null ) {
            mUser.setFirstName("Arnold");
            mUser.setLastName("Braunschweiger");
            mUser.setUserName("Brono");
            mUserId = mUserDbAdapter.createUser(mUser);
        }
        ////// --------------------------- //////
    }
}
