package com.example.coba.activity_hasilv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.coba.R;
import com.example.coba.activity_home.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class HasilFragment extends Fragment {
    public HasilFragment(){

    }
    @Override
    public void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    ImageButton openDrawer;
    ViewPager viewPager;
    TabLayout tablayout;
    ViewPagerAdapter adaptor;
    DrawerLayout drawerLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_hasil,container,false);
        openDrawer=(ImageButton) view.findViewById(R.id.opDrw);
        viewPager= (ViewPager) view.findViewById(R.id.vyPgr);
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setViewPager(viewPager);
        return view;

    }
    public void setViewPager(ViewPager viewPager){
        adaptor=new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adaptor.addFlag(new SemuaFragment(),"ALL");
        viewPager.setAdapter(adaptor);
    }
}