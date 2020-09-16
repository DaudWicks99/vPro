package com.example.coba.activity_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AddVotingActivity;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_home.adapter.AdapterAllCategory;
import com.example.coba.activity_home.adapter.AdapterKbmCategory;
import com.example.coba.activity_home.adapter.AdapterKuliahCategory;
import com.example.coba.activity_home.adapter.AdapterLkfCategory;
import com.example.coba.activity_home.adapter.AdapterPanitiaCategory;
import com.example.coba.activity_home.adapter.ViewPagerAdapter;
import com.example.coba.activity_home.model.MenuHome;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.example.coba.utils.SessionManajer;
import com.example.mylibrary.Views.EmptyRecycleView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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
    DrawerLayout drawerLayout;
    FloatingActionButton floatingactionbutton;
    AdapterAllCategory allCategory;
    AdapterLkfCategory lkfCategory;
    AdapterKuliahCategory kuliahCategory;
    AdapterKbmCategory kbmCategory;
    AdapterPanitiaCategory panitiaCategory;
    String sToken;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        openDrawer=(ImageButton) view.findViewById(R.id.opDrawer);
        floatingactionbutton=(FloatingActionButton) view.findViewById(R.id.plus);
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
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
        allCategory=new AdapterAllCategory(getContext(),menuHomes);
        lkfCategory=new AdapterLkfCategory(getContext(),menuHomes1);
        kuliahCategory=new AdapterKuliahCategory(getContext(),menuHomes2);
        kbmCategory= new AdapterKbmCategory(getContext(),menuHomes3);
        panitiaCategory= new AdapterPanitiaCategory(getContext(),menuHomes4);
        loadMenu();
        loadMenu1();
        loadMenu2();
        loadMenu3();
        loadMenu4();
        return view;

    }
    public void loadMenu(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
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
                            MenuHome transaction = new MenuHome();
                            transaction.setId(item.getString("id_listMenu"));
                            transaction.setTitle(object.getString("nama"));
                            transaction.setUrl(object.getString("pictures"));
                            transaction.setIdVote(item.getString("id"));
                            menuHomes.add(transaction);
                        }
                        votingAll.setAdapter(allCategory);
                        allCategory.notifyDataSetChanged();
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
                        lkfCategory.notifyDataSetChanged();
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
                        kuliahCategory.notifyDataSetChanged();
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
                        kbmCategory.notifyDataSetChanged();
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
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
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
                        panitiaCategory.notifyDataSetChanged();
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

}
