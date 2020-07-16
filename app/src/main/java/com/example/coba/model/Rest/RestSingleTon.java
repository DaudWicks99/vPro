package com.example.coba.model.Rest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RestSingleTon {
    private static final String TAG = RestSingleTon.class.getSimpleName();
    private static RestSingleTon instance;
    private Context ctx;
    private RequestQueue reqq;

    public RestSingleTon (Context ctx){
        instance = this;
        this.ctx = ctx;
    }

    public static RestSingleTon getInstance(){return instance;}

    public <T> void addToReqq(Request<T> req, String tag){
        req.setTag(tag);
        getReqq().add(req);
    }

    public  <T> void addToReqq (Request<T> req){
        req.setTag(TAG);
        getReqq().add(req);
    }

    public void canclePendingReq(Object tag) {
        if (reqq != null){
            reqq.cancelAll(tag);
        }
    }

    public RequestQueue getReqq(){
        if (reqq == null){
            reqq = Volley.newRequestQueue(ctx);
        }
        return reqq;
    }
}

