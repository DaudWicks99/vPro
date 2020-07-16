package com.example.coba.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.coba.LoginActivity;

import java.util.HashMap;

public class SessionManajer {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "VotingPref";
    private static final String IS_LOGIN = "IsLoginedIn";
    public static final String KEY_TOKEN = "token";


    public SessionManajer (Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSeasson(String token){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN, token);

        editor.commit();
    }

    public void checkLogin(){
        if(!this.IsLoginedIn()){
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }
    }

    public HashMap<String, String>getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        return user;
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean IsLoginedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
