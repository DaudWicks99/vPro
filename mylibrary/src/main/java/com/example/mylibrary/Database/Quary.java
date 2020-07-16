package com.example.mylibrary.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Quary {
    public SQLiteDatabase mDb;
    String sql;

    public Quary(SQLiteDatabase db){mDb = db;}

    private ContentValues setValue(Cursor cursor, ContentValues model) {
        for (int i = 0; i < cursor.getColumnCount(); i++){
            switch (cursor.getType(i)){
                case Cursor.FIELD_TYPE_STRING: {
                    model.put(cursor.getColumnName(i), cursor.getString(i));
                    break;
                }
                case Cursor.FIELD_TYPE_INTEGER: {
                    model.put(cursor.getColumnName(i), cursor.getInt(i));
                    break;
                }
                case Cursor.FIELD_TYPE_FLOAT: {
                    model.put(cursor.getColumnName(i), cursor.getFloat(i));
                    break;
                }
                case Cursor.FIELD_TYPE_BLOB: {
                    model.put(cursor.getColumnName(i), cursor.getBlob(i));
                    break;
                }
                default:{
                    model.put(cursor.getColumnName(i), cursor.getString(i));
                    break;
                }
            }
        }
        return model;
    }

    public List<ContentValues> all() {
        List<ContentValues> models = new ArrayList<>();
        try{
            Cursor cursor = mDb.rawQuery(sql, null);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    ContentValues model = new ContentValues();
                    models.add(setValue(cursor, model));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return models;
    }
    public ContentValues one(){
        Cursor cursor = mDb.rawQuery(sql, null);
        ContentValues attrs = new ContentValues();
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            attrs = setValue(cursor, attrs);
            cursor.close();
        }
        return attrs;
    }
}
