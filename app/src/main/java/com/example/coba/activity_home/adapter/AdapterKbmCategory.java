package com.example.coba.activity_home.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coba.R;
import com.example.coba.activity_home.VoteActivity;
import com.example.coba.activity_home.model.MenuHome;
import com.example.coba.database.Database;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterKbmCategory extends RecyclerView.Adapter<AdapterKbmCategory.ViewHolder> {
    ArrayList<MenuHome> menuHomes;
    Context context;
    String sToken;
    public AdapterKbmCategory(Context context, ArrayList<MenuHome> menuHomes){
        this.menuHomes=menuHomes;
        this.context=context;
        sToken = UserInfos.getFromDatabase(Database.db).token;

    }
    @NonNull
    @Override
    public AdapterKbmCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_kbm,parent,false);
        return new AdapterKbmCategory.ViewHolder(view){};
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String id=menuHomes.get(position).getId();
        holder.judulKBM.setText(menuHomes.get(position).getTitle());
        String url= "http://167.71.199.106:8001/common/uploads/ListMenuPic/low/"+menuHomes.get(position).getUrl();
        Log.e("image",url);
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.imageKBM);
        holder.imageKBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VoteActivity.class);
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
        ImageView imageKBM,shareKBM,editKBM,deleteKBM;
        TextView judulKBM;
        public ViewHolder(View itemView){
            super(itemView);
            imageKBM=(ImageView)itemView.findViewById(R.id.imageKBM);
            shareKBM=(ImageView)itemView.findViewById(R.id.shareKBM);
            editKBM=(ImageView)itemView.findViewById(R.id.editKBM);
            deleteKBM=(ImageView)itemView.findViewById(R.id.deleteKBM);
            judulKBM=(TextView)itemView.findViewById(R.id.judulKBM);
        }

    }
}
