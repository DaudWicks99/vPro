package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email;
    FancyButton button;
    ProgressDialog spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email=(EditText)findViewById(R.id.email_forgot_password);
        spinner=new ProgressDialog(ForgotPasswordActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        button=(FancyButton)findViewById(R.id.btn_forgot_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=email.getText().toString();
                if (comment.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
                }else {
                    loginRest(comment);
                }
            }
        });
    }
    public void  loginRest(final String email){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "email", email);
        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                spinner.dismiss();
                Log.e("response", response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Cannot login");
                    try {
                        String msg = response.getString("msg");
                            Toast.makeText(ForgotPasswordActivity.this,msg, Toast.LENGTH_LONG).show();
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        String token = response.getString("token");
                        Intent intent = new Intent(getBaseContext(), ValidationActivity.class);
                        intent.putExtra("token_id", token);
                        startActivity(intent);
                        ForgotPasswordActivity.this.finish();
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.SEARCH_EMAIL),payload,successResp,errorResp);
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        spinner.setMessage("Loading.....");
        spinner.show();
        AppController.getRest().addToReqq(myReq," ");
    }
}