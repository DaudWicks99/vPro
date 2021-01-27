package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.chaos.view.PinView;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class ValidationActivity extends AppCompatActivity {
    PinView code;
    FancyButton enter, resend;
    ProgressDialog spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        final String token = getIntent().getStringExtra("token_id");
        spinner=new ProgressDialog(ValidationActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        code=(PinView) findViewById(R.id.code_validation);
        enter=(FancyButton)findViewById(R.id.btn_validation);
        resend=(FancyButton)findViewById(R.id.btn_resend_forgot);
        resend.setEnabled(false);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimUlang(token);
            }
        });
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                resend.setText("RESEND: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                resend.setEnabled(true);
                resend.setText("RESEND");
            }
        }.start();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codee=code.getText().toString();
                if (codee.isEmpty()){
                    Toast.makeText(ValidationActivity.this,"Please enter code",Toast.LENGTH_LONG).show();
                }else {
                    validation(codee,token);
                }
            }
        });
    }
    public void kirimUlang(String token){
        resend.setEnabled(false);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                resend.setText("RESEND: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                resend.setEnabled(true);
                resend.setText("RESEND");
            }
        }.start();
        JSONObject payload= new JSONObject();
        JsonHelper.put(payload,"token",token);
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                spinner.dismiss();
                JSONObject object=response;
                try {
                    String code=object.getString("code");
                    if (code.equals("0")){

                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("",spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.REUPLOAD_VALIDASI_CODE),payload,successResp,errorResp);
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        spinner.setMessage("Upload....");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }

    public void validation(String code, String token){
        JSONObject payload= new JSONObject();
        JsonHelper.put(payload,"token",token);
        JsonHelper.put(payload,"kode",code);
        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                spinner.dismiss();
                JSONObject object=response;
                try {
                    String code=object.getString("code");
                    if (code.equals("0")){
                        String email = response.getString("email");
                        Intent i = new Intent(ValidationActivity.this, EnterPasswordActivity.class);
                        i.putExtra("email_id", email);
                        startActivity(i);
                        ValidationActivity.this.finish();
                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("",spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.CHECK_VALIDASI),payload,successResp,errorResp);
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        spinner.setMessage("Upload....");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }
}