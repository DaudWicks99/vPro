package com.example.coba;

import android.app.Application;
import android.content.Context;
import android.database.SQLException;

import com.example.coba.database.Database;
import com.example.coba.model.Rest.RestSingleTon;

public class AppController extends Application {
    public static Database mDb;
    private RestSingleTon rest;
    private  static AppController instance;

    @Override
    public void onTerminate(){
        mDb.close();
        super.onTerminate();

    }
    @Override
    protected void attachBaseContext(Context base) {super.attachBaseContext(base);}
    public static synchronized AppController getInstance() {return instance;}

    public static synchronized RestSingleTon getRest() {return instance.rest;}
    public static synchronized Database getDb(){ return instance.mDb;}
    @Override
    public void  onCreate(){
        super.onCreate();
        rest = new RestSingleTon(this);
        instance = this;
        mDb = new Database(this);

        try{
            mDb.open();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }




}

