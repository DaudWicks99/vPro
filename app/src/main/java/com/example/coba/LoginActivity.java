package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.example.coba.utils.SessionManajer;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {
    EditText usnme;
    EditText uspass;
    FancyButton login;
    ImageButton visibility;
    ImageButton visibilityOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(FancyButton) findViewById(R.id.btnLogin);

        TextView daftar = (TextView) findViewById(R.id.txtSign);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);

            }
        });

        usnme=(EditText)findViewById(R.id.usnme);
        uspass=(EditText)findViewById(R.id.uspass);
        visibility=(ImageButton)findViewById(R.id.visibility);
        visibilityOff=(ImageButton)findViewById(R.id.visibilityOff);

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibility.setVisibility(View.GONE);
                visibilityOff.setVisibility(View.VISIBLE);
                uspass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        visibilityOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibility.setVisibility(View.VISIBLE);
                visibilityOff.setVisibility(View.GONE);
                uspass.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usnme.getText().toString();
                String password = uspass.getText().toString();
                if (password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Tolong isi password terlebih dahulu", Toast.LENGTH_LONG).show();
                }
                else if(username.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Tolong masukkan username terlebih dahulu", Toast.LENGTH_LONG).show();
                }
                else if(password.isEmpty()||username.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Tolong isi password dan username terlebih dahulu", Toast.LENGTH_LONG).show();
                }
                else{
                    loginRest(username, password);
                    }
                }

        });
    }

    public void  loginRest(final String email, String password){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "email", email);
        JsonHelper.put(payload, "pass", password);
        Log.e(" ",email+password);
        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                Toast.makeText(LoginActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Cannot login");
                }
                else {
                    try {
                        UserInfos.getFromDatabase(Database.db)
                                .setToken(response.getString("token"))
                                .setUsername(email)
                                .save();

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        LoginActivity.this.finish();
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.LOGIN,payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"");
    }
}
