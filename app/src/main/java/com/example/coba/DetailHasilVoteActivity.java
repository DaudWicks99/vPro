package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class DetailHasilVoteActivity extends AppCompatActivity {
    TextView txtJdlHasil;
    TextView total;
    TextView title;
    ImageView detailsVoteBack;
    LinearLayout AlreadyVote;
    LinearLayout NoAlreadyVote;
    PorterShapeImageView background;
    String sToken;
    String id;
    String hasil="";
    ProgressDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hasil_vote);
        txtJdlHasil=(TextView)findViewById(R.id.txtJdlHasil);
        AlreadyVote=(LinearLayout)findViewById(R.id.AlreadyVote);
        NoAlreadyVote=(LinearLayout)findViewById(R.id.NoAlreadyVote);
        total=(TextView)findViewById(R.id.total);
        title=(TextView)findViewById(R.id.txtVwHasilVt);
        spinner=new ProgressDialog(DetailHasilVoteActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        background=(PorterShapeImageView)findViewById(R.id.imgHasilVt);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        detailsVoteBack=(ImageView)findViewById(R.id.detailsVoteBack);
        detailsVoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                            String msg=response.getString("msg");
                            if (msg.equals("You Already Vote")){
                                AlreadyVote.setVisibility(View.VISIBLE);
                                NoAlreadyVote.setVisibility(View.GONE);
                                JSONObject item = response.getJSONObject("result");
                                total.setText(item.getString("opsiVoting")+" : "+item.getString("total"));
                                JSONObject object2 = item.getJSONObject("listMenu");
                                String imgName=object2.getString("pictures");
                                String url="http://167.71.199.106:8001/common/uploads/ListMenuPic/low/"+imgName;
                                Picasso.get()
                                        .load(url)
                                        .placeholder(R.drawable.placeholder)
                                        .into(background);
                                title.setText(object2.getString("nama"));
                            }else {
                                AlreadyVote.setVisibility(View.GONE);
                                NoAlreadyVote.setVisibility(View.VISIBLE);
                                JSONObject item = response.getJSONObject("result");
                                String imgName=item.getString("pictures");
                                String url="http://167.71.199.106:8001/common/uploads/ListMenuPic/low/"+imgName;
                                Picasso.get()
                                        .load(url)
                                        .placeholder(R.drawable.placeholder)
                                        .into(background);
                                title.setText(item.getString("nama"));
                            }




                        }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }

                }
            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.AMBIL_SATU_HASIL,payload,successResp,errorResp);
        spinner.setMessage("Loading.....");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }
}
