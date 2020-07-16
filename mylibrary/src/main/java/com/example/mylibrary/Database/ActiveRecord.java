package com.example.mylibrary.Database;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class ActiveRecord <T>{
    public static final String TAG = "ActiveRecord";
    public SQLiteDatabase mDb;
    public ContentValues attr;
    private ContentValues oldattr;
    boolean isNewRecord;

    public ActiveRecord(SQLiteDatabase db){
        setDb(db);
        oldattr = null;
        isNewRecord = true;
        attr = new ContentValues();
    }

    public static <T>T findOneStatic(ActiveRecord model, String k, String v){
        Quary quary = new Quary(model.mDb);
        quary.sql = "select * form " + model.tableName() + "where \"" + k + "\" = \"" + v + "\";";
        model.attr = quary.one();
        String pk = model.attr.getAsString(model.primaryKey());
        if (pk == null){
            model.isNewRecord = true;
        }
        else {
            model.isNewRecord = false;
        }
        return (T) model;
    }

    public static void deleteAll(ActiveRecord model){
        try{
            int cd = model.mDb.delete(model.tableName(), "1", null);
            Log.d(TAG, "Delete "+String.valueOf(cd));
        }
        catch (NullPointerException ex){
            Log.d(TAG, "error null");
            ex.printStackTrace();
        }
    }

    public void setDb(SQLiteDatabase db){this.mDb = db;}

    public  abstract String tableName();
    public  abstract String primaryKey();

    public String get(String key) {
        String o = attr.getAsString(key);
        if (o == null) {
            return "";
        }
        return o;
    }

    public ActiveRecord<T> set(String key, String value) {
        attr.put(key, value);
        return this;
    }
    public boolean validate(){
        if (attr == null) return false;
        return true;
    }

    public boolean save() {
        if (validate()) {
            if (isNewRecord) {
                oldattr = new ContentValues(attr);
                mDb.insert(tableName(), null, attr);
                Log.d(TAG, "save new item");
                return true;

            } else {
                String pk = attr.getAsString(primaryKey());
                Log.d(TAG, "update item");
                final String selectionArgs[] = {pk};
                final String selection = primaryKey() + " = ?";
                attr.remove(primaryKey());
                mDb.update(tableName(), attr, selection, selectionArgs);
                return true;
            }
        }return false;
    }
    public boolean delete(){
        if (isNewRecord) {
            return false;
        }
        final String selectionArgs[] = {attr.getAsString(primaryKey())};
        final String selection = primaryKey() + " = ?";
        if (mDb.delete(tableName(), selection, selectionArgs) > 0){
            return true;
        }
        return false;
    }

    public static List<? extends ActiveRecord> findAlls(ActiveRecord model){
        Quary quary = new Quary(model.mDb);
        quary.sql = "select * from " + model.tableName() + ";";

        List models = new ArrayList<>();
        for (ContentValues at : quary.all()){
            try{
                ActiveRecord mdl;
                Constructor<?> ctor = model.getClass().getConstructor(SQLiteDatabase.class);
                mdl = (ActiveRecord)ctor.newInstance(new Object[] {model.mDb});
                mdl.attr = at;
                models.add(mdl.getClass().cast(mdl));
            }catch (InstantiationException e){
                e.printStackTrace();
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return models;
    }
    public static Quary find(ActiveRecord model){
        Quary quary = new Quary(model.mDb);
        quary.sql = "select * from " + model.tableName() + ";";
        return quary;
    }

}
