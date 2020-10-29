package com.example.coba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.activity_hasilv.HasilFragment;
import com.example.coba.activity_hasilv.SemuaFragment;
import com.example.coba.activity_home.AboutUsFragment;
import com.example.coba.activity_home.HomeFragment;
import com.example.coba.activity_info.InfoFragment;
import com.example.coba.database.Database;
import com.example.coba.model.ExpandableListView.ExpandableNavigationListView;
import com.example.coba.model.ExpandableListView.Model.HeaderModel;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.example.coba.utils.SessionManajer;
import com.example.mylibrary.Models.UserInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
ExpandableNavigationListView navigationExpandableListView;
DrawerLayout drawer;
NavigationView navigationView;
RelativeLayout signOutDrawer;
TextView userName, email;
Toolbar toolbar;
SessionManajer session;
View navHeader;
    CircleImageView imageView;
String sToken;
    long time;
    String times;

int colorMenu=R.color.colorAccent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationExpandableListView=(ExpandableNavigationListView) findViewById(R.id.expandable_navigation);
        drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        signOutDrawer=(RelativeLayout) findViewById(R.id.signOutDrawer);
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        navHeader=navigationView.inflateHeaderView(R.layout.nav_header_main);
        userName=(TextView)navHeader.findViewById(R.id.nav_username);
        email=(TextView)navHeader.findViewById(R.id.nav_email);
        imageView=(CircleImageView) navHeader.findViewById(R.id.imageHeader);
        imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
        time=System.currentTimeMillis();
        times=String.valueOf(time);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        if (sToken==null || sToken.equals("")){
            Log.d("mainActivity", "no token");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            MainActivity.this.finish();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        cekToken();
        lihatProfil();

        navCon();
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close

        );
        signOutDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen("SIGNOUT");

                //Categories Menu
                //Common.showToast(MainActivity.this, "Categories  Select");
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        displaySelectedScreen("INFO");





    }
    public void navCon(){
        navigationExpandableListView
                .init(this)
                .addHeaderModel(new HeaderModel(R.drawable.ic_info_orange_24dp,"INFO FTEK",colorMenu))
                .addHeaderModel(new HeaderModel(R.mipmap.ic_vote,"VOTING",colorMenu))
                .addHeaderModel(new HeaderModel(R.drawable.ic_archive_orange_24dp,"HASIL VOTING",colorMenu))
                .addHeaderModel(new HeaderModel(R.drawable.ic_priority_high_black_24dp,"ABOUT US",colorMenu))

//                .addHeaderModel(
//                        new HeaderModel(R.drawable.ic_action_attach_money,getString(R.string.transaction), R.drawable.add,colorMenu, true,false, false)
//                                .addChildModel(new ChildModel(getString(R.string.on_going),colorMenu,false))
//                                .addChildModel(new ChildModel(getString(R.string.history),colorMenu,false))
//                )
                .build()
                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition);

                        //drawer.closeDrawer(GravityCompat.START);
                        if (id == 0) {
                            displaySelectedScreen("INFO");

                            drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 1) {
                            displaySelectedScreen("HOME");
                            //Home Menu
                            //Common.showToast(MainActivity.this, "Home Select");
                        } else if (id == 2) {
                            displaySelectedScreen("PROFILE");
                            //Cart Menu
                            //Common.showToast(MainActivity.this, "Cart Select");
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 3) {
                            displaySelectedScreen("ABOUT US");

                            //Categories Menu
                            //Common.showToast(MainActivity.this, "Categories  Select");
                            drawer.closeDrawer(GravityCompat.START);

                        }
                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition, childPosition);

                        if (id == 0) {
                            displaySelectedScreen("ONGOING");
                            //Common.showToast(MainActivity.this, "Man's Fashion");
                        } else if (id == 1) {
                            displaySelectedScreen("HISTORY");
                        }

                        drawer.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });
        navigationExpandableListView.expandGroup(0);
    }
    public void lihatProfil (){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
//                Log.e(" ", response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Cannot login");
                }
                else {
                    try {
                        JSONObject result= response.getJSONObject("result");
                            String imgName=result.getString("pictures");
                            String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_PROFILE)+imgName+"?time="+times;
//                            Log.e(" ",url);
                            Picasso.get()
                                    .load(url)
                                    .placeholder(R.drawable.fotokosong)
                                    .into(imageView);
                            userName.setText(result.getString("full_name"));
                            email.setText(result.getString("email"));
                    }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }

                }
            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.PROFILE),payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"");
    }
    public void cekToken (){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                if(!RestHelper.validateResponse(response)){
                    try {
                        String code = response.getString("code");
                        if (code.equals("1")){
                            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                            ab.setMessage("Your token is expire, please login again");
                            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    UserInfo.deleteAll(new UserInfo(Database.db));
                                    Database.clear();
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(RestUrl.getSubscribtion());
                                    startActivity(new Intent(MainActivity.this,LoginActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            });
                            ab.show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
                else {
                    try {
                        String code = response.getString("code");
                    }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.CHECK_TOKEN),payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"");
    }
    private void displaySelectedScreen(String itemName) {

        //creating fragment object
        Fragment fragment = null;
        Fragment fragmentActive = getSupportFragmentManager().findFragmentById(R.id.frame);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //initializing the fragment object which is selected
        try{
            if(fragmentActive.getTag().equals(itemName)){
                drawer.closeDrawer(GravityCompat.START);
                return;
            }
        }
        catch(NullPointerException ex){
            //ex.printStackTrace();
        }
        switch (itemName) {
            case "INFO":
                fragment = new InfoFragment();
                break;
            case "HOME":
                fragment = new HomeFragment();
                break;
            case "PROFILE":
                fragment = new SemuaFragment();
                break;
            case "ABOUT US":
                fragment = new AboutUsFragment();
                break;
            case "SIGNOUT":
                AlertDialog.Builder ab= new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("Sign Out");
                ab.setMessage("Are you sure?");
                ab.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        UserInfo.deleteAll(new UserInfo(Database.db));
                        Database.clear();
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(RestUrl.getSubscribtion());
                        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        MainActivity.this.finish();
                        MainActivity.this.finishActivity(1);

                    }
                });
                ab.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                   ab.show();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment,itemName);
            ft.addToBackStack(null);
            ft.commit();

            drawer.closeDrawer(GravityCompat.START);
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        displaySelectedScreen(String.valueOf(menuItem.getItemId()));
        return true;
    }
    public static long back_pressed;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(f instanceof InfoFragment){
            if (back_pressed + 2000 > System.currentTimeMillis()){
                FragmentManager fm = this.getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                super.onBackPressed();
            }else {
                Toast.makeText(getBaseContext(),"tekan sekali lagi untuk keluar",Toast.LENGTH_SHORT).show();
                back_pressed=System.currentTimeMillis();
            }
        }
//        else if (f instanceof ProfileFragment){
//            displaySelectedScreen("HOME");
//        }else if (f instanceof OnGoingTransactionFragment){
//            displaySelectedScreen("HOME");
//        }else if (f instanceof HistoryTransactionFragment){
//            displaySelectedScreen("HOME");
//        }
        else{
            super.onBackPressed();
        }
    }

}
