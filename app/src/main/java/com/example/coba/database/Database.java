package com.example.coba.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mylibrary.Models.UserInfo;

public class Database {
    private static final String TAG = "Database";
    private static final String DATABASE_NAME = "ftek";
    private static final int DATABASE_VERSION = 2;

    private static DatabaseHelper mDbHelper;
    private static Context context;
    public static SQLiteDatabase db;

    public Database(Context ctx){ context = ctx; }
    public Database open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        db = mDbHelper.getWritableDatabase();
        return this;
    }
    public static void clear(){

    }
    public void close() {mDbHelper.close();}

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper (Context context){ super(context, DATABASE_NAME, null, DATABASE_VERSION);}

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(UserInfo.SCHEMA.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrade Database from version" + oldVersion + " to " + newVersion + "which destroys all old data");

            db.execSQL("DROP TABLE IF EXISTS " + UserInfo.SCHEMA.TABLE);
            onCreate(db);
        }

        @Override
        public void  onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Downgreading Database from version" + oldVersion + " to " + newVersion + "which destroys all old data");

            db.execSQL("DROP TABLE IF EXISTS " + UserInfo.SCHEMA.TABLE);
            onCreate(db);
        }
    }

}
