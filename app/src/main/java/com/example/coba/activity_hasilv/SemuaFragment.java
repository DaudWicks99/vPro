package com.example.coba.activity_hasilv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;

public class SemuaFragment extends Fragment {
    public SemuaFragment(){
    }
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }
    ArrayList<listItem>transactions=new ArrayList<>();
    CustomAdapterListHasil adapter;
    String sToken;
    ListView HasilVoting;
    ImageButton opDrwSmua;
    DrawerLayout drawerLayout;
    RelativeLayout animAll, animLkf, animKuliah, animKbm, animPanitia;
    CircleImageView All,Lkf,Kuliah,Kbm,Panitia;
    TextView itemAll,itemLkf,itemKuliah,itemKbm,itemPanitia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_semua, container, false);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        opDrwSmua=(ImageButton) view.findViewById(R.id.opDrwSmua);
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        opDrwSmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        animAll=(RelativeLayout)view.findViewById(R.id.RlSemuaAll);
        animLkf=(RelativeLayout)view.findViewById(R.id.RlSemuaLkf);
        animKuliah=(RelativeLayout)view.findViewById(R.id.RlSemuaKuliah);
        animKbm=(RelativeLayout)view.findViewById(R.id.RlSemuaKbm);
        animPanitia=(RelativeLayout)view.findViewById(R.id.RlSemuaPanitia);
        All=(CircleImageView)view.findViewById(R.id.CiAll);
        Lkf=(CircleImageView)view.findViewById(R.id.CiLkf);
        Kuliah=(CircleImageView)view.findViewById(R.id.CiKuliah);
        Kbm=(CircleImageView)view.findViewById(R.id.CiKbm);
        Panitia=(CircleImageView)view.findViewById(R.id.CiPanitia);
        itemAll=(TextView)view.findViewById(R.id.itemAllMenuHasil);
        itemLkf=(TextView)view.findViewById(R.id.itemLkfMenuHasil);
        itemKuliah=(TextView)view.findViewById(R.id.itemKuliahMenuHasil);
        itemKbm=(TextView)view.findViewById(R.id.itemKbmMenuHasil);
        itemPanitia=(TextView)view.findViewById(R.id.itemPanitiaMenuHasil);
        All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HasilVotingActivity.class);
                intent.putExtra("id","5");
                startActivity(intent);
            }
        });
        Lkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HasilVotingActivity.class);
                intent.putExtra("id","2");
                startActivity(intent);
            }
        });
        Kuliah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HasilVotingActivity.class);
                intent.putExtra("id","3");
                startActivity(intent);
            }
        });
        Kbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HasilVotingActivity.class);
                intent.putExtra("id","1");
                startActivity(intent);
            }
        });
        Panitia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HasilVotingActivity.class);
                intent.putExtra("id","4");
                startActivity(intent);
            }
        });
        TranslateAnimation translateAnimation = new TranslateAnimation(300,0,0,0);
        Animation alphaAnimation = new AlphaAnimation(0,1);
        translateAnimation.setDuration(500);
        alphaAnimation.setDuration(1300);
        TranslateAnimation translateAnimation1 = new TranslateAnimation(300,0,0,0);
        Animation alphaAnimation1 = new AlphaAnimation(0,1);
        translateAnimation1.setDuration(500);
        alphaAnimation1.setDuration(1300);
        TranslateAnimation translateAnimation2 = new TranslateAnimation(300,0,0,0);
        Animation alphaAnimation2 = new AlphaAnimation(0,1);
        translateAnimation2.setDuration(500);
        alphaAnimation2.setDuration(1300);
        TranslateAnimation translateAnimation3 = new TranslateAnimation(300,0,0,0);
        Animation alphaAnimation3 = new AlphaAnimation(0,1);
        translateAnimation3.setDuration(500);
        alphaAnimation3.setDuration(1300);
        TranslateAnimation translateAnimation4 = new TranslateAnimation(300,0,0,0);
        Animation alphaAnimation4 = new AlphaAnimation(0,1);
        translateAnimation4.setDuration(500);
        alphaAnimation4.setDuration(1300);
        AnimationSet animation= new AnimationSet(true);
        animation.addAnimation(translateAnimation);
        animation.addAnimation(alphaAnimation);
        AnimationSet animation1= new AnimationSet(true);
        animation1.addAnimation(translateAnimation1);
        animation1.addAnimation(alphaAnimation1);
        AnimationSet animation2= new AnimationSet(true);
        animation2.addAnimation(translateAnimation2);
        animation2.addAnimation(alphaAnimation2);
        AnimationSet animation3= new AnimationSet(true);
        animation3.addAnimation(translateAnimation3);
        animation3.addAnimation(alphaAnimation3);
        AnimationSet animation4= new AnimationSet(true);
        animation4.addAnimation(translateAnimation4);
        animation4.addAnimation(alphaAnimation4);
        animAll.setAnimation(animation);
        animLkf.setAnimation(animation1);
        animKuliah.setAnimation(animation2);
        animKbm.setAnimation(animation3);
        animPanitia.setAnimation(animation4);
//        adapter=new CustomAdapterListHasil(getContext(),transactions);
//        HasilVoting.setAdapter(adapter);
//        transactions=loadMenu();
        loadMenu();
        loadMenu2();
        loadMenu3();
        loadMenu4();
        loadMenu5();
        return view;
    }
    public void loadMenu() {
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", '5');
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("hasil", response.toString());
                if (!RestHelper.validateResponse(response)) {
                    Log.w("jashkbfjas", "Not a valid response");
                    return;
                } else {
                    try {
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                        itemAll.setText(results.length()+" "+"items");
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
    public void loadMenu2() {
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", '3');
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("hasil", response.toString());
                if (!RestHelper.validateResponse(response)) {
                    Log.w("jashkbfjas", "Not a valid response");
                    return;
                } else {
                    try {
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                        itemKuliah.setText(results.length()+" "+"items");
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
    public void loadMenu3() {
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", '2');
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("hasil", response.toString());
                if (!RestHelper.validateResponse(response)) {
                    Log.w("jashkbfjas", "Not a valid response");
                    return;
                } else {
                    try {
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                        itemLkf.setText(results.length()+" "+"items");
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
    public void loadMenu4() {
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", '4');
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("hasil", response.toString());
                if (!RestHelper.validateResponse(response)) {
                    Log.w("jashkbfjas", "Not a valid response");
                    return;
                } else {
                    try {
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                        itemPanitia.setText(results.length()+" "+"items");
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
    public void loadMenu5() {
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        JsonHelper.put(payload, "groupId", '1');
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("hasil", response.toString());
                if (!RestHelper.validateResponse(response)) {
                    Log.w("jashkbfjas", "Not a valid response");
                    return;
                } else {
                    try {
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                        itemKbm.setText(results.length()+" "+"items");
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
}
