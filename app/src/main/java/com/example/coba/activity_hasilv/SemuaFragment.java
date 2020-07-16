package com.example.coba.activity_hasilv;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.activity_hasilv.adaptor.CustomAdapterListHasil;
import com.example.coba.activity_home.adapter.CustomAdapterListItem;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SemuaFragment extends Fragment {
    public SemuaFragment(){
    }
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }
    ArrayList<listItem>transactions=new ArrayList<>();
    CustomAdapterListHasil adapter;
    String sToken;
    ListView HasilVoting;
    ImageButton opDrwSmua;
    TextView txtVw2;
    DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_semua, container, false);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        HasilVoting=(ListView)view.findViewById(R.id.HasilVoting);
        opDrwSmua=(ImageButton) view.findViewById(R.id.opDrwSmua);
        drawerLayout=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        opDrwSmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        adapter=new CustomAdapterListHasil(getContext(),transactions);
        HasilVoting.setAdapter(adapter);
        transactions=loadMenu();
        return view;
    }
    public ArrayList<listItem> loadMenu(){
        transactions.clear();
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "token", sToken);
        Response.Listener successResp= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("namama",response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w("jashkbfjas","Not a valid response" );
                    return;
                }
                else {
                    try{
                        JSONArray results = response.getJSONArray("result");
                        transactions.clear();
                        for (int i = 0; i < results.length(); i++){
                            JSONObject item = results.getJSONObject(i);
                            listItem transaction = new listItem();
                            transaction.setId(item.getString("id"));
                            transaction.setTitle(item.getString("nama"));
                            transaction.setUrl(item.getString("pictures"));
                            transaction.setIdVote(item.getString("id"));
                            transactions.add(transaction);
                        }
                        adapter.reload(transactions);
                        adapter.notifyDataSetChanged();
                    }catch (JSONException ex){
                        Log.w("dsadsa", "not a valid object");
                    }
                }


            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("sdad", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.AMBIL_LIST_HASIL,payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"sda");

        return transactions;
    }
}
