package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class VoteActivity extends AppCompatActivity {


    RadioGroup rdGroup;
    RadioButton rdBtn1;
    RadioButton rdBtn2;
    RadioButton rdBtn3;
    RadioButton rdBtn4;
    RadioButton rdBtn5;
    ImageView detailVoteBack;
    FancyButton submitVote;
    PorterShapeImageView background;
    TextView title;
    String sToken;
    String id;
    String hasil="";
    ProgressDialog spinner;

    long time;
    String times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        background=(PorterShapeImageView)findViewById(R.id.imgVote);
        title=(TextView)findViewById(R.id.txtVw);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        spinner=new ProgressDialog(VoteActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        submitVote=(FancyButton)findViewById(R.id.btnSubmitVote);
        detailVoteBack=(ImageView) findViewById(R.id.detailsVoteBack);
        detailVoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        getData(id);

        rdGroup=(RadioGroup)findViewById(R.id.rdGroup);
        rdBtn1=(RadioButton)findViewById(R.id.rdBtn1);
        rdBtn2=(RadioButton)findViewById(R.id.rdBtn2);
        rdBtn3=(RadioButton)findViewById(R.id.rdBtn3);
        rdBtn4=(RadioButton)findViewById(R.id.rdBtn4);
        rdBtn5=(RadioButton)findViewById(R.id.rdBtn5);

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
                            final String idVote= item.getString("id");
                            JSONObject object2 = item.getJSONObject("listMenu");
                            String imgName=object2.getString("pictures");
                            String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_VOTING)+imgName+"?time="+times;
                            Picasso.get()
                                    .load(url)
                                    .placeholder(R.drawable.placeholder)
                                    .into(background);
                            title.setText(object2.getString("nama"));

                            final String rbtn1=item.getString("voting1");
                            final String rbtn2=item.getString("voting2");
                            final String rbtn3=item.getString("voting3");
                            final String rbtn4=item.getString("voting4");
                            final String rbtn5=item.getString("voting5");
                            Log.e("", rbtn1+rbtn2+rbtn3+rbtn4+rbtn5);
                            rdBtn1.setText(rbtn1);
                            if (!rbtn2.equals("")){
                                rdBtn2.setText(rbtn2);
                                rdBtn2.setVisibility(View.VISIBLE);
                            }
                            else {
                                rdBtn2.setVisibility(View.GONE);
                            }

                            if (!rbtn3.equals("")){
                                rdBtn3.setText(rbtn3);
                                rdBtn3.setVisibility(View.VISIBLE);
                            }
                            else {
                                rdBtn3.setVisibility(View.GONE);
                            }

                            if (!rbtn4.equals("")){
                                rdBtn4.setText(rbtn4);
                                rdBtn4.setVisibility(View.VISIBLE);
                            }
                            else {
                                rdBtn4.setVisibility(View.GONE);
                            }

                            if (!rbtn5.equals("")){
                                rdBtn5.setText(rbtn5);
                                rdBtn5.setVisibility(View.VISIBLE);
                            }
                            else {
                                rdBtn5.setVisibility(View.GONE);
                            }

                            rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if(checkedId==R.id.rdBtn1){
                                    hasil=rbtn1;
                                }
                                else if(checkedId==R.id.rdBtn2){
                                    hasil=rbtn2;
                                }
                                else if(checkedId==R.id.rdBtn3){
                                    hasil=rbtn3;
                                }
                                else if(checkedId==R.id.rdBtn4){
                                    hasil=rbtn4;
                                }
                                else if(checkedId==R.id.rdBtn5){
                                    hasil=rbtn5;
                                }


                            }
                        });
                            submitVote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!hasil.equals("")){
                                        submitItem(idVote,hasil);
                                    }else {
                                        Log.e(" ", "pilih dulu");
                                        Toast.makeText(VoteActivity.this,"Tolong Pilih Vote Terlebih Dahulu", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    }}
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }

            }
        }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.AMBIL_SATU_VOTE),payload,successResp,errorResp);
        spinner.setMessage("Loading.....");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }

    public void submitItem (String idVote, String hasil){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);
        JsonHelper.put(payload,"idList", id);
        JsonHelper.put(payload,"idVote", idVote);
        JsonHelper.put(payload,"hasil", hasil);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                Log.e("", response.toString());
                spinner.dismiss();
                Log.e(" ", response.toString());
                JSONObject object=response;
                try {
                    int code= Integer.parseInt(object.getString("code"));
                    if (code==1){
                        String msg=object.getString("msg");
                        Toast.makeText(VoteActivity.this,msg, Toast.LENGTH_LONG).show();
                    }else {
                        onBackPressed();
                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.SUBMIT_VOTE),payload,successResp,errorResp);
        spinner.setMessage("Loading.....");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }

}
