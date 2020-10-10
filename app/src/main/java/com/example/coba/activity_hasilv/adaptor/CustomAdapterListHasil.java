package com.example.coba.activity_hasilv.adaptor;

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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.EditVotingActivity;
import com.example.coba.RestUrl;
import com.example.coba.activity_hasilv.HasilVotingActivity;
import com.example.coba.activity_hasilv.model.ListHasil;
import com.example.coba.activity_home.DetailHasilVoteActivity;
import com.example.coba.R;
import com.example.coba.activity_hasilv.SemuaFragment;
import com.example.coba.activity_home.ShowAllActivity;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomAdapterListHasil extends BaseAdapter {
    LayoutInflater inflater;
    Context ctx;
    ArrayList<ListHasil> items;
    ArrayList<ListHasil> filterList;
    HasilVotingActivity activity;

    String sToken;



    public CustomAdapterListHasil(Context c, ArrayList<ListHasil> items, HasilVotingActivity activity){
        this.ctx= c;
        this.items=items;
        this.activity=activity;
        sToken = UserInfos.getFromDatabase(Database.db).token;

    }

    public void reload(ArrayList<ListHasil> a){
        this.filterList= (ArrayList<ListHasil>) a.clone();
        this.items= (ArrayList<ListHasil>) a.clone();
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
            view=inflater.inflate(R.layout.model_list_show_all,viewGroup, false);
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

        String url= RestUrl.getImgBase(RestUrl.IMAGE_URL_VOTING)+rawUrl;
        Log.e("token",sToken);
        Holders holders=new Holders(view);
        holders.title.setSelected(true);
        holders.hapusVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab= new AlertDialog.Builder(ctx);
                ab.setTitle("Delete Info");
                ab.setMessage("Are you sure?");
                ab.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //deleteMenu(id);

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
        holders.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, DetailHasilVoteActivity.class);
                intent.putExtra("id",id);
                ctx.startActivity(intent);
            }
        });
        //checkAdmin(holders.editVoting,holders.hapusVoting);
        holders.title.setText(items.get(i).getNama());
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
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.CHECK_ADMIN),payload,successResp,errorResp);
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
                    activity.onReload();
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(null,null);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.DELETE_LIST_MENU),payload,successResp,errorResp);
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
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.DELETE_VOTE),payload,successResp,errorResp);
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }
    public class Holders{
        ImageView background;
        TextView title;
        ImageView editVoting;
        ImageView hapusVoting;
        ImageView shareVoting;


        public Holders(View v){
            background=(ImageView) v.findViewById(R.id.imageShowAll);
            title=(TextView) v.findViewById(R.id.jdlShowAll);
            editVoting=(ImageView)v.findViewById(R.id.editShowAll);
            hapusVoting=(ImageView)v.findViewById(R.id.hapusShowAll);
            shareVoting=(ImageView)v.findViewById(R.id.shareShowAll);

        }
    }


}
