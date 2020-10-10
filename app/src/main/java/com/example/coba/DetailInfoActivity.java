package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailInfoActivity extends AppCompatActivity {
    ImageView background;
    TextView jdlDtl;
    TextView descDtl;
    RelativeLayout Details;
    ImageView detailsInfoBack,IvDetailInfo;

    String sToken;
    String id;
    ZoomInImageView imgDtlInf;
    ProgressDialog spinner;
    long time;
    String times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        IvDetailInfo=(ImageView)findViewById(R.id.IvDetailInfo);
        background=(ImageView)findViewById(R.id.IvDetailInfo);
        jdlDtl=(TextView)findViewById(R.id.jdlDtl);
        descDtl=(TextView)findViewById(R.id.descDtl);
        time=System.currentTimeMillis();
        times=String.valueOf(time);
        spinner=new ProgressDialog(DetailInfoActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        detailsInfoBack=(ImageView) findViewById(R.id.detailsInfoBack);
        detailsInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailInfoActivity.this,MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DetailInfoActivity.this.finish();
                startActivity(intent);
            }
        });


        imgDtlInf=(ZoomInImageView) findViewById(R.id.imgDtlInf);



        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        getData(id);


    }
    public void getData (String ids){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);
        JsonHelper.put(payload,"id", ids);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                spinner.dismiss();
                Log.e(" ", response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Cannot login");
                }
                else {
                    try {
                        JSONArray result= response.getJSONArray("result");
                        for(int i = 0; i < result.length(); i++){
                            JSONObject item = result.getJSONObject(i);
                            String imgName=item.getString("image");
                            String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_INFO)+imgName+"?time="+times;
                            Log.e(" ",url);
                            Picasso.get()
                                    .load(url)
                                    .placeholder(R.drawable.placeholder)
                                    .into(imgDtlInf);
                            Picasso.get()
                                    .load(url)
                                    .placeholder(R.drawable.placeholder)
                                    .into(background);
                            jdlDtl.setText(item.getString("judul"));
                            descDtl.setText(item.getString("description"));
                        }




                    }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }

                }
            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.AMBIL_SATU_INFO),payload,successResp,errorResp);
        spinner.setMessage("Loading.....");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }
}
