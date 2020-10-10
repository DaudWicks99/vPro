package com.example.coba.activity_hasilv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_hasilv.adaptor.CustomAdapterListHasil;
import com.example.coba.activity_hasilv.model.ListHasil;
import com.example.coba.activity_home.ShowAllActivity;
import com.example.coba.activity_home.adapter.CustomAdapterListItem;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class HasilVotingActivity extends AppCompatActivity {
    ShimmerFrameLayout shimmer;
    ArrayList<ListHasil> transactions=new ArrayList<>();
    String sToken;
    String id;
    Integer data1;
    Integer data2;
    ListView LsViHasilVoting;
    CustomAdapterListHasil adapter;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_voting);
        shimmer=(ShimmerFrameLayout)findViewById(R.id.SfHasilVoting);
        LsViHasilVoting=(ListView)findViewById(R.id.LsViHasilVoting);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        back=(ImageButton)findViewById(R.id.backHasilVoting);
        Intent intent= getIntent();
        id=intent.getStringExtra("id");
        adapter=new CustomAdapterListHasil(HasilVotingActivity.this,transactions,HasilVotingActivity.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadMenu(id);
    }
    public void loadMenu(String id) {
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", id);
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.GONE);
                Log.e("hasil", response.toString());
                if (!RestHelper.validateResponse(response)) {
                    Log.w("jashkbfjas", "Not a valid response");
                    return;
                } else {
                    try {
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            ListHasil transaction = new ListHasil();
                            transaction.setId(item.getString("id"));
                            transaction.setNama(item.getString("nama"));
                            transaction.setUrl(item.getString("pictures"));
                            transactions.add(transaction);
                        }
                        Collections.sort(transactions, new Comparator<ListHasil>() {
                            @Override
                            public int compare(ListHasil o1, ListHasil o2) {
                                try {
                                    data1 = Integer.parseInt(o1.getId());
                                    data2 = Integer.parseInt(o2.getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return data2.compareTo(data1);
                            }
                        });
                        LsViHasilVoting.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException ex) {
                        Log.w("dsadsa", "not a valid object");
                    }
                }


            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("sdad", null);
        JsonObjectRequest myReq = new JsonObjectRequest(RestUrl.getUrl(RestUrl.AMBIL_LIST_HASIL), payload, successResp, errorResp);
        AppController.getRest().addToReqq(myReq, "sda");
    }
        public void onReload(){
            transactions.clear();
            loadMenu(id);
        }
        @Override
        public void onResume(){
            super.onResume();
            shimmer.startShimmerAnimation();
            onReload();
        }
        @Override
        public void onPause(){
            shimmer.stopShimmerAnimation();
            super.onPause();
        }

        public static Date toDate(String value) throws ParseException {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            return format.parse(value);
        }
    }
