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
            MySQLiteHelper.COLUMN_STATUS };

    public StatusDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void newRecord(String cha , float status){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CHARACTER, cha);
        values.put(MySQLiteHelper.COLUMN_STATUS , status);
        database.insert(MySQLiteHelper.TABLE_STATUS  , null , values);
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
        status.setStatus(0);
        return status;
    }
}
