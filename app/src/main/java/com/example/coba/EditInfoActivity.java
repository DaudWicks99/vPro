package com.example.coba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import mehdi.sakout.fancybuttons.FancyButton;

public class EditInfoActivity extends AppCompatActivity {
    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;
    private static final String TAG= "EditInfoActivity";
    FancyButton editButtonUploadInfo;
    ImageView editImgKosong;
    EditText editDescInfoUpload;
    EditText editJudulUploadInfo;
    ImageView editInfoBack;


    Bitmap bmp;
    String upload1;
    String sToken;
    String mTitle;
    String status;
    String description;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        Log.e("dsa",id);
        EditItem(id);
        editImgKosong=(ImageView)findViewById(R.id.editImgKosong);
        editDescInfoUpload= (EditText)findViewById(R.id.editDescInfoUpload);
        editButtonUploadInfo=(FancyButton) findViewById(R.id.editButtonUploadInfo);
        editJudulUploadInfo=(EditText)findViewById(R.id.editJudulUploadInfo);
        editInfoBack=(ImageView) findViewById(R.id.editInfoBack);
        editInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        editButtonUploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItemMenu();
            }
        });


        editImgKosong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }
    private void showPictureDialog(){
        final CharSequence[] options = { "Take Photos", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);
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
        editImgKosong.setImageBitmap(bmp);
        editImgKosong.setVisibility(View.VISIBLE);
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
    public void editItemMenu() {
        mTitle=editJudulUploadInfo.getText().toString();
        description=editDescInfoUpload.getText().toString();
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
                        Log.e("Response",response.toString());
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
                JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.UPDATE_LIST_MENU,payload,successResp,errorResp);
                myReq.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getRest().addToReqq(myReq," ");
            }else {
                Toast.makeText(EditInfoActivity.this, "Input Choice 1 first", Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(EditInfoActivity.this, "Input Title first", Toast.LENGTH_LONG).show();
        }
    }
    private void  EditVote(JSONObject payload){
        Response.Listener<JSONObject> successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w(TAG,"Response not valid");
                    return;
                }else{
                    Log.e(TAG,"Success Response");
                    onBackPressed();
                }

            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(TAG, null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.UPDATE_INFO,payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,TAG);
    }
    private void  EditItem(String id){
        JSONObject payload=new JSONObject();
        JsonHelper.put(payload,"token",sToken);
        JsonHelper.put(payload,"id",id);
        Response.Listener<JSONObject> successResp = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w(TAG,"Response not valid");
                    return;
                }else{
                    Log.e(TAG,"Success Response");
                    try {
                        JSONArray result=response.getJSONArray("result");
                        for (int i=0; i<result.length();i++){
                            JSONObject item=result.getJSONObject(i);
                            JSONObject object=item.getJSONObject("listMenu");
                            editJudulUploadInfo.setText(object.getString("judul"));
                            editDescInfoUpload.setText(object.getString("description"));
                            String url=RestUrl.IMAGE_URL_INFO+object.getString("image");
                            Picasso.get().load(url).placeholder(R.drawable.placeholder).into(editImgKosong);

                        }
                    }catch (JSONException ex){
                        ex.printStackTrace();

                    }
                }

            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(TAG, null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.AMBIL_SATU_INFO,payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,TAG);
    }
}
