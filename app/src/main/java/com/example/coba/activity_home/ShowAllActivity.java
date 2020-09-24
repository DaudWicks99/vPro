package com.example.coba.activity_home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.coba.activity_home.adapter.CustomAdapterListItem;
import com.example.coba.activity_home.adapter.CustomAdapterListKbm;
import com.example.coba.activity_home.model.MenuHome;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.activity_info.model.InfoMenu;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;

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
    Date data1;
    Date data2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        LsViShowAll=(ListView)findViewById(R.id.LsViShowAll);
        opDrawerShowAll=(ImageButton)findViewById(R.id.opDrawerShowAll);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        Intent intent= getIntent();
        id=intent.getStringExtra("id");
        adapter=new CustomAdapterListItem(ShowAllActivity.this,transactions,ShowAllActivity.this);
        opDrawerShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                Log.e("namama",response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");

                            for (int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                listItem transaction = new listItem();
                                transaction.setId(item.getString("id_listMenu"));
                                transaction.setTitle(object.getString("nama"));
                                transaction.setUrl(object.getString("pictures"));
                                transaction.setIdVote(item.getString("id"));
                                transaction.setDate(item.getString("create_at"));
                                transactions.add(transaction);
                            }
                        Collections.sort(transactions, new Comparator<listItem>() {
                            @Override
                            public int compare(listItem o1, listItem o2) {
                                try {
                                    data1=toDate(o1.getDate());
                                    data2=toDate(o2.getDate());
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

    public static Date toDate(String value) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return format.parse(value);
    }
}
