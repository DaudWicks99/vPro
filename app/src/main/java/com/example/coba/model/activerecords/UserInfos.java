package com.example.coba.model.activerecords;

import android.database.sqlite.SQLiteDatabase;

import com.example.coba.database.Database;
import com.example.mylibrary.Database.ActiveRecord;
import com.example.mylibrary.Models.UserInfo;

import java.util.List;

public class UserInfos {
    public String token;
    public String username;
    public String password;
    public String id;
    private SQLiteDatabase db;
    private static UserInfos instance;

    private UserInfos(){
    }

    public static UserInfos getFromDatabase(SQLiteDatabase db){

        instance = new UserInfos();
        List<? extends ActiveRecord> uinfos =ActiveRecord.findAlls(new UserInfo(db));
        for (ActiveRecord et : uinfos) {
            if (et.get("name").equals("username")) instance.username=et.get("value");
            if (et.get("name").equals("password")) instance.password=et.get("value");
            if (et.get("name").equals("id")) instance.id=et.get("value");
            if (et.get("name").equals("token")) instance.token=et.get("value");
        }
        return instance;
    }

    public static UserInfos saveToDatabase(SQLiteDatabase db, UserInfos inst){
        UserInfo.deleteAll(new UserInfo(db));
        new UserInfo(Database.db, "username",inst.username).save();
        new UserInfo(Database.db, "password",inst.password).save();
        new UserInfo(Database.db, "id",inst.id).save();
        new UserInfo(Database.db, "token",inst.token).save();

        return inst;
    }

    public UserInfos setUsername(String username){
        this.username=username;
        return this;
    }
    public UserInfos setPassword(String password){
        this.password=password;
        return this;
    }
    public UserInfos setId(String id){
        this.id=id;
        return this;
    }
    public UserInfos setToken(String token){
        this.token=token;
        return this;
    }
    public UserInfos save(){
        this.saveToDatabase(db, this);
        return this;
    }
}
