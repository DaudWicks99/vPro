package com.example.coba.activity_home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_home.model.MenuHome;
import com.example.coba.database.Database;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterAllCategory extends RecyclerView.Adapter<AdapterAllCategory.ViewHolder> {
    ArrayList<MenuHome> menuHomes;
    Context context;
    String sToken;
    public AdapterAllCategory(Context context, ArrayList<MenuHome> menuHomes){
        this.menuHomes=menuHomes;
        this.context=context;
        sToken = UserInfos.getFromDatabase(Database.db).token;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_all,parent,false);
        return new ViewHolder(view){};
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.judulAll.setText(menuHomes.get(position).getTitle());
        String url= "http://167.71.199.106:8001/common/uploads/ListMenuPic/low/"+menuHomes.get(position).getUrl();
        Log.e("image",url);
        Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.imageAll);
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
}
