package com.example.coba.activity_hasilv.adaptor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coba.activity_home.DetailHasilVoteActivity;
import com.example.coba.R;
import com.example.coba.activity_hasilv.SemuaFragment;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterListHasil extends BaseAdapter {
    LayoutInflater inflater;
    Context ctx;
    ArrayList<listItem> items;
    ArrayList<listItem> filterList;
    SemuaFragment fragment;

    String sToken;



    public CustomAdapterListHasil(Context c,ArrayList<listItem> items){
        this.ctx= c;
        this.items=items;
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
            view=inflater.inflate(R.layout.model_list_menu_hasil_voting,viewGroup, false);
        }
        String rawUrl=items.get(i).getUrl();
        String url="http://167.71.199.106:8001/common/uploads/ListMenuPic/low/"+rawUrl;
        final String id=items.get(i).getId();
        Log.e("token",sToken);
        Holders holders=new Holders(view);
        holders.title.setText(items.get(i).getTitle());
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(holders.background);
        holders.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, DetailHasilVoteActivity.class);
                intent.putExtra("id",id);
                ctx.startActivity(intent);
            }
        });

        return view;
    }
    public class Holders{
        ImageView background;
        TextView title;


        public Holders(View v){
            background=(ImageView) v.findViewById(R.id.imgHasilVoting);
            title=(TextView) v.findViewById(R.id.descHasilVote);

        }
    }


}
