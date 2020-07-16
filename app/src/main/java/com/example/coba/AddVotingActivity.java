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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.view.View.GONE;

public class AddVotingActivity extends AppCompatActivity {
    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;
    private static final String TAG= "AddVotingActivity";
    LinearLayout addVoting1;
    LinearLayout addVoting2;
    LinearLayout addVoting3;
    LinearLayout addVoting4;
    ImageButton plus1;
    ImageButton plus2;
    ImageButton plus3;
    ImageButton plus4;
    ImageButton hapusPlus1;
    ImageButton hapusPlus2;
    ImageButton hapusPlus3;
    ImageButton hapusPlus4;
    ImageView imgKosongVoting;
    FancyButton buttonUploadVoting;
    EditText judulUploadVoting;
    ImageView VoteBack;
    MaterialSpinner kategori;
    EditText add1;
    EditText add2;
    EditText add3;
    EditText add4;
    EditText add5;

    Bitmap bmp;
    String upload1;
    String cat="1";
    String mTitle, madd1, madd2, madd3, madd4, madd5;
    String sToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voting);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        imgKosongVoting=(ImageView)findViewById(R.id.imgKosongVoting);
        buttonUploadVoting=(FancyButton) findViewById(R.id.buttonUploadVoting);
        judulUploadVoting=(EditText)findViewById(R.id.judulUploadVoting);
        kategori=(MaterialSpinner) findViewById(R.id.kategori);
        imgKosongVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        plus1=(ImageButton)findViewById(R.id.plus1);
        plus2=(ImageButton)findViewById(R.id.plus2);
        plus3=(ImageButton)findViewById(R.id.plus3);
        plus4=(ImageButton)findViewById(R.id.plus4);
        hapusPlus1=(ImageButton)findViewById(R.id.hapusPlus1);
        hapusPlus2=(ImageButton)findViewById(R.id.hapusPlus2);
        hapusPlus3=(ImageButton)findViewById(R.id.hapusPlus3);
        hapusPlus4=(ImageButton)findViewById(R.id.hapusPlus4);
        addVoting1=(LinearLayout)findViewById(R.id.addVoting1);
        addVoting2=(LinearLayout)findViewById(R.id.addVoting2);
        addVoting3=(LinearLayout)findViewById(R.id.addVoting3);
        addVoting4=(LinearLayout)findViewById(R.id.addVoting4);
        add1=(EditText)findViewById(R.id.add1);
        add2=(EditText)findViewById(R.id.add2);
        add3=(EditText)findViewById(R.id.add3);
        add4=(EditText)findViewById(R.id.add4);
        add5=(EditText)findViewById(R.id.add5);
        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus1.setVisibility(GONE);
                addVoting1.setVisibility(View.VISIBLE);
            }
        });
        plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus2.setVisibility(GONE);
                addVoting2.setVisibility(View.VISIBLE);
            }
        });
        plus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus3.setVisibility(GONE);
                addVoting3.setVisibility(View.VISIBLE);
            }
        });
        plus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus4.setVisibility(GONE);
                addVoting4.setVisibility(View.VISIBLE);
            }
        });

        hapusPlus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusPlus4.setVisibility(View.VISIBLE);
                addVoting4.setVisibility(GONE);
                plus4.setVisibility(View.VISIBLE);
            }
        });
        hapusPlus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusPlus3.setVisibility(View.VISIBLE);
                addVoting3.setVisibility(GONE);
                plus3.setVisibility(View.VISIBLE);
            }
        });
        hapusPlus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusPlus2.setVisibility(View.VISIBLE);
                addVoting2.setVisibility(GONE);
                plus2.setVisibility(View.VISIBLE);
            }
        });
        hapusPlus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusPlus1.setVisibility(View.VISIBLE);
                addVoting1.setVisibility(GONE);
                plus1.setVisibility(View.VISIBLE);
            }
        });

        VoteBack=(ImageView) findViewById(R.id.VoteBack);
        VoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        kategori.setItems("KBM","LKF","KULIAH","KEPANITIAAN");
        kategori.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals("KBM")){
                    cat="1";
                }
                else if(item.equals("LKF")){
                    cat="2";
                }
                else if(item.equals("KULIAH")){
                    cat="3";
                }
                else if(item.equals("KEPANITIAAN")){
                    cat="4";
                }
            }
        });
        buttonUploadVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemMenu();
            }
        });
    }
    private void showPictureDialog(){
        final CharSequence[] options = { "Take Photos", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddVotingActivity.this);
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
        imgKosongVoting.setImageBitmap(bmp);
        imgKosongVoting.setVisibility(View.VISIBLE);
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

    public void addItemMenu() {
        mTitle=judulUploadVoting.getText().toString();
        madd1=add1.getText().toString();
        madd2=add2.getText().toString();
        madd3=add3.getText().toString();
        madd4=add4.getText().toString();
        madd5=add5.getText().toString();
        JSONObject payload=new JSONObject();
        JsonHelper.put(payload,"token",sToken);
        JsonHelper.put(payload,"nama",mTitle);
        JsonHelper.put(payload,"group",cat);
        JsonHelper.put(payload,"desc"," ");
        JsonHelper.put(payload,"status","1");
        JsonHelper.put(payload,"image",upload1);


        if(!mTitle.isEmpty()){
            if (!madd1.isEmpty()){
                Response.Listener successResp = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response",response.toString());
                        JSONObject object=response;
                        try {
                            String code=object.getString("code");
                            if(code.equals("0")){
                                String id=object.getString("idList");
                                String ids=id;
                                Log.e("id:",ids);
                                if(!ids.isEmpty()){
                                    JSONObject payload99 = new JSONObject();
                                    JsonHelper.put(payload99,"token",sToken);
                                    JsonHelper.put(payload99,"idList",ids);
                                    JsonHelper.put(payload99,"vote1",madd1);
                                    JsonHelper.put(payload99,"vote2",madd2);
                                    JsonHelper.put(payload99,"vote3",madd3);
                                    JsonHelper.put(payload99,"vote4",madd4);
                                    JsonHelper.put(payload99,"vote5",madd5);
                                    addVote(payload99);
                                }else{
                                    onBackPressed();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorResp = RestHelper.generalErrorResponse(TAG,null);
                JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.ADD_LIST_MENU,payload,successResp,errorResp);
                myReq.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getRest().addToReqq(myReq,TAG);
            }else {
                Toast.makeText(AddVotingActivity.this, "Input Choice 1 first", Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(AddVotingActivity.this, "Input Title first", Toast.LENGTH_LONG).show();
        }
    }
    private void  addVote(JSONObject payload){
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
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.ADD_VOTE,payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,TAG);
    }


}
