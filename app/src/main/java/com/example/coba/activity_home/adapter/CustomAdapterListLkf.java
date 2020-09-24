package com.example.coba.activity_home.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.EditVotingActivity;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_home.VoteActivity;
import com.example.coba.activity_home.SMFFragment;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomAdapterListLkf extends BaseAdapter {
    LayoutInflater inflater;
    Context ctx;
    ArrayList<listItem> items;
    ArrayList<listItem> filterList;
    SMFFragment fragment;

    String sToken;


    public CustomAdapterListLkf(Context c, ArrayList<listItem> items, SMFFragment fragment){
        this.ctx= c;
        this.items=items;
        this.fragment=fragment;
        sToken = UserInfos.getFromDatabase(Database.db).token;

    }

    public void reload(ArrayList<listItem> a){
        this.filterList= (ArrayList<listItem>) a.clone();
        this.items= (ArrayList<listItem>) a.clone();
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
            view=inflater.inflate(R.layout.model_list_menu,viewGroup, false);
        }
        String rawUrl=items.get(i).getUrl();
        final String id=items.get(i).getId();
        final String idVote=items.get(i).getIdVote();
        String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_VOTING)+rawUrl;
        Log.e("token",sToken);
        Holders holders=new Holders(view);
        holders.hapusVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMenu(id,idVote);
            }
        });
        holders.editVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, EditVotingActivity.class);
                intent.putExtra("id",id);
                ctx.startActivity(intent);

            }
        });
        holders.shareVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your body is here";
                String shareSub = "Your subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                ctx.startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });
        holders.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, VoteActivity.class);
                intent.putExtra("id",id);
                ctx.startActivity(intent);
            }
        });

        checkAdmin(holders.editVoting,holders.hapusVoting);
        holders.title.setText(items.get(i).getTitle());
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holders.background);

        return view;
    }

    public void checkAdmin(final ImageView edit, final ImageView delete){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());
                if (!RestHelper.validateResponse(response)){
                    Log.w("adapter", "invalid response");
                    return;
                }else {
                    try {
                        String mag=response.getString("msg");
                        Log.e("pesan",mag);
                        if (mag.equals("You are admin")){
                            edit.setVisibility(View.VISIBLE);
                            delete.setVisibility(View.VISIBLE);
                        }else {
                            edit.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(null,null);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.CHECK_ADMIN,payload,successResp,errorResp);
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }
    public void deleteMenu(String id, final String idVote){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token",sToken);
        JsonHelper.put(payload, "idList",id);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(!RestHelper.validateResponse(response)) {
                    Log.w("adapter", "invalid response");
                }else {
                    deleteVote(idVote);
                    fragment.onReload();
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(null,null);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.DELETE_LIST_MENU,payload,successResp,errorResp);
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }

    public void deleteVote(String id){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token",sToken);
        JsonHelper.put(payload, "idVote",id);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(!RestHelper.validateResponse(response)) {
                    Log.w("adapter", "invalid response");
                }else {
                    Log.w("adapter", "success");
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(null,null);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.DELETE_VOTE,payload,successResp,errorResp);
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }





    public class Holders{
        RelativeLayout layout;
        ImageView background;
        TextView title;
        ImageView editVoting;
        ImageView hapusVoting;
        ImageView shareVoting;

        public Holders(View v){
            layout=(RelativeLayout) v.findViewById(R.id.clickInfo);
            background=(ImageView) v.findViewById(R.id.imgInfo);
            title=(TextView) v.findViewById(R.id.txtV);
            editVoting=(ImageView)v.findViewById(R.id.editVoting);
            hapusVoting=(ImageView)v.findViewById(R.id.hapusVoting);
            shareVoting=(ImageView)v.findViewById(R.id.shareVoting);
        }
    }
}
