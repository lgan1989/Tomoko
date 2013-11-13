package com.ganlu.tomoko;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lgan on 7/22/13.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    public static final String TABLE_STATUS = "status";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_CHARACTER = "character";
    public static final String COLUMN_STATUS = "status";

    private static final String DATABASE_NAME = "status.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STATUS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CHARACTER
            + " text not null, " + COLUMN_STATUS
            + " real ont null );";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        onCreate(db);
    }
}
