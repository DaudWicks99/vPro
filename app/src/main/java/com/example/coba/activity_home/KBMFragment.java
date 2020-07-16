package com.example.coba.activity_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.AppController;
import com.example.coba.R;
import com.example.coba.RestUrl;
import com.example.coba.VoteActivity;
import com.example.coba.activity_home.adapter.AdapterListMenu;
import com.example.coba.activity_home.adapter.CustomAdapterListItem;
import com.example.coba.activity_home.adapter.CustomAdapterListKbm;
import com.example.coba.activity_home.model.listItem;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KBMFragment extends Fragment {
    public KBMFragment(){
    }
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }
    String[] title={"KBM BASKET"};
    int[] images={R.drawable.basket};
    String[] vote={"Jadwal latihan :, Senin 10-1, jumat 2-5, sabtu 3-6"};
    ListView infoKBM;
    TextView data;
    ArrayList<listItem> transactions=new ArrayList<>();
    CustomAdapterListKbm adapter;
    String sToken;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_kbm, container, false);
        infoKBM=(ListView)view.findViewById(R.id.infoKBM);
        data=(TextView)view.findViewById(R.id.txtNoKbm);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        adapter=new CustomAdapterListKbm(getContext(),transactions,KBMFragment.this);
        infoKBM.setAdapter(adapter);
        infoKBM.setEmptyView(data);
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
                            JSONObject object = item.getJSONObject("listMenu");
                            String group=object.getString("group_id");
                            if (group.equals("1")){
                                listItem transaction = new listItem();
                                transaction.setId(item.getString("id_listMenu"));
                                transaction.setTitle(object.getString("nama"));
                                transaction.setUrl(object.getString("pictures"));
                                transaction.setIdVote(item.getString("id"));
                                transactions.add(transaction);
                            }

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
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.AMBIL_MENU,payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"sda");

        return transactions;
    }
    public void onReload(){
        transactions.clear();
        loadMenu();
    }
}
