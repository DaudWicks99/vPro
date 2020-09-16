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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static android.view.View.GONE;
public class EditVotingActivity extends AppCompatActivity {
    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;
    private static final String TAG= "EditVotingActivity";
    LinearLayout addEdtVoting1;
    LinearLayout addEdtVoting2;
    LinearLayout addEdtVoting3;
    LinearLayout addEdtVoting4;
    ImageButton EdtPlus1;
    ImageButton EdtPlus2;
    ImageButton EdtPlus3;
    ImageButton EdtPlus4;
    ImageButton deletePlus1;
    ImageButton deletePlus2;
    ImageButton deletePlus3;
    ImageButton deletePlus4;
    ImageButton deletePlus5;
    ImageView imgEditKosongVoting;
    Button buttonEditVoting;
    EditText judulEditVoting;
    ImageView EditVoteBack;
    MaterialSpinner kategoriEdit;
    EditText EdtAdd1;
    EditText EdtAdd2;
    EditText EdtAdd3;
    EditText EdtAdd4;
    EditText EdtAdd5;

    Bitmap bmp;
    String upload1;
    String cat="1";
    String mTitle, medit1, medit2, medit3, medit4, medit5;
    String sToken;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_voting);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        EditItem(id);
        imgEditKosongVoting=(ImageView)findViewById(R.id.imgEditKosongVoting);
        buttonEditVoting=(Button)findViewById(R.id.buttonEditVoting);
        judulEditVoting=(EditText)findViewById(R.id.judulEditVoting);
        kategoriEdit=(MaterialSpinner) findViewById(R.id.kategoriEdit);
        EdtPlus1=(ImageButton)findViewById(R.id.EdtPlus1);
        EdtPlus2=(ImageButton)findViewById(R.id.EdtPlus2);
        EdtPlus3=(ImageButton)findViewById(R.id.EdtPlus3);
        EdtPlus4=(ImageButton)findViewById(R.id.EdtPlus4);
        deletePlus2=(ImageButton)findViewById(R.id.deletePlus2);
        deletePlus3=(ImageButton)findViewById(R.id.deletePlus3);
        deletePlus4=(ImageButton)findViewById(R.id.deletePlus4);
        deletePlus5=(ImageButton)findViewById(R.id.deletePlus5);
        addEdtVoting1=(LinearLayout)findViewById(R.id.addEdtVoting1);
        addEdtVoting2=(LinearLayout)findViewById(R.id.addEdtVoting2);
        addEdtVoting3=(LinearLayout)findViewById(R.id.addEdtVoting3);
        addEdtVoting4=(LinearLayout)findViewById(R.id.addEdtVoting4);
        EdtAdd1=(EditText)findViewById(R.id.EdtAdd1);
        EdtAdd2=(EditText)findViewById(R.id.EdtAdd2);
        EdtAdd3=(EditText)findViewById(R.id.EdtAdd3);
        EdtAdd4=(EditText)findViewById(R.id.EdtAdd4);
        EdtAdd5=(EditText)findViewById(R.id.EdtAdd5);
        EdtPlus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdtPlus1.setVisibility(GONE);
                addEdtVoting1.setVisibility(View.VISIBLE);
            }
        });
        EdtPlus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EdtPlus2.setVisibility(GONE);
                addEdtVoting2.setVisibility(View.VISIBLE);
            }
        });
        EdtPlus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdtPlus3.setVisibility(GONE);
                addEdtVoting3.setVisibility(View.VISIBLE);
            }
        });
        EdtPlus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdtPlus4.setVisibility(GONE);
                addEdtVoting4.setVisibility(View.VISIBLE);
            }
        });

        deletePlus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlus5.setVisibility(View.VISIBLE);
                addEdtVoting4.setVisibility(GONE);
                EdtPlus4.setVisibility(View.VISIBLE);
            }
        });

        deletePlus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlus4.setVisibility(View.VISIBLE);
                addEdtVoting3.setVisibility(GONE);
                EdtPlus3.setVisibility(View.VISIBLE);
            }
        });

        deletePlus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlus3.setVisibility(View.VISIBLE);
                addEdtVoting2.setVisibility(GONE);
                EdtPlus2.setVisibility(View.VISIBLE);
            }
        });

        deletePlus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlus2.setVisibility(View.VISIBLE);
                addEdtVoting1.setVisibility(GONE);
                EdtPlus1.setVisibility(View.VISIBLE);
            }
        });
        EditVoteBack=(ImageView) findViewById(R.id.EditVoteBack);
        EditVoteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        kategoriEdit.setItems("KBM","LKF","KULIAH","KEPANITIAAN");
        kategoriEdit.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
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
        buttonEditVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItemMenu();
            }
        });

        imgEditKosongVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }
    private void showPictureDialog(){
        final CharSequence[] options = { "Take Photos", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditVotingActivity.this);
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
        imgEditKosongVoting.setImageBitmap(bmp);
        imgEditKosongVoting.setVisibility(View.VISIBLE);
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
        mTitle=judulEditVoting.getText().toString();
        medit1=EdtAdd1.getText().toString();
        medit2=EdtAdd2.getText().toString();
        medit3=EdtAdd3.getText().toString();
        medit4=EdtAdd4.getText().toString();
        medit5=EdtAdd5.getText().toString();
        JSONObject payload=new JSONObject();
        JsonHelper.put(payload,"token",sToken);
        JsonHelper.put(payload,"idList",id);
        JsonHelper.put(payload,"nama",mTitle);
        JsonHelper.put(payload,"group",cat);
        JsonHelper.put(payload,"desc"," ");

        if(!mTitle.isEmpty()){
            if (!medit1.isEmpty()){
                Response.Listener successResp = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject object=response;
                        try {
                            String code=object.getString("code");
                            if(code.equals("0")){

                                Log.e("Response",response.toString());
                                    onBackPressed();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorResp = RestHelper.generalErrorResponse(TAG,null);
                JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.UPDATE_LIST_MENU),payload,successResp,errorResp);
                myReq.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getRest().addToReqq(myReq,TAG);
            }else {
                Toast.makeText(EditVotingActivity.this, "Input Choice 1 first", Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(EditVotingActivity.this, "Input Title first", Toast.LENGTH_LONG).show();
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
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.UPDATE_VOTE),payload,successResp,errorResp);
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
                            judulEditVoting.setText(object.getString("nama"));
                            String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_VOTING)+object.getString("pictures");
                            Picasso.get().load(url).placeholder(R.drawable.placeholder).into(imgEditKosongVoting);
                            cat=object.getString("group_id");
                            JSONObject group=object.getJSONObject("groupMenu");
                            kategoriEdit.setText(group.getString("nama"));
                        }
                    }catch (JSONException ex){
                        ex.printStackTrace();

                    }
                }

            }
        };
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse(TAG, null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.AMBIL_SATU_VOTE),payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,TAG);
    }



}

