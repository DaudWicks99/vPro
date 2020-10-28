package com.example.coba.activity_home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailHasilVoteActivity extends AppCompatActivity {
    TextView txtJdlHasil;
    TextView total1,total2,total3,total4,total5;
    TextView title;
    ImageView detailsVoteBack;
    LinearLayout AlreadyVote;
    LinearLayout NoAlreadyVote;
    PorterShapeImageView background;
    PieChart pieChart;
    String sToken;
    String id;
    String hasil="";
    int value1, value2, value3, value4, value5;
    String rawValue1, rawValue2, rawValue3, rawValue4, rawValue5;
    String key1, key2, key3, key4, key5;
    ProgressDialog spinner;
    float jumlahTotal = 0;
    ArrayList <PieEntry> pieEntry = new ArrayList<>();
    long time;
    String times, judul;
    String hasilVote = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hasil_vote);
        txtJdlHasil=(TextView)findViewById(R.id.txtJdlHasil);
        AlreadyVote=(LinearLayout)findViewById(R.id.AlreadyVote);
        NoAlreadyVote=(LinearLayout)findViewById(R.id.NoAlreadyVote);
        total1=(TextView)findViewById(R.id.total1);
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
        pieChart=(PieChart)findViewById(R.id.pieChartHasilVote);
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
                        Log.e("hasil", response.toString());
                            if (msg.equals("You Already Vote")){
                                AlreadyVote.setVisibility(View.VISIBLE);
                                NoAlreadyVote.setVisibility(View.GONE);
                                JSONArray item2 = response.getJSONArray("result2");
                                for (int i = 0; i < item2.length(); i++) {
                                    JSONObject objct = item2.getJSONObject(i);
                                    String imgName=objct.getString("pictures");
                                    String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_VOTING)+imgName+"?time="+times;
                                    Picasso.get()
                                            .load(url)
                                            .placeholder(R.drawable.placeholder)
                                            .into(background);
                                    title.setText(objct.getString("nama"));
                                    judul= objct.getString("nama");
                                }
                                JSONArray item = response.getJSONArray("result");
                                Log.e("loop",String.valueOf(item.length()));
                                for (int i = 0; i < item.length(); i++){
                                    JSONObject object = item.getJSONObject(i);
                                    int value = Integer.parseInt(object.getString("total"));
                                    jumlahTotal = jumlahTotal + value;

                                }
                                if (jumlahTotal!=0){
                                    for (int i = 0; i < item.length(); i++){
                                        JSONObject object = item.getJSONObject(i);
                                        int value = Integer.parseInt(object.getString("total"));
                                        String opsi = object.getString("opsiVoting");
                                        float presentase = value/jumlahTotal*100;
                                        pieEntry.add(new PieEntry(presentase,opsi));
                                        String totalVote = opsi+" "+ presentase+"%"+"\n";
                                        hasilVote = hasilVote + totalVote;
                                    }
                                   // total1.setText(hasilVote);
                                    PieDataSet pieDataSet= new PieDataSet(pieEntry,judul);
                                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                    pieDataSet.setValueTextColor(R.color.black);
                                    pieDataSet.setValueTextSize(16f);

                                    PieData pieData=new PieData(pieDataSet);
                                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                                    pieChart.setUsePercentValues(true);
                                    pieChart.setData(pieData);
                                    pieChart.getDescription().setEnabled(false);
                                    pieChart.setCenterText(judul);
                                    pieChart.animateXY(2000,2000);
                                }

                            }else {
                                AlreadyVote.setVisibility(View.GONE);
                                NoAlreadyVote.setVisibility(View.VISIBLE);
                                JSONArray item2 = response.getJSONArray("result2");
                                for (int i = 0; i < item2.length(); i++) {
                                    JSONObject objct = item2.getJSONObject(i);
                                    String imgName=objct.getString("pictures");
                                    String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_VOTING)+imgName+"?time="+times;
                                    Picasso.get()
                                            .load(url)
                                            .placeholder(R.drawable.placeholder)
                                            .into(background);
                                    title.setText(objct.getString("nama"));
                                }
                            }




                        }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }

                }
            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.AMBIL_SATU_HASIL),payload,successResp,errorResp);
        spinner.setMessage("Loading.....");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }
}
