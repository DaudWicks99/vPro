package com.example.coba.activity_home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.coba.R;
import com.example.coba.VoteActivity;

public class AdapterListMenu extends BaseAdapter {
    Context context;
    String[] title;
    String[] vote;
    int[] image;
    LayoutInflater inflater;

    public AdapterListMenu(Context context,String[] title,int[] image,String[] vote)
    {
        this.context=context;
        this.title=title;
        this.image=image;
        this.vote=vote;
    }
    @Override
    public int getCount() {return title.length;}

    @Override
    public Object getItem(int i) {return  i;}

    @Override
    public  long getItemId(int i) {return  i;}

    @Override
    public View getView(final int i,  View view, ViewGroup viewGroup)
    {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null){
            view = inflater.inflate(R.layout.model_list_menu, viewGroup, false);
        }
        final String titles= title[i];
        final String votes= vote[i];
        Holders holders=new Holders(view);
        holders.title.setText(title[i]);
        holders.background.setImageResource(image[i]);
        holders.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VoteActivity.class);
                intent.putExtra("title",titles);
                intent.putExtra("vote",votes);
                intent.putExtra("image",image[i]);
                context.startActivity(intent);

            }
        });
        return view;
    }
    public class Holders{
        RelativeLayout layout;
        ImageView background;
        TextView title;

        public Holders(View v){
            layout=(RelativeLayout) v.findViewById(R.id.clickInfo);
            background=(ImageView) v.findViewById(R.id.imgInfo);
            title=(TextView) v.findViewById(R.id.txtV);
        }
    }

}
