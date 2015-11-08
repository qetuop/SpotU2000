package com.qetuop.spotu2000;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 10/18/15.
 */
public class UserDbAdapter extends AbstractDbAdapter {
    private String[] projection = {
            COLUMN_ID,
            COLUMN_USER_FIRST_NAME,
            COLUMN_USER_LAST_NAME,
            COLUMN_USER_USER_NAME
    };

    public UserDbAdapter(Context ctx) {
        super(ctx);
    }

    // CREATE
    public long createUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_USER_LAST_NAME, user.getLastName());
        values.put(COLUMN_USER_USER_NAME, user.getUserName());

        return mDb.insert(TABLE_USER, null, values);
    }


    // convert a "ptr/cursor" of a table entry to an object
    private User cursorToUser(Cursor cursor) {
        User user = new User();

        user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))); // why can't get?
        user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FIRST_NAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_LAST_NAME)));
        user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_USER_NAME)));

        return user;
    }

   /* query()
    String dbName	The table name to compile the query against.
    String[] columnNames	A list of which table columns to return. Passing "null" will return all columns.
    String selection	Where-clause, i.e. filter for the selection of data, null will select all data.
        String selection =  PlayerEntry.COLUMN_NAME_PLAYER_NAME + "=?";
    String[] selectionArgs	You may include ?s in the "whereClause"". These placeholders will get replaced by the values from the selectionArgs array.
    String[] groupBy	A filter declaring how to group rows, null will cause the rows to not be grouped.
    String[] having	Filter for the groups, null means no filter.
    String[] orderBy	Table columns which will be used to order the data, null means no ordering.
    */

    // READ
    public User getUser(long id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.


        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = mDb.query(
                TABLE_USER,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();

        User user = cursorToUser(cursor);

        cursor.close();

        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = mDb.query(
                TABLE_USER,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return users;
    }



    // UPDATE
    public void updateUser(long id, User user) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_USER_LAST_NAME, user.getLastName());
        values.put(COLUMN_USER_USER_NAME, user.getUserName());

        // Which row to update, based on the ID
        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                TABLE_USER,
                values,
                selection,
                selectionArgs);
    }

    // DESTROY
    public void removeUser(String userName) {

        // Define 'where' part of query.
        String selection = COLUMN_USER_USER_NAME + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = {userName};

        // Issue SQL statement.
        mDb.delete(TABLE_USER, selection, selectionArgs);
    }

    public void removeAllUsers() {
        List<User> users = getAllUsers();
        for ( User u : users ) {
            removeUser(u.getUserName());
        }
    }
}
