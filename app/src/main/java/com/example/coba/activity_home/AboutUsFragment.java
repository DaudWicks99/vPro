package com.example.coba.activity_home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_home.adapter.CustomAdapterListKbm;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutUsFragment extends Fragment {
    public AboutUsFragment(){
    }
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }
    String[] title={"KBM BASKET"};
    int[] images={R.drawable.basket};
    ArrayList<listItem> transactions=new ArrayList<>();
    CustomAdapterListKbm adapter;
    ImageButton Drawer;
    String sToken;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_about_us, container, false);
        adapter=new CustomAdapterListKbm(getContext(),transactions, AboutUsFragment.this);
        Drawer=(ImageButton)view.findViewById(R.id.DrawerAboutUs);
        return view;
    }

}
