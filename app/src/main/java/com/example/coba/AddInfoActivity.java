package com.example.coba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.activity_home.HomeFragment;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.example.coba.service.ServiceFirebase.MessageService;
import com.example.coba.service.Util.FCMHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import mehdi.sakout.fancybuttons.FancyButton;

public class AddInfoActivity extends AppCompatActivity {
    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;
    FancyButton buttonUploadInfo;
    ImageView imgKosong;
    EditText descInfoUpload;
    EditText judulUploadInfo;
    ImageButton infoBack;
    ProgressDialog spinner;

    Bitmap bmp;
    String upload1;
    String sToken;
    String mTitle;
    String status;
    String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        imgKosong=(ImageView)findViewById(R.id.imgKosong);
        descInfoUpload= (EditText)findViewById(R.id.descInfoUpload);
        buttonUploadInfo=(FancyButton) findViewById(R.id.buttonUploadInfo);
        judulUploadInfo=(EditText)findViewById(R.id.judulUploadInfo);
        spinner=new ProgressDialog(AddInfoActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        infoBack=(ImageButton) findViewById(R.id.InfoBack);
        infoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spinner.setMessage("Loading. . . ");
        buttonUploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNotif();
            }
        });


        imgKosong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });


    }
    private void showPictureDialog(){
        final CharSequence[] options = { "Take Photos", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddInfoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals("Take Photos")) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, TAKE_PICTURE);
                }
                else if(options[which].equals("Choose from gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
                }
                else if(options[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public String saveImage(Bitmap myBitmap){
        ByteArrayOutputStream Bytes = new ByteArrayOutputStream();
        bmp=myBitmap;
        imgKosong.setImageBitmap(bmp);
        imgKosong.setVisibility(View.VISIBLE);
        upload1=getStringImage(myBitmap);
        return"";
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap resize=Bitmap.createScaledBitmap(bmp,(int) (bmp.getWidth()*0.4),(int) (bmp.getHeight()*0.4),true);
        resize.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if(requestCode == TAKE_PICTURE)
            {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                saveImage(photo);
            }
            else if(requestCode == ACTIVITY_SELECT_IMAGE){
                try{
                    Uri selectedImage = data.getData();
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap thumbnail = (BitmapFactory.decodeStream(imageStream));
                    saveImage(thumbnail);
                }catch (Exception ex){

                }
            }
        }
    }
    public void sendNotif() {
        spinner.show();
        mTitle=judulUploadInfo.getText().toString();
        description=descInfoUpload.getText().toString();
        if(!mTitle.isEmpty()){
            if (!description.isEmpty()){
                    new AsyncTask<Void, Void, Void>(){
                    JsonObject notificationObject;
                    JsonObject dataObject;

                    Boolean success;

                protected void onPreExecute(){
                    notificationObject=new JsonObject();
                    notificationObject.addProperty("title","INFO");
                    notificationObject.addProperty("body",mTitle);
                    notificationObject.addProperty("click_action", MessageService.ACTION_NEED_INFO);

                    dataObject=new JsonObject();
                    dataObject.addProperty("id","1");


                }
                @Override
                protected Void doInBackground(Void... voids) {
                    FCMHelper helper= FCMHelper.getInstance();
                    try {
                        helper.sendTopicNotificationAndData("admin",notificationObject,dataObject);
                        success=true;
                    }catch (IOException e){
                        e.printStackTrace();
                        success=false;
                    }
                    return null;
                }
                protected void onPostExecute(Void unused){
                    if (success){
                        addItemMenu();
                    }else {
                        spinner.dismiss();
                        Toast.makeText(AddInfoActivity.this,"Upload Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
                }else {
                    Toast.makeText(AddInfoActivity.this, "Input Description first", Toast.LENGTH_LONG).show();
                }
            }

            else {
                Toast.makeText(AddInfoActivity.this, "Input Title first", Toast.LENGTH_LONG).show();
            }
    }
    public void addItemMenu() {
        mTitle=judulUploadInfo.getText().toString();
        description=descInfoUpload.getText().toString();
        JSONObject payload=new JSONObject();
        JsonHelper.put(payload,"token",sToken);
        JsonHelper.put(payload,"judul",mTitle);
        JsonHelper.put(payload,"desc",description);
        JsonHelper.put(payload,"status","1");
        JsonHelper.put(payload,"image",upload1);

        if(!mTitle.isEmpty()){
            if (!description.isEmpty()){
                Response.Listener successResp = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.dismiss();
                        JSONObject object=response;
                        try {
                            String code=object.getString("code");
                            if(code.equals("0")){
                               onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorResp = RestHelper.generalErrorResponse(" ",null);
                JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.ADD_INFO_MENU),payload,successResp,errorResp);
                myReq.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getRest().addToReqq(myReq," ");
            }else {
                Toast.makeText(AddInfoActivity.this, "Input Choice 1 first", Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(AddInfoActivity.this, "Input Title first", Toast.LENGTH_LONG).show();
        }
    }
}

