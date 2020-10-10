package com.example.coba.activity_info.adapterr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.DetailInfoActivity;
import com.example.coba.EditInfoActivity;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_info.InfoFragment;
import com.example.coba.activity_info.model.InfoMenu;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class CustomAdapterListItemInfo extends BaseAdapter {
    LayoutInflater inflater;
    Context ctx;
    String sToken;
    ArrayList<InfoMenu> items;
    ArrayList<InfoMenu> filterList;
    InfoFragment fragment;
    long time;
    String times;

    public CustomAdapterListItemInfo(Context c,ArrayList<InfoMenu> items,InfoFragment AllFragment){
        this.ctx= c;
        this.items=items;
        this.fragment=AllFragment;
        sToken = UserInfos.getFromDatabase(Database.db).token;
        time=System.currentTimeMillis();
        times=String.valueOf(time);

    }

    public void reload(ArrayList<InfoMenu> a){
        this.filterList= (ArrayList<InfoMenu>) a.clone();
        this.items= (ArrayList<InfoMenu>) a.clone();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater==null){
            inflater=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view==null){
            view=inflater.inflate(R.layout.model_list_menu_info,viewGroup, false);
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(300,0,0,0);
        Animation alphaAnimation = new AlphaAnimation(0,1);
        translateAnimation.setDuration(500);
        alphaAnimation.setDuration(1300);
        AnimationSet animation= new AnimationSet(true);
        animation.addAnimation(translateAnimation);
        animation.addAnimation(alphaAnimation);
        view.setAnimation(animation);
        String rawUrl=items.get(i).getUrl();
        final String id=items.get(i).getId();
        String url= RestUrl.getImgBase(RestUrl.IMAGE_URL_INFO)+rawUrl+"?time="+times;
        Holders holders=new Holders(view);
        holders.title.setSelected(true);
        holders.title.setText(items.get(i).getTitle());
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(holders.background);
        holders.editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, EditInfoActivity.class);
                intent.putExtra("id",id);
                ctx.startActivity(intent);

            }
        });

        holders.showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, DetailInfoActivity.class);
                intent.putExtra("id",id);
                ctx.startActivity(intent);
            }
        });

        holders.hapusInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab= new AlertDialog.Builder(ctx);
                ab.setTitle("Delete Info");
                ab.setMessage("Are you sure?");
                ab.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteInfo(id);

                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                ab.show();


            }
        });

        checkAdmin(holders.menuInfoFtek);
        holders.title.setText(items.get(i).getTitle());
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holders.background);
        return view;
    }
    public void checkAdmin(final LinearLayout menu){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!RestHelper.validateResponse(response)){
                    Log.w("adapter", "invalid response");
                    return;
                }else {
                    try {
                        String mag=response.getString("msg");
                        Log.e("pesan",mag);
                        if (mag.equals("You are admin")){
                            menu.setVisibility(View.VISIBLE);

                        }else {
                            menu.setVisibility(View.GONE);

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



    public void deleteInfo(String id){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token",sToken);
        JsonHelper.put(payload, "id",id);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(!RestHelper.validateResponse(response)) {
                    Log.w("adapter", "invalid response");
                }else {
                    fragment.onReload();
                    Log.w("adapter", "success");
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(null,null);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.DELETE_INFO),payload,successResp,errorResp);
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }



    public class Holders{
        RelativeLayout layout;
        ImageView background;
        TextView title;
        ImageView editInfo;
        ImageView hapusInfo;
        ImageView shareInfo;
        LinearLayout menuInfoFtek;
        FancyButton showInfo;



        public Holders(View v){
            layout=(RelativeLayout) v.findViewById(R.id.clickInfo);
            background=(ImageView) v.findViewById(R.id.imageInfo);
            title=(TextView) v.findViewById(R.id.txtVie);
            editInfo=(ImageView) v.findViewById(R.id.editInfo);
            hapusInfo=(ImageView) v.findViewById(R.id.hapusInfo);
            shareInfo=(ImageView) v.findViewById(R.id.shareInfo);
            menuInfoFtek=(LinearLayout) v.findViewById(R.id.menuInfoFtek);
            showInfo=(FancyButton) v.findViewById(R.id.btnShowInfo);

        }
    }

}
