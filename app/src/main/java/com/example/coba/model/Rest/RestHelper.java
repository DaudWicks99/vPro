package com.example.coba.model.Rest;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class RestHelper {
    private static final String TAG = RestHelper.class.getSimpleName();
    public static boolean validateResponse (JSONObject reap){
        try{
            if (reap.getInt("code")==0)
                return true;
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return false;
    }
    public static Response.ErrorListener generalErrorResponse(final String TAG, final ProgressDialog pdialog) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(pdialog!=null) pdialog.dismiss();
                Log.e(TAG, "Error: " + error.getMessage());
                if(error.networkResponse!=null){
                    String err = new String(error.networkResponse.data);
                    Log.e(TAG, "Error Network: " + err);
                }
            }
        };
    }

}
