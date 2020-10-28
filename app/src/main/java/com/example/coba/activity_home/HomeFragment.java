package com.example.coba.activity_home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_home.adapter.AdapterAllCategory;
import com.example.coba.activity_home.adapter.AdapterKbmCategory;
import com.example.coba.activity_home.adapter.AdapterKuliahCategory;
import com.example.coba.activity_home.adapter.AdapterLkfCategory;
import com.example.coba.activity_home.adapter.AdapterPanitiaCategory;
import com.example.coba.activity_home.model.MenuHome;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.example.mylibrary.Views.EmptyRecycleView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public HomeFragment(){

    }
    @Override
    public void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public final static String TAG="Home";
    ImageButton openDrawer;
    EmptyRecycleView votingAll, votingLkf, votingKuliah, votingKbm, votingPanitia;
    ArrayList<MenuHome> menuHomes=new ArrayList<>();
    ArrayList<MenuHome> menuHomes1=new ArrayList<>();
    ArrayList<MenuHome> menuHomes2=new ArrayList<>();
    ArrayList<MenuHome> menuHomes3=new ArrayList<>();
    ArrayList<MenuHome> menuHomes4=new ArrayList<>();
    ScrollView svHome;
    SwipeRefreshLayout votingSwip;
    DrawerLayout drawerLayout;
    FloatingActionButton floatingactionbutton;
    AdapterAllCategory allCategory;
    AdapterLkfCategory lkfCategory;
    AdapterKuliahCategory kuliahCategory;
    AdapterKbmCategory kbmCategory;
    AdapterPanitiaCategory panitiaCategory;
    ShimmerFrameLayout shimmerAll, shimmerLkf, shimmerKbm, shimmerKuliah, shimmerPanitia;
    TextView saAll, saKbm, saLkf, saPanitia, saKuliah;
    ImageView noDataAll,noDataLkf,noDataKuliah,noDataKbm,noDataPanitia;
    String sToken;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        openDrawer=(ImageButton) view.findViewById(R.id.opDrawer);
        floatingactionbutton=(FloatingActionButton) view.findViewById(R.id.plus);
        votingSwip=(SwipeRefreshLayout)view.findViewById(R.id.votingSwip);
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        saAll=(TextView) view.findViewById(R.id.saAll);
        saLkf=(TextView) view.findViewById(R.id.saLkf);
        saKuliah=(TextView) view.findViewById(R.id.saKuliah);
        saPanitia=(TextView) view.findViewById(R.id.saPanitia);
        saKbm=(TextView) view.findViewById(R.id.saKbm);
        svHome=(ScrollView)view.findViewById(R.id.svHome);
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddVotingActivity.class);
                startActivity(intent);
            }
        });
        noDataAll=(ImageView)view.findViewById(R.id.noDataAll);
        noDataLkf=(ImageView)view.findViewById(R.id.noDataLkf);
        noDataKuliah=(ImageView)view.findViewById(R.id.noDataKuliah);
        noDataKbm=(ImageView)view.findViewById(R.id.noDataKbm);
        noDataPanitia=(ImageView)view.findViewById(R.id.noDataPanitia);
        shimmerAll=(ShimmerFrameLayout)view.findViewById(R.id.SfVotingAll);
        shimmerLkf=(ShimmerFrameLayout)view.findViewById(R.id.SfVotingLkf);
        shimmerKbm=(ShimmerFrameLayout)view.findViewById(R.id.SfVotingKbm);
        shimmerKuliah=(ShimmerFrameLayout)view.findViewById(R.id.SfVotingKuliah);
        shimmerPanitia=(ShimmerFrameLayout)view.findViewById(R.id.SfVotingPanitia);
        votingAll=(EmptyRecycleView)view.findViewById(R.id.VotingAll);
        votingLkf=(EmptyRecycleView)view.findViewById(R.id.VotingLKF);
        votingKuliah=(EmptyRecycleView)view.findViewById(R.id.VotingKULIAH);
        votingKbm=(EmptyRecycleView)view.findViewById(R.id.VotingKBM);
        votingPanitia=(EmptyRecycleView)view.findViewById(R.id.VotingPANITIA);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager3=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager4=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        votingAll.setLayoutManager(linearLayoutManager);
        votingLkf.setLayoutManager(linearLayoutManager1);
        votingKuliah.setLayoutManager(linearLayoutManager2);
        votingKbm.setLayoutManager(linearLayoutManager3);
        votingPanitia.setLayoutManager(linearLayoutManager4);
        allCategory=new AdapterAllCategory(getContext(),menuHomes,HomeFragment.this);
        lkfCategory=new AdapterLkfCategory(getContext(),menuHomes1,HomeFragment.this);
        kuliahCategory=new AdapterKuliahCategory(getContext(),menuHomes2,HomeFragment.this);
        kbmCategory= new AdapterKbmCategory(getContext(),menuHomes3,HomeFragment.this);
        panitiaCategory= new AdapterPanitiaCategory(getContext(),menuHomes4,HomeFragment.this);
        loadMenu();
        loadMenu1();
        loadMenu2();
        loadMenu3();
        loadMenu4();
        checkAdmin();
        votingSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        votingSwip.setRefreshing(false);
                        onReload();
                    }
                },2000);
            }
        });
        svHome.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scroll=votingSwip.getScrollY();
                if (scroll== 0){
                    votingSwip.setEnabled(true);
                }else
                    votingSwip.setEnabled(false);
            }
        });
        saAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ShowAllActivity.class);
                intent.putExtra("id","5");
                startActivity(intent);
            }
        });
        saKbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ShowAllActivity.class);
                intent.putExtra("id","1");
                startActivity(intent);
            }
        });
        saLkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ShowAllActivity.class);
                intent.putExtra("id","2");
                startActivity(intent);
            }
        });
        saKuliah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ShowAllActivity.class);
                intent.putExtra("id","3");
                startActivity(intent);
            }
        });
        saPanitia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ShowAllActivity.class);
                intent.putExtra("id","4");
                startActivity(intent);
            }
        });
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

    public void loadMenu(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", "5");
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                shimmerAll.stopShimmerAnimation();
                shimmerAll.setVisibility(View.GONE);
                Log.e("namama",response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");
                        menuHomes.clear();
                        if (results.length()<4){
                            saAll.setVisibility(View.GONE);
                            for (int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                MenuHome transaction = new MenuHome();
                                transaction.setId(item.getString("id_listMenu"));
                                transaction.setTitle(object.getString("nama"));
                                transaction.setUrl(object.getString("pictures"));
                                transaction.setIdVote(item.getString("id"));
                                menuHomes.add(transaction);
                            }
                            votingAll.setAdapter(allCategory);
                            votingAll.setEmptyView(noDataAll);
                            allCategory.notifyDataSetChanged();
                        }
                        else {
                            saAll.setVisibility(View.VISIBLE);
                            for (int i = 0; i < 4; i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                MenuHome transaction = new MenuHome();
                                transaction.setId(item.getString("id_listMenu"));
                                transaction.setTitle(object.getString("nama"));
                                transaction.setUrl(object.getString("pictures"));
                                transaction.setIdVote(item.getString("id"));
                                menuHomes.add(transaction);
                            }
                            votingAll.setAdapter(allCategory);
                            votingAll.setEmptyView(noDataAll);
                            allCategory.notifyDataSetChanged();
                        }

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
    public void loadMenu1(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", "2");
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                shimmerLkf.stopShimmerAnimation();
                shimmerLkf.setVisibility(View.GONE);
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");
                        menuHomes1.clear();
                        if (results.length()<4){
                            saLkf.setVisibility(View.GONE);
                            for (int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("2")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes1.add(transaction);
                                }

                            }
                            votingLkf.setAdapter(lkfCategory);
                            votingLkf.setEmptyView(noDataLkf);
                            lkfCategory.notifyDataSetChanged();
                        }else {
                            saLkf.setVisibility(View.VISIBLE);
                            for (int i = 0; i < 4; i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("2")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes1.add(transaction);
                                }

                            }
                            votingLkf.setAdapter(lkfCategory);
                            votingLkf.setEmptyView(noDataLkf);
                            lkfCategory.notifyDataSetChanged();
                        }

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
    public void loadMenu2(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", "3");
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                shimmerKuliah.stopShimmerAnimation();
                shimmerKuliah.setVisibility(View.GONE);
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");
                        menuHomes2.clear();
                        if (results.length()<4){
                            saKuliah.setVisibility(View.GONE);
                            for (int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("3")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes2.add(transaction);
                                }

                            }
                            votingKuliah.setAdapter(kuliahCategory);
                            votingKuliah.setEmptyView(noDataKuliah);
                            kuliahCategory.notifyDataSetChanged();
                        }else {
                            saKuliah.setVisibility(View.VISIBLE);
                            for (int i = 0; i < 4; i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("3")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes2.add(transaction);
                                }

                            }
                            votingKuliah.setAdapter(kuliahCategory);
                            votingKuliah.setEmptyView(noDataKuliah);
                            kuliahCategory.notifyDataSetChanged();
                        }

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
    public void loadMenu3(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", "1");
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                shimmerKbm.stopShimmerAnimation();
                shimmerKbm.setVisibility(View.GONE);
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");
                        menuHomes3.clear();
                        if (results.length()<4){
                            saKbm.setVisibility(View.GONE);
                            for (int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("1")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes3.add(transaction);
                                }

                            }
                            votingKbm.setAdapter(kbmCategory);
                            votingKbm.setEmptyView(noDataKbm);
                            kbmCategory.notifyDataSetChanged();
                        }else {
                            saKbm.setVisibility(View.VISIBLE);
                            for (int i = 0; i < 4; i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("1")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes3.add(transaction);
                                }

                            }
                            votingKbm.setAdapter(kbmCategory);
                            votingKbm.setEmptyView(noDataKbm);
                            kbmCategory.notifyDataSetChanged();
                        }

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
    public void loadMenu4(){
        JSONObject payload = new JSONObject(); //buat file berbentuk JSONObject dengan nama payload
        JsonHelper.put(payload, "token", sToken); // fungsi jadi untuk mempermudah
        JsonHelper.put(payload, "groupId", "4");
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                shimmerPanitia.stopShimmerAnimation();
                shimmerPanitia.setVisibility(View.GONE);
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");
                        menuHomes4.clear();
                        if (results.length()<4){
                            saPanitia.setVisibility(View.GONE);
                            for (int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("4")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes4.add(transaction);
                                }

                            }
                            votingPanitia.setAdapter(panitiaCategory);
                            votingPanitia.setEmptyView(noDataPanitia);
                            panitiaCategory.notifyDataSetChanged();
                        }else {
                            saPanitia.setVisibility(View.VISIBLE);
                            for (int i = 0; i < 4; i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject object = item.getJSONObject("listMenu");
                                String group=object.getString("group_id");
                                if (group.equals("4")){
                                    MenuHome transaction = new MenuHome();
                                    transaction.setId(item.getString("id_listMenu"));
                                    transaction.setTitle(object.getString("nama"));
                                    transaction.setUrl(object.getString("pictures"));
                                    transaction.setIdVote(item.getString("id"));
                                    menuHomes4.add(transaction);
                                }

                            }
                            votingPanitia.setAdapter(panitiaCategory);
                            votingPanitia.setEmptyView(noDataPanitia);
                            panitiaCategory.notifyDataSetChanged();
                        }

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

    public void onReload() {
        menuHomes.clear();
        menuHomes1.clear();
        menuHomes2.clear();
        menuHomes3.clear();
        menuHomes4.clear();
        loadMenu();
        loadMenu1();
        loadMenu2();
        loadMenu3();
        loadMenu4();
    }
    @Override
    public void onResume(){
        super.onResume();
        shimmerAll.startShimmerAnimation();
        shimmerLkf.startShimmerAnimation();
        shimmerKbm.startShimmerAnimation();
        shimmerKuliah.startShimmerAnimation();
        shimmerPanitia.startShimmerAnimation();
        onReload();
    }
    @Override
    public void onPause(){
        shimmerAll.stopShimmerAnimation();
        shimmerLkf.stopShimmerAnimation();
        shimmerKuliah.stopShimmerAnimation();
        shimmerKbm.stopShimmerAnimation();
        shimmerPanitia.stopShimmerAnimation();
        super.onPause();
    }
}
