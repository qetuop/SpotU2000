package com.qetuop.spotu2000;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by brian on 10/11/15.
 */
public class AbstractDbAdapter {

    protected static final String LOG = "AbstractDbAdapter";

    public DatabaseHelper mDatabaseHelper; // not thread safe?
    //protected static DatabaseHelper mDatabaseHelper; // more thread safe?
    protected SQLiteDatabase mDb;

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "spotU2000.db";

    // Table Names
    public static final String TABLE_EXERCISE = "exercise";
    public static final String TABLE_USER = "user";
    public static final String TABLE_WORKOUT = "workout";

    // Common
    public static final String COLUMN_ID = "_id"; // use BaseColumns._ID or create class that implements it? Contract class?

    // Exercise Table
    public static final String COLUMN_EXERCISE_NAME = "name";
    public static final String COLUMN_EXERCISE_TYPE = "type";
    public static final String COLUMN_EXERCISE_USER_ID = "user_id";

    // User TABLE
    public static final String COLUMN_USER_FIRST_NAME = "first_name";
    public static final String COLUMN_USER_LAST_NAME = "last_name";
    public static final String COLUMN_USER_USER_NAME = "user_name";

    // Workout TABLE
    public static final String COLUMN_WORKOUT_CREATION_DATE = "creation_date";
    public static final String COLUMN_WORKOUT_EXERCISE_ID = "exercise_id";
    public static final String COLUMN_WORKOUT_SETS = "sets";
    public static final String COLUMN_WORKOUT_REPS = "reps";
    public static final String COLUMN_WORKOUT_WEIGHT = "weight";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // Table Create Statements

    //  User table create statement - create first since Exercise references it?
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER+ "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER_FIRST_NAME + " text, "
            + COLUMN_USER_LAST_NAME + " text, "
            + COLUMN_USER_USER_NAME + " text not null "
            + ")";  // no trailing ';'


    // Exercise table create statement
    private static final String CREATE_TABLE_EXERCISE = "CREATE TABLE "
            + TABLE_EXERCISE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_EXERCISE_NAME + " text not null, "
            + COLUMN_EXERCISE_USER_ID + " integer references " + TABLE_USER + ", "
            + COLUMN_EXERCISE_TYPE + " text not null "
            + ")"; // no trailing ';'

    // Workout table create statement
    private static final String CREATE_TABLE_WORKOUT = "CREATE TABLE "
            + TABLE_WORKOUT + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_WORKOUT_CREATION_DATE + " integer not null, "
            + COLUMN_WORKOUT_EXERCISE_ID + " integer references " + TABLE_EXERCISE + ", "
            + COLUMN_WORKOUT_SETS + " integer, "
            + COLUMN_WORKOUT_REPS + " text, "
            + COLUMN_WORKOUT_WEIGHT + " text "
            + ")"; // no trailing ';'


    protected final Context mCtx;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            Log.d(LOG, "ctor");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG, "onCreate");

            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_EXERCISE);
            db.execSQL(CREATE_TABLE_WORKOUT);

            // requires API 16
//            db.setForeignKeyConstraintsEnabled(true); // ?finish transactions ??
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(this.getClass().getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");

            // create backup first?

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);

            onCreate(db);
        }
    }

    public AbstractDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public AbstractDbAdapter open() throws SQLException {
        Log.d(LOG, "open");

        mDatabaseHelper = new DatabaseHelper(mCtx);
        mDb = mDatabaseHelper.getWritableDatabase();

        return this;
    }

/*    concurrent issue?
public SQLiteDatabase open() throws SQLException {
        Log.d(LOG, "open");

        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(mCtx);
        }

        return mDatabaseHelper.getWritableDatabase();
    }*/



    public void close() {
        mDatabaseHelper.close();
    }

}
