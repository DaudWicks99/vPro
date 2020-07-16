package com.example.mylibrary.Models;

import android.database.sqlite.SQLiteDatabase;

import com.example.mylibrary.Database.ActiveRecord;
import com.example.mylibrary.Database.BaseSchema;

public class UserInfo extends ActiveRecord {
    public UserInfo(SQLiteDatabase db) {super(db);}

    public UserInfo(SQLiteDatabase db, String name, String value){
        super(db);
        set("name", name);
        set("value", value);
    }

    @Override
    public String tableName() { return UserInfo.SCHEMA.TABLE;}

    @Override
    public String primaryKey() {return "_id";}
    public interface SCHEMA extends BaseSchema {
        String TABLE = "userinfo";
        String NAME = "name";
        String VALUE = "value";
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
                + ID + " INTERGER PRIMARY KEY, "
                + NAME + " TEXT NOT NULL, "
                + VALUE + " TEXT"
                + ")";

        String[] COLUMNS = new String[]{
                ID,
                NAME,
                VALUE
        };
    }
}
