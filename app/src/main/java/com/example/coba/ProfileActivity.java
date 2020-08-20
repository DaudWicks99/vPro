package com.example.coba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;

    CircleImageView photoProfile;
    ImageButton back, editProfile;
    TextView uName, email, flname, nim, alamat,noTlp, gender, dob;
    FloatingActionButton changeProfile;
    ProgressDialog spinner;
    Bitmap bmp;
    String sToken;
    String nFlname, nNim, nAlamat, nTlp, nGender, nTempat, nDob;
    String upload1;
    long time;
    String times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        photoProfile=(CircleImageView) findViewById(R.id.photoProfile);
        changeProfile=(FloatingActionButton) findViewById(R.id.changePhotoProfile);
        editProfile=(ImageButton)findViewById(R.id.ProfileEdit);
        back=(ImageButton) findViewById(R.id.ProfileBack);
        uName=(TextView) findViewById(R.id.profile_username);
        email=(TextView) findViewById(R.id.profile_email);
        nim=(TextView) findViewById(R.id.profile_nim);
        alamat=(TextView) findViewById(R.id.profile_alamat);
        noTlp=(TextView) findViewById(R.id.profile_noTlp);
        gender=(TextView) findViewById(R.id.profile_gender);
        dob=(TextView) findViewById(R.id.profile_dob);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        time=System.currentTimeMillis();
        times=String.valueOf(time);
        spinner=new ProgressDialog(ProfileActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        lihatProfil();
        lihatBio();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,EditNimActivity.class);
                intent.putExtra("flname",nFlname);
                intent.putExtra("nim",nNim);
                intent.putExtra("alamat",nAlamat);
                intent.putExtra("tlp",nTlp);
                intent.putExtra("gender",nGender);
                intent.putExtra("tempat",nTempat);
                intent.putExtra("dob",nDob);
                startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }
    private void showPictureDialog(){
        final CharSequence[] options = { "Take Photos", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Change Photo!");
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
        upload1=getStringImage(myBitmap);
        uploadImage(upload1);
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

    public void uploadImage(String upload){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);
        JsonHelper.put(payload,"image", upload);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                spinner.dismiss();
  //              Log.e("response", response.toString());
                JSONObject object=response;
                    try {
                        String code=object.getString("code");
                        if(code.equals("0")){
                            lihatProfil();
                        }

                    }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }


            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.UPDATE_PIC_PROFILE),payload,successResp,errorResp);
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        spinner.setMessage("Upload Photo. . . ");
        spinner.show();
        AppController.getRest().addToReqq(myReq,"");
    }

    public void lihatProfil (){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
    //            Log.e(" ", response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Not a valid Response");
                }
                else {
                    try {
                        JSONObject result= response.getJSONObject("result");
                        String imgName=result.getString("pictures");
                        String url=RestUrl.getImgBase(RestUrl.IMAGE_URL_PROFILE)+imgName+"?time="+times;
                        Log.e(" ",url);
                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.fotokosong)
                                .into(photoProfile);
                        nFlname=result.getString("full_name");
                        uName.setText(result.getString("full_name"));
                        email.setText(result.getString("email"));
                    }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }

                }
            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.PROFILE),payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"");
    }

    public void lihatBio (){
        JSONObject payload = new JSONObject();
        JsonHelper.put(payload,"token", sToken);
        Log.e("token",sToken);

        Response.Listener successResp = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                Log.e(" ", response.toString());
                if(!RestHelper.validateResponse(response)){
                    Log.w(" ", "Not a valid Response");
                    nNim="-";
                    nAlamat="-";
                    nTlp="-";
                    nGender="-";
                    nTempat="-";
                    nDob="-";
                    nim.setText(nNim);
                    alamat.setText(nAlamat);
                    noTlp.setText(nTlp);
                    gender.setText(nGender);
                    dob.setText(nDob);
                }
                else {
                    try {
                        JSONObject result= response.getJSONObject("result");
                        nNim=result.getString("nim");
                        nim.setText(nNim);
                        nAlamat=result.getString("alamat");
                        alamat.setText(nAlamat);
                        nTlp=result.getString("nomerTelpon");
                        noTlp.setText(nTlp);
                        nGender=result.getString("gender");
                        if (nGender.equals("1")) {
                            gender.setText("MALE");
                        }else if (nGender.equals("2")){
                            gender.setText("FIMALE");
                        }
                        nTempat=result.getString("tempat");
                        nDob=result.getString("tanggalLahir");
                        dob.setText(nTempat+", "+nDob);
                    }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }

                }
            }};
        Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", null);
        JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.BIODATA),payload,successResp,errorResp);
        AppController.getRest().addToReqq(myReq,"");
    }
}
