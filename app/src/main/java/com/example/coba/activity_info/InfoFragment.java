package com.example.coba.activity_info;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AddInfoActivity;
import com.example.coba.AddVotingActivity;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.VoteActivity;
import com.example.coba.activity_hasilv.SemuaFragment;
import com.example.coba.activity_hasilv.bpmfFragment;
import com.example.coba.activity_hasilv.kbmFragment;
import com.example.coba.activity_hasilv.panitiaFragment;
import com.example.coba.activity_hasilv.smfFragment;
import com.example.coba.activity_home.adapter.AdapterListMenu;
import com.example.coba.activity_home.adapter.CustomAdapterListItem;
import com.example.coba.activity_home.adapter.ViewPagerAdapter;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.activity_info.adapterr.AdapterrListMenu;
import com.example.coba.activity_info.adapterr.CustomAdapterListItemInfo;
import com.example.coba.activity_info.model.InfoMenu;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class InfoFragment extends Fragment {
    public InfoFragment() {

    }
    @Override
    public void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public final static String TAG="INFO";
    String[] title={"Seminar","POM"};
    int[] images={R.drawable.basket,1};
    String[] vote={"Deskripsi","Pekan olahraga mahasiswa"};
    ListView LsVi;
    ArrayList<InfoMenu> transactions=new ArrayList<>();
    CustomAdapterListItemInfo adapterr;
    String sToken;
    FloatingActionButton floatingactionbutton;
    ImageButton opDrawer;
    DrawerLayout drawerLayout;
    ViewPager viewPager;
    SwipeRefreshLayout infSwip;

    Date data1;
    Date data2;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

       LsVi =(ListView)view.findViewById(R.id.LsVi);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        floatingactionbutton=(FloatingActionButton) view.findViewById(R.id.plusInfo);
        LsVi.setAdapter(adapterr);
        floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddInfoActivity.class);
                startActivity(intent);
            }
        });
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        opDrawer=(ImageButton)view.findViewById(R.id.opDrawer);
        opDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        infSwip=(SwipeRefreshLayout)view.findViewById(R.id.infSwip);




        adapterr=new CustomAdapterListItemInfo(getContext(),transactions,InfoFragment.this);
        getTransactions();
        LsVi.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean scrollEnabled;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (LsVi == null || LsVi.getChildCount() == 0) ?
                                0 : LsVi.getChildAt(0).getTop();

                boolean newScrollEnabled =
                        (firstVisibleItem == 0 && topRowVerticalPosition >= 0) ?
                                true : false;

                if (null != infSwip && scrollEnabled != newScrollEnabled) {
                    // Start refreshing....
                    infSwip.setEnabled(newScrollEnabled);
                    infSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    infSwip.setRefreshing(false);
                                    onReload();
                                }
                            },2000);

                        }
                    });
                    scrollEnabled = newScrollEnabled;

                }
            }
        });
        checkAdmin();
        return view;
    }
    public void checkAdmin(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(JSONObject response) {
//                Log.e("response", response.toString());
                if (!RestHelper.validateResponse(response)){
                    Log.w("adapter", "invalid response");
                    return;
                }else {
                    try {
                        String mag=response.getString("msg");
//                        Log.e("pesan",mag);
                        if (mag.equals("You are admin")){
                            floatingactionbutton.setVisibility(View.VISIBLE);

                        }else {
                            floatingactionbutton.setVisibility(View.GONE);

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(null,null);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.CHECK_ADMIN),payload,successResp,errorResp);
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }




    public void getTransactions(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.e("namama",response.toString());
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
                            InfoMenu transaction = new InfoMenu();
                            transaction.setId(item.getString("id"));
                            transaction.setTitle(item.getString("judul"));
                            transaction.setDesc(item.getString("description"));
                            transaction.setUrl(item.getString("image"));
                            transaction.setDate(item.getString("create_at"));
                            transactions.add(transaction);
                        }
                        Collections.sort(transactions, new Comparator<InfoMenu>() {
                            @Override
                            public int compare(InfoMenu o1, InfoMenu o2) {
                                try {
                                    data1=toDate(o1.getDate());
                                    data2=toDate(o2.getDate());
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                                return data2.compareTo(data1);
                            }
                        });
//                        Log.e("dsad",transactions.toString());
                        LsVi.setAdapter(adapterr);
                        adapterr.reload(transactions);
                        adapterr.notifyDataSetChanged();
                    }catch (JSONException ex){
                        Log.w("dsadsa", "not a valid object");
                    }
                }


            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("sdad", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.AMBIL_INFO),payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"sda");

    }
    public void onReload(){
        transactions.clear();
        getTransactions();
    }

    @Override
    public void onResume(){
        super.onResume();
        onReload();
    }


    public static Date toDate(String value) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return format.parse(value);
    }

}
