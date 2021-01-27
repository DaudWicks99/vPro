package com.example.coba.activity_home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_home.adapter.CustomAdapterListItem;
import com.example.coba.activity_home.adapter.CustomAdapterListKbm;
import com.example.coba.activity_home.model.MenuHome;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.activity_info.model.InfoMenu;
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

import static com.example.coba.activity_info.InfoFragment.toDate;

public class ShowAllActivity extends AppCompatActivity {
    ListView LsViShowAll;
    ImageButton opDrawerShowAll;
    SwipeRefreshLayout refreshShowAll;
    CustomAdapterListItem adapter;
    ArrayList <listItem> transactions=new ArrayList<>();
    String sToken;
    String id;
    Integer data1;
    Integer data2;
    ShimmerFrameLayout shimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        LsViShowAll=(ListView)findViewById(R.id.LsViShowAll);
        opDrawerShowAll=(ImageButton)findViewById(R.id.opDrawerShowAll);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        shimmer=(ShimmerFrameLayout)findViewById(R.id.SfShowAll);
        Intent intent= getIntent();
        id=intent.getStringExtra("id");
        adapter=new CustomAdapterListItem(ShowAllActivity.this,transactions,ShowAllActivity.this);
        opDrawerShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshShowAll=(SwipeRefreshLayout)findViewById(R.id.refreshShowAll);
        LsViShowAll.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean scrollEnabled;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (LsViShowAll == null || LsViShowAll.getChildCount() == 0) ?
                                0 : LsViShowAll.getChildAt(0).getTop();

                boolean newScrollEnabled =
                        (firstVisibleItem == 0 && topRowVerticalPosition >= 0) ?
                                true : false;

                if (null != refreshShowAll && scrollEnabled != newScrollEnabled) {
                    // Start refreshing....
                    refreshShowAll.setEnabled(newScrollEnabled);
                    refreshShowAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshShowAll.setRefreshing(false);
                                    onReload();
                                }
                            },2000);

                        }
                    });
                    scrollEnabled = newScrollEnabled;

                }
            }
        });
        loadMenu(id);
    }
    public void loadMenu(String id){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", id);
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.GONE);
                Log.e("namama",response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                            for (int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                listItem transaction = new listItem();
                                transaction.setId(item.getString("id_listMenu"));
                                transaction.setTitle(object.getString("nama"));
                                transaction.setUrl(object.getString("pictures"));
                                transaction.setIdVote(item.getString("id"));
                                transactions.add(transaction);
                            }
                        Collections.sort(transactions, new Comparator<listItem>() {
                            @Override
                            public int compare(listItem o1, listItem o2) {
                                try {
                                    data1=Integer.parseInt(o1.getId());
                                    data2=Integer.parseInt(o2.getId());
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                return data2.compareTo(data1);
                            }
                        });
                            LsViShowAll.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                    }catch (JSONException ex){
                        Log.w("dsadsa", "not a valid object");
                    }
                }


            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("sdad", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.AMBIL_MENU),payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"sda");

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
