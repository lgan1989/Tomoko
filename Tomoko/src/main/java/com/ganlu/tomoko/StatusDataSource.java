package com.ganlu.tomoko;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
/**
 * Created by lgan on 7/22/13.
 */
public class StatusDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_CHARACTER,
            MySQLiteHelper.COLUMN_ROME,
            MySQLiteHelper.COLUMN_RIGHT,
            MySQLiteHelper.COLUMN_WRONG
    };

    public StatusDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void newRecord(String cha , String rome){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CHARACTER, cha);
        values.put(MySQLiteHelper.COLUMN_ROME , rome);
        values.put(MySQLiteHelper.COLUMN_WRONG , 0);
        values.put(MySQLiteHelper.COLUMN_RIGHT , 0);
        database.insert(MySQLiteHelper.TABLE_STATUS  , null , values);
    }

    public void update(long _right , long _wrong , int id){
        String strFilter = MySQLiteHelper.COLUMN_ID + "=" + id;
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_RIGHT , _right);
        args.put(MySQLiteHelper.COLUMN_WRONG , _wrong);
        database.update(MySQLiteHelper.TABLE_STATUS, args, strFilter, null);
    }

    public ArrayList<Status> getAllStatus() {
        ArrayList<Status> statusList = new ArrayList<Status>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_STATUS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Status status = cursorToComment(cursor);
            statusList.add(status);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return statusList;
    }

    private Status cursorToComment(Cursor cursor) {
        Status status = new Status();
        status.setId(cursor.getLong(0));
        status.setCharacter(cursor.getString(1));
        status.setRome(cursor.getString(2));
        status.setRightCount(cursor.getLong(3));
        status.setWrongCount(cursor.getLong(4));

        return status;
    }
}
