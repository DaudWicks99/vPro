package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.example.coba.utils.SessionManajer;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class SignupActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText fullname;
    EditText email;
    EditText confirmPassword;
    ImageButton mataOn, mataOff, mataOn2, mataOff2;
    FancyButton btnSignUp;
    SessionManajer session;
    ImageButton signUpBack;
    MaterialSpinner kategori;
    String mUname,mFname,mEmail,mPass,mPassCon,mKategori;
    String role=" ";
    ProgressDialog spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        spinner=new ProgressDialog(SignupActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        fullname=(EditText)findViewById(R.id.fullname);
        email=(EditText)findViewById(R.id.email);
        kategori=(MaterialSpinner)findViewById(R.id.kategoriSignUp);
        confirmPassword=(EditText)findViewById(R.id.confirmPassword);
        mataOn=(ImageButton)findViewById(R.id.visibilitySignup);
        mataOff=(ImageButton)findViewById(R.id.visibilityOffSignup);
        mataOn2=(ImageButton)findViewById(R.id.visibilitySignup2);
        mataOff2=(ImageButton)findViewById(R.id.visibilityOffSignup2);
        btnSignUp=(FancyButton)findViewById(R.id.btnSignUp);
        signUpBack=(ImageButton)findViewById(R.id.signUpBack);
        signUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        session=new SessionManajer(getApplicationContext());
        mataOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mataOn.setVisibility(View.GONE);
                mataOff.setVisibility(View.VISIBLE);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        mataOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mataOn.setVisibility(View.VISIBLE);
                mataOff.setVisibility(View.GONE);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        mataOn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mataOn2.setVisibility(View.GONE);
                mataOff2.setVisibility(View.VISIBLE);
                confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        mataOff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mataOn2.setVisibility(View.VISIBLE);
                mataOff2.setVisibility(View.GONE);
                confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        kategori.setItems("SELECT ROLE","MAHASISWA","DOSEN","ALUMNI");
        kategori.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals("SELECT ROLE")){
                    role=" ";
                }
                else if(item.equals("MAHASISWA")){
                    role="MAHASISWA";
                }
                else if(item.equals("DOSEN")){
                    role="DOSEN";
                }
                else if(item.equals("ALUMNI")){
                    role="ALUMNI";
                }

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUname = username.getText().toString();
                mFname = fullname.getText().toString();
                mKategori = role;
                mEmail = email.getText().toString();
                mPass = password.getText().toString();
                mPassCon = confirmPassword.getText().toString();

                if (!mUname.isEmpty()) {
                    if (!mFname.isEmpty()) {
                        if (!mKategori.equals(" ")) {
                            if (!mEmail.isEmpty()) {
                                if (!mPass.isEmpty()) {
                                    if (!mPassCon.isEmpty()) {
                                        if (mPass.equals(mPassCon)) {
                                            register();
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Password Tidak Sama", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Tolong isi Confirm Password", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(SignupActivity.this, "Tolong isi Password", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(SignupActivity.this, "Tolong isi Email", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Tolong isi Kategori", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Tolong isi Fullname", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Tolong isi Username", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void register(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"uname",mUname);
        JsonHelper.put(payload,"flname",mFname);
        JsonHelper.put(payload,"email",mEmail);
        JsonHelper.put(payload,"pass",mPass);
        JsonHelper.put(payload,"role",mKategori);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response",response.toString());
                spinner.dismiss();
                try {
                    int code = Integer.parseInt(response.getString("code"));
                    if (code == 0) {
                        onBackPressed();
                    } else if (code == 1) {
                        String msg = response.getString("msg");
                        if (msg.equals("Data Not Valid")) {
                            Toast.makeText(SignupActivity.this, "Registration failed, Please try again...", Toast.LENGTH_LONG).show();
                        } else if (msg.equals("Data is Already Used")) {
                            Toast.makeText(SignupActivity.this, "Data is exist", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    }else if (code==2){
                        Toast.makeText(SignupActivity.this, "Registration failed, Please try again...", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.REGISTER),payload,successResp,errorResp);
        spinner.setMessage("Sign Up. . . ");
        spinner.show();
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }
    public void  login(){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "email", mEmail);
        JsonHelper.put(payload, "pass", mPass);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                spinner.dismiss();
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Cannot login");
                    return;
                }
                else {
                    try {
                        UserInfos.getFromDatabase(Database.db)
                                .setToken(response.getString("token"))
                                .setUsername(mEmail)
                                .save();

                        Intent i = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(i);
                        SignupActivity.this.finish();
                    }
                    catch (JSONException e){
                        e.printStackTrace();

                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.LOGIN),payload,successResp,errorResp);
        spinner.setMessage("Login. . . ");
        spinner.show();
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }
}
