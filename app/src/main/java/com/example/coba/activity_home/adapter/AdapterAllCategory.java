package com.example.coba.activity_home.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.EditVotingActivity;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_home.AllFragment;
import com.example.coba.activity_home.HomeFragment;
import com.example.coba.activity_home.VoteActivity;
import com.example.coba.activity_home.model.MenuHome;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterAllCategory extends RecyclerView.Adapter<AdapterAllCategory.ViewHolder> {
    ArrayList<MenuHome> menuHomes;
    Context context;
    String sToken;
    HomeFragment fragment;
    public AdapterAllCategory(Context context, ArrayList<MenuHome> menuHomes, HomeFragment fragment){
        this.menuHomes=menuHomes;
        this.context=context;
        this.fragment=fragment;
        sToken = UserInfos.getFromDatabase(Database.db).token;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_all,parent,false);
        TranslateAnimation translateAnimation = new TranslateAnimation(300,0,0,0);
        Animation alphaAnimation = new AlphaAnimation(0,1);
        translateAnimation.setDuration(500);
        alphaAnimation.setDuration(1300);
        AnimationSet animation= new AnimationSet(true);
        animation.addAnimation(translateAnimation);
        animation.addAnimation(alphaAnimation);
        view.setAnimation(animation);
        return new ViewHolder(view){};
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String id=menuHomes.get(position).getId();
        final String idVote=menuHomes.get(position).getIdVote();
        holder.judulAll.setText(menuHomes.get(position).getTitle());
        String url= RestUrl.getImgBase(RestUrl.IMAGE_URL_VOTING)+menuHomes.get(position).getUrl();
        Log.e("image",url);
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.imageAll);
        holder.judulAll.setSelected(true);
        holder.imageAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VoteActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
        checkAdmin(holder.editAll, holder.deleteAll);
        holder.deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab= new AlertDialog.Builder(context);
                ab.setTitle("Delete Info");
                ab.setMessage("Are you sure?");
                ab.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteMenu(id,idVote);

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
        holder.editAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EditVotingActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return menuHomes.size();
    }
    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageAll,shareAll,editAll,deleteAll;
        TextView judulAll;
        public ViewHolder(View itemView){
            super(itemView);
            imageAll=(ImageView)itemView.findViewById(R.id.imageAll);
            shareAll=(ImageView)itemView.findViewById(R.id.shareAll);
            editAll=(ImageView)itemView.findViewById(R.id.editAll);
            deleteAll=(ImageView)itemView.findViewById(R.id.deleteAll);
            judulAll=(TextView)itemView.findViewById(R.id.judulAll);
        }

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
                    fragment.onReload();
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
}
