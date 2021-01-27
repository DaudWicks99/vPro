package com.example.coba.activity_home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
    DrawerLayout drawerLayout;
    String sToken;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_about_us, container, false);
        adapter=new CustomAdapterListKbm(getContext(),transactions, AboutUsFragment.this);
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//         sample code snippet to set the text content on the ExpandableTextView
        ExpandableTextView expTv1 = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        ExpandableTextView expTv2 = (ExpandableTextView) view.findViewById(R.id.expand_text_view2);
        ExpandableTextView expTv3 = (ExpandableTextView) view.findViewById(R.id.expand_text_view3);
//         IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expTv1.setText(getString(R.string.dummy_text1));
        expTv2.setText(getString(R.string.dummy_text2));
        expTv3.setText(getString(R.string.dummy_text3));
        Drawer=(ImageButton)view.findViewById(R.id.DrawerAboutUs);
        Drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        return view;
    }

}
