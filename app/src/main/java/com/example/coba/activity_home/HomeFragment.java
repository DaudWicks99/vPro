package com.example.coba.activity_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.coba.AddVotingActivity;
import com.example.coba.R;
import com.example.coba.activity_home.adapter.ViewPagerAdapter;
import com.example.coba.database.Database;
import com.example.coba.model.activerecords.UserInfos;
import com.example.coba.utils.SessionManajer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    public HomeFragment(){

    }
    @Override
    public void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public final static String TAG="Home";
    ImageButton openDrawer;
    ViewPager viewPager;
    TabLayout tablayout;
    ViewPagerAdapter adapter;
    DrawerLayout drawerLayout;
    FloatingActionButton floatingactionbutton;
    String sToken;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        openDrawer=(ImageButton) view.findViewById(R.id.opDrawer);
        viewPager= (ViewPager) view.findViewById(R.id.vyPager);
        tablayout=(TabLayout) view.findViewById(R.id.tabLayout);
        floatingactionbutton=(FloatingActionButton) view.findViewById(R.id.plus);
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setViewPager(viewPager);
        floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddVotingActivity.class);
                startActivity(intent);
            }
        });
        tablayout.setupWithViewPager(viewPager);
        return view;

    }
    public void setViewPager(ViewPager viewPager){
        adapter=new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFlag(new AllFragment(),"ALL");
        adapter.addFlag(new KBMFragment(),"KBM");
        adapter.addFlag(new SMFFragment(),"LKF");
        adapter.addFlag(new BPMFFragment(),"KULIAH");
        adapter.addFlag(new KEPANITIAANFragment(),"KEPANITIAAN");
        viewPager.setAdapter(adapter);
    }


}
