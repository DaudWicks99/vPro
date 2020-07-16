package com.example.coba.model.picasso;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {
    public static void downloadImage(Context c, String url, ImageView imageView, int placeholder){
        if (url!=null&&url.length()>0){
            //Picasso.with(c).load(url).placeholder(placeholder).into(imageView);
        }
        else {
            //Picasso.with(c).load(placeholder).into(imageView);
        }
    }
}
