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

public class AdapterKuliahCategory extends RecyclerView.Adapter<AdapterKuliahCategory.ViewHolder>{
    ArrayList<MenuHome> menuHomes;
    Context context;
    String sToken;
    public AdapterKuliahCategory(Context context, ArrayList<MenuHome> menuHomes){
        this.menuHomes=menuHomes;
        this.context=context;
        sToken = UserInfos.getFromDatabase(Database.db).token;

    }
    @NonNull
    @Override
    public AdapterKuliahCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_kuliah,parent,false);
        return new AdapterKuliahCategory.ViewHolder(view){};
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String id=menuHomes.get(position).getId();
        holder.judulKULIAH.setText(menuHomes.get(position).getTitle());
        String url= "http://167.71.199.106:8001/common/uploads/ListMenuPic/low/"+menuHomes.get(position).getUrl();
        Log.e("image",url);
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.imageKULIAH);
        holder.imageKULIAH.setOnClickListener(new View.OnClickListener() {
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
        ImageView imageKULIAH,shareKULIAH,editKULIAH,deleteKULIAH;
        TextView judulKULIAH;
        public ViewHolder(View itemView){
            super(itemView);
            imageKULIAH=(ImageView)itemView.findViewById(R.id.imageKULIAH);
            shareKULIAH=(ImageView)itemView.findViewById(R.id.shareKULIAH);
            editKULIAH=(ImageView)itemView.findViewById(R.id.editKULIAH);
            deleteKULIAH=(ImageView)itemView.findViewById(R.id.deleteKULIAH);
            judulKULIAH=(TextView)itemView.findViewById(R.id.judulKULIAH);
        }

    }
}
