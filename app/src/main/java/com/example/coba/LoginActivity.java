package com.example.coba;

import androidx.annotation.AnimRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.example.coba.utils.SessionManajer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText usnme,uspass;
    TextInputLayout tl_username, tl_password;
    FancyButton login;
    ProgressDialog spinner;
    String sToken;
    RelativeLayout rlRoot;
    private String username = "", password = "";
    String restValidasi=RestUrl.getUrl(RestUrl.CHECK_VALIDASI);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(FancyButton) findViewById(R.id.btnLogin);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        rlRoot = (RelativeLayout) findViewById(R.id.rlRoot);
        spinner=new ProgressDialog(LoginActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tl_username = (TextInputLayout) findViewById(R.id.tl_username);
        tl_password = (TextInputLayout) findViewById(R.id.tl_password);
        rlRoot.startAnimation(setAnimation(R.anim.bottom_up, (new AccelerateDecelerateInterpolator()), true));
        TextView daftar = (TextView) findViewById(R.id.txtSign);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);

            }
        });

        usnme=(TextInputEditText) findViewById(R.id.usnme);
        uspass=(TextInputEditText) findViewById(R.id.uspass);
        try {

            usnme.addTextChangedListener(textWatcherListener(tl_username, "Please Enter Username"));
            uspass.addTextChangedListener(textWatcherListener(tl_password, "Please Enter Password"));


        } catch (Exception e) {
            Log.e("sdad", e.getMessage());
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usnme.getText().toString();
                password = uspass.getText().toString();
                if(validation()){
                    loginRest(username, password);
                }

            }

        });
    }

    public void  loginRest(final String uname, final String password){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload, "email", uname);
        JsonHelper.put(payload, "pass", password);
        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                spinner.dismiss();
                //Toast.makeText(LoginActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Cannot login");
                    try {
                        String msg = response.getString("msg");
                        if (msg.contains("Incorrect")){
                            Toast.makeText(LoginActivity.this,msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        dialogShow(uname,response.getString("token"));
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.LOGIN),payload,successResp,errorResp);
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        spinner.setMessage("Loading.....");
        spinner.show();
        AppController.getRest().addToReqq(myReq," ");
    }
    public void checkAdmin(String sToken){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(JSONObject response) {
//                Log.e("response", response.toString());
                if (!RestHelper.validateResponse(response)){
                    Log.w("adapter", "invalid response");
                    return;
                }else {
                    try {
                        String mag=response.getString("msg");
//                        Log.e("pesan",mag);
                        if (mag.equals("You are admin")){
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            LoginActivity.this.finish();

                        }else {
                            FirebaseMessaging.getInstance().subscribeToTopic(RestUrl.getSubscribtion());
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            LoginActivity.this.finish();

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(null,null);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(RestUrl.getUrl(RestUrl.CHECK_ADMIN),payload,successResp,errorResp);
        AppController.getRest().addToReqq(jsonObjectRequest,"");
    }
    public void dialogShow(final String email, final String token){
        final Dialog d = new Dialog(LoginActivity.this);
        d.setCancelable(false);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.model_check_validasi);
        final EditText cmt=(EditText) d.findViewById(R.id.editValidasi);

        final ImageView cancel=(ImageView) d.findViewById(R.id.cancelEdit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        FancyButton submit=(FancyButton) d.findViewById(R.id.btnEnterValidasi);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=cmt.getText().toString();
                if (comment.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Tolong isi kolom",Toast.LENGTH_LONG).show();
                }else {
                    JSONObject payload= new JSONObject();
                    JsonHelper.put(payload,"token",token);
                    JsonHelper.put(payload,"kode",comment);
                    Response.Listener successResp = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            spinner.dismiss();
                            JSONObject object=response;
                            try {
                                String code=object.getString("code");
                                if (code.equals("0")){
                                    UserInfos.getFromDatabase(Database.db)
                                            .setToken(token)
                                            .setUsername(email)
                                            .save();
                                    checkAdmin(token);

                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    LoginActivity.this.finish();
                                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                    d.dismiss();
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
        });
        d.show();
    }
    private TextWatcher textWatcherListener(final View view, final String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    if (view instanceof TextInputLayout) {

                        if (s.length() == 0) {
                            ((TextInputLayout) view).setError(errorMessage);
                        } else {
                            ((TextInputLayout) view).setError(null);
                        }

                    }

                } catch (Exception e) {
                    Log.e("sdad", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean validation() {
        try {

            if (username.equalsIgnoreCase("")) {

                tl_username.setError("Please Enter Username");
                return false;

            } else if (password.equalsIgnoreCase("")) {

                tl_password.setError("Please Enter Password");
                return false;

            }

        } catch (Exception e) {
            Log.e("sdasa", e.getMessage());
            return false;
        }

        return true;
    }
    private Animation setAnimation(@AnimRes int id, Interpolator interpolator, Boolean fillAfter) {
        Animation animation = AnimationUtils.loadAnimation(this, id);

        if (interpolator != null) {
            animation.setInterpolator(interpolator);
        } else {
            animation.setInterpolator((new LinearInterpolator()));
        }

        animation.setFillAfter(fillAfter);
        return animation;
    }
}
