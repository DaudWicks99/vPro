package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class EnterPasswordActivity extends AppCompatActivity {
    EditText password1, password2;
    FancyButton button;
    ProgressDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        final String email = getIntent().getStringExtra("email_id");
        spinner=new ProgressDialog(EnterPasswordActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        password1=(EditText)findViewById(R.id.input_new_password);
        password2=(EditText)findViewById(R.id.input_second_new_password);
        button=(FancyButton)findViewById(R.id.btn_new_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1=password1.getText().toString();
                String pass2=password2.getText().toString();

                    enterPassword(email,pass1);

            }
        });
    }
    public void enterPassword(String email, String newPass){
        String pass1=password1.getText().toString();
        String pass2=password2.getText().toString();
        if (!pass1.isEmpty()){
            if (!pass2.isEmpty()){
                if (pass1.equals(pass2)){
                    JSONObject payload= new JSONObject();
                    JsonHelper.put(payload,"email",email);
                    JsonHelper.put(payload,"newPass",newPass);
                    Response.Listener successResp = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            spinner.dismiss();
                            JSONObject object=response;
                            try {
                                String code =object.getString("code");
                                if (code.equals("0")){
                                    Intent i = new Intent(EnterPasswordActivity.this, SuccessActivity.class);
                                    startActivity(i);
                                    EnterPasswordActivity.this.finish();
                                }
                            }catch (JSONException ex){
                                ex.printStackTrace();
                            }
                        }
                    };
                    Response.ErrorListener errorResp = RestHelper.generalErrorResponse("",spinner);
                    JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.FORGOT_PASSWORD),payload,successResp,errorResp);
                    myReq.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
                    spinner.setMessage("Upload....");
                    spinner.show();
                    AppController.getRest().addToReqq(myReq,"");
                }else {
                    Toast.makeText(EnterPasswordActivity.this,"Password not match!",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(EnterPasswordActivity.this,"Please input Confrim New Password",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(EnterPasswordActivity.this,"Please input New Password",Toast.LENGTH_LONG).show();
        }
    }
}