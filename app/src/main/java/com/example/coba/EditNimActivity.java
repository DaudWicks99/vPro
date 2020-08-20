package com.example.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coba.database.Database;
import com.example.coba.model.Json.JsonHelper;
import com.example.coba.model.Rest.RestHelper;
import com.example.coba.model.activerecords.UserInfos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;
import mehdi.sakout.fancybuttons.FancyButton;

public class EditNimActivity extends AppCompatActivity {
    ImageButton back;
    EditText editNim, editAlamat, editTlp,  editFlName, editTempat;
    DatePickerInputEditText editDob;
    MaterialSpinner gender;
    FancyButton confrim;
    ProgressDialog spinner;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener mDatePicker;
    String cat=" ";
    String sToken;
    String nFlname, nNim, nAlamat, nTlp,nGender, nTempat, nDob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nim);
        sToken = UserInfos.getFromDatabase(Database.db).token;
        Intent intent=getIntent();
        nFlname=intent.getStringExtra("flname");
        nNim=intent.getStringExtra("nim");
        nAlamat=intent.getStringExtra("alamat");
        nTlp=intent.getStringExtra("tlp");
        nGender=intent.getStringExtra("tlp");
        nTempat=intent.getStringExtra("tempat");
        nDob=intent.getStringExtra("dob");

        myCalendar=Calendar.getInstance();
        editNim=(EditText)findViewById(R.id.editNim);
        editFlName=(EditText)findViewById(R.id.editFlName);
        editAlamat=(EditText)findViewById(R.id.editAlamat);
        editTempat=(EditText)findViewById(R.id.editTempat);
        editTlp=(EditText)findViewById(R.id.editNoTelepon);
        editDob=(DatePickerInputEditText) findViewById(R.id.editDob);
        gender=(MaterialSpinner)findViewById(R.id.editGender);
        confrim=(FancyButton)findViewById(R.id.btnEditNim);
        spinner=new ProgressDialog(EditNimActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProfile();
            }
        });
        back=(ImageButton)findViewById(R.id.editNimBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(nFlname.equals("-")){
            nFlname=" ";
        }if(nNim.equals("-")){
            nNim=" ";
        }else {
            editNim.setText(nNim);
        }
        if(nAlamat.equals("-")){
            nAlamat=" ";
        }else {
            editAlamat.setText(nAlamat);
        }
        if(nTlp.equals("-")){
            nTlp=" ";
        }else {
            editTlp.setText(nTlp);
        }
        if(nGender.equals("-")){
            nGender=" ";
        }else {
            cat=nGender;
        }
        if(nTempat.equals("-")){
            nTempat=" ";
        }else {
            editTempat.setText(nTempat);
        }
        if(nDob.equals("-")){
            nDob=" ";
        }else {
            editDob.setText(nDob);
        }

        editFlName.setText(nFlname);

        gender.setItems("SELECT GENDER","MALE","FIMALE");
        gender.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals("SELECT GENDER")){
                    cat=" ";
                }
                else if(item.equals("MALE")){
                    cat="1";
                }
                else if(item.equals("FIMALE")){
                    cat="2";
                }

            }
        });

        mDatePicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDatePickerLable1();
            }
        };
        editDob.setManager(EditNimActivity.this.getSupportFragmentManager());

    }
    public void AddProfile(){
        nFlname=editFlName.getText().toString();
        nNim=editNim.getText().toString();
        nAlamat=editAlamat.getText().toString();
        nTlp=editTlp.getText().toString();
        nTempat=editTempat.getText().toString();
        nDob=editDob.getText().toString();
        if (!nFlname.isEmpty()||!nFlname.equals(" ")){
            if (!nNim.isEmpty()||!nNim.equals(" ")){
                if (!nAlamat.isEmpty()||!nAlamat.equals(" ")){
                    if (!nTlp.isEmpty()||!nTlp.equals(" ")){
                        if (!cat.equals(" ")){
                            if (!nTempat.isEmpty()||!nTempat.equals(" ")){
                                if (!nDob.isEmpty()||!nDob.equals(" ")){
                                    JSONObject payload=new JSONObject();
                                    JsonHelper.put(payload,"token",sToken);
                                    JsonHelper.put(payload,"flname",nFlname);
                                    JsonHelper.put(payload,"nim",nNim);
                                    JsonHelper.put(payload,"alamat",nAlamat);
                                    JsonHelper.put(payload,"nomerTelpon",nTlp);
                                    JsonHelper.put(payload,"gender",cat);
                                    JsonHelper.put(payload,"tempat",nTempat);
                                    JsonHelper.put(payload,"tanggalLahir",nDob);
                                    Response.Listener successResp = new Response.Listener<JSONObject>(){
                                        @Override
                                        public void onResponse(JSONObject response){
                                            spinner.dismiss();
                                            JSONObject object=response;
                                            try {
                                                String code=object.getString("code");
                                                if(code.equals("0")){
                                                    Intent intent=new Intent(EditNimActivity.this,ProfileActivity.class)
                                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }

                                            }
                                            catch (JSONException ex){
                                                ex.printStackTrace();
                                            }


                                        }};
                                    Response.ErrorListener errorResp = RestHelper.generalErrorResponse("", spinner);
                                    JsonObjectRequest myReq=new JsonObjectRequest(RestUrl.getUrl(RestUrl.ADD_PROFILE),payload,successResp,errorResp);
                                    myReq.setRetryPolicy(new DefaultRetryPolicy(
                                            10000,
                                            0,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    spinner.setMessage("Upload Photo. . . ");
                                    spinner.show();
                                    AppController.getRest().addToReqq(myReq,"");

                                }else {
                                    Toast.makeText(EditNimActivity.this, "Input Date Of Birth", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(EditNimActivity.this, "Input Place Of Birth", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(EditNimActivity.this, "Select Gender", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(EditNimActivity.this, "Input Phone Number", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(EditNimActivity.this, "Input Address", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(EditNimActivity.this, "Input Nim", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(EditNimActivity.this, "Input Full Name", Toast.LENGTH_LONG).show();
        }
    }

    private void updateDatePickerLable1(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDob.setText(sdf.format(myCalendar.getTime()));
    }


}
