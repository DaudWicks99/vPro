package com.example.coba.activity_info.adapterr;

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

import com.example.coba.DetailInfoActivity;
import com.example.coba.R;

public class AdapterrListMenu extends BaseAdapter {
    Context context;
    String[] title;
    String[] vote;
    int[] image;
    LayoutInflater inflater;

    public AdapterrListMenu(Context context,String[] title,int[] image,String[] vote)
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
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null){
            view = inflater.inflate(R.layout.model_list_menu_info, viewGroup, false);
        }
        final String titles= title[i];
        final String votes= vote[i];
        int images=image[i];

        AdapterrListMenu.Holderss holders=new AdapterrListMenu.Holderss(view);
        if(images==1){
            holders.layout.setVisibility(View.VISIBLE);
            holders.layout1.setVisibility(View.GONE);
            holders.title1.setText(titles);
            holders.title2.setText(vote[i]);
            holders.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DetailInfoActivity.class);
                    intent.putExtra("title",titles);
                    intent.putExtra("vote",votes);
                    intent.putExtra("image",image[i]);
                    context.startActivity(intent);

                }
            });
        }
        else {
            holders.layout1.setVisibility(View.VISIBLE);
            holders.layout.setVisibility(View.GONE);
            holders.title.setText(titles);
            holders.background.setImageResource(image[i]);
            holders.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DetailInfoActivity.class);
                    intent.putExtra("title",titles);
                    intent.putExtra("vote",votes);
                    intent.putExtra("image",image[i]);
                    context.startActivity(intent);

                }
            });

        }


        return view;
    }
    public class Holderss{
        CardView layout;
        RelativeLayout layout1;

        ImageView background;
        TextView title;
        TextView title1;
        TextView title2;

        public Holderss(View v){
            layout=(CardView) v.findViewById(R.id.info);
            background=(ImageView) v.findViewById(R.id.imageInfo);
            title=(TextView) v.findViewById(R.id.txtVie);


        }
    }
}
