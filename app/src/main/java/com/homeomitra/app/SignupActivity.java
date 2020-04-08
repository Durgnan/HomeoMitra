package com.homeomitra.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class SignupActivity extends AppCompatActivity {
    LinearLayout patientPharmacyData,doctorData;
    EditText email,fname,lname,phone,password,cfPassword,area,address,landmark,pincode,city,location,degree,specialization,daysOpen,timeSlots,fee;
    Button submit,open;
    TextView filename;
    RadioButton patient,doctor,pharmacy;
    RadioGroup mainGroup;
    String mid,fn,ln,ph,pass,cfpass,are,add,land,pin,cit,loc,deg,spec,dop,tslo,fees;
    String path,ba1;
    static final int REQUEST_IMAGE_CAPTURE = 1888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViews();
        mainGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.doctorRadio) {
                    patientPharmacyData.setVisibility(View.GONE);
                    doctorData.setVisibility(View.VISIBLE);
                    area.setText("");
                    address.setText("");
                    landmark.setText("");
                    pincode.setText("");
                    city.setText("");
                    open.setEnabled(true);
                    submit.setEnabled(false);
                    open.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setTitle("Add Photo!");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (options[item].equals("Take Photo"))
                                    {
                                        dispatchTakePictureIntent();
                                    }
                                    else if (options[item].equals("Choose from Gallery"))
                                    {
                                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, 2);
                                    }
                                    else if (options[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                            builder.show();

                        }
                    });

                }
                else
                {
                    doctorData.setVisibility(View.GONE);
                    patientPharmacyData.setVisibility(View.VISIBLE);
                    area.setText("");
                    address.setText("");
                    landmark.setText("");
                    pincode.setText("");
                    city.setText("");
                    location.setText("");
                    degree.setText("");
                    specialization.setText("");
                    daysOpen.setText("");
                    timeSlots.setText("");
                    fee.setText("");
                    open.setEnabled(false);
                    submit.setEnabled(true);
                    filename.setText("Choose Image");
                }

            }
        });
        submit.setOnClickListener(SubmitData);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
                if(photoFile!=null) {
                    path = photoFile.getAbsolutePath();
                }
            } catch (Exception ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,getPackageName()+".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,
                        REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Log.e("TAG",path.toString());
                submit.setEnabled(true);


                //upload();

                // If you are using Glide.
            }
            else if(requestCode == 2)
            {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);

                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                path = picturePath;
                Log.e("TT",picturePath);
                Log.w("path of image", picturePath+"");


                submit.setEnabled(true);
            }
        }

    }
    private File createPhotoFile()
    {

        File storageDir =
             getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            fn = fname.getText().toString().trim();
            ln = lname.getText().toString().trim();
            image = File.createTempFile(fn+"_"+ln,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }
    protected void findViews()
    {
        patientPharmacyData = findViewById(R.id.patientPharmacyData);
        doctorData = findViewById(R.id.doctorData);
        email = findViewById(R.id.email);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        cfPassword = findViewById(R.id.cfpassword);
        area = findViewById(R.id.area);
        address = findViewById(R.id.address);
        landmark = findViewById(R.id.landmark);
        pincode = findViewById(R.id.pincode);
        city = findViewById(R.id.city);
        location = findViewById(R.id.location);
        degree = findViewById(R.id.degree);
        specialization = findViewById(R.id.spec);
        daysOpen = findViewById(R.id.dayOpen);
        timeSlots = findViewById(R.id.timeSlots);
        fee = findViewById(R.id.fees);
        submit = findViewById(R.id.submit);
        open = findViewById(R.id.saveImage);
        filename = findViewById(R.id.saveImageText);
        patient = findViewById(R.id.patientRadio);
        doctor = findViewById(R.id.doctorRadio);
        pharmacy = findViewById(R.id.pharmacyRadio);
        mainGroup = findViewById(R.id.radioGrp);




    }

    OnClickListener SubmitData = new OnClickListener() {
        @Override
        public void onClick(View v) {

            mid = email.getText().toString().trim();
            fn = fname.getText().toString().trim();
            ln = lname.getText().toString().trim();
            ph = phone.getText().toString().trim();
            pass = password.getText().toString().trim();
            cfpass = cfPassword.getText().toString().trim();




            switch (mainGroup.getCheckedRadioButtonId())
            {
                case R.id.patientRadio :
                    are = area.getText().toString().trim();
                    add = address.getText().toString().trim();
                    land = landmark.getText().toString().trim();
                    pin = pincode.getText().toString().trim();
                    cit = city.getText().toString().trim();
                    if( mid.isEmpty()||fn.isEmpty()||ln.isEmpty()||ph.isEmpty()||pass.isEmpty()||cfpass.isEmpty()||are.isEmpty() ||add.isEmpty() ||land.isEmpty() ||pin.isEmpty() ||cit.isEmpty())
                    {

                        if(mid.isEmpty()) email.setError("Enter a value");
                        if(fn.isEmpty()) fname.setError("Enter a value");
                        if(ln.isEmpty()) lname.setError("Enter a value");
                        if(ph.isEmpty()) phone.setError("Enter a value");
                        if(pass.isEmpty()) password.setError("Enter a value");
                        if(cfpass.isEmpty()) cfPassword.setError("Enter a value");
                        if(are.isEmpty()) area.setError("Enter a value");
                        if(add.isEmpty())address.setError("Enter a value");
                        if(land.isEmpty())landmark.setError("Enter a value");
                        if(pin.isEmpty())pincode.setError("Enter a value");
                        if(cit.isEmpty())city.setError("Enter a value");

                    }
                    else if(cfpass.equals(pass) == false)
                    {
                        cfPassword.setError("Password doesn't match");
                        cfPassword.setText("");
                    }
                    else if(Patterns.EMAIL_ADDRESS.matcher(mid).matches() == false)
                    {
                        email.setError("Enter a valid email address");
                        email.setText("");
                    }
                    else
                    {
                        String type = "PATIENT";
                        submit.setEnabled(false);
                        submit.setText("Creating...");
                        String login_url = getResources().getString(R.string.HM_LOGIN_API);
                        JSONObject jsonObject= new JSONObject();
                        try {
                            jsonObject.put("MODE","SIGNUP");
                            jsonObject.put("type",type);
                            jsonObject.put("email",mid);
                            jsonObject.put("phone",ph);
                            jsonObject.put("fname",fn);
                            jsonObject.put("lname",ln);
                            jsonObject.put("password",pass);
                            jsonObject.put("area",are);
                            jsonObject.put("address",add);
                            jsonObject.put("pincode",pin);
                            jsonObject.put("landmark",land);
                            jsonObject.put("city",cit);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RequestQueue requestQueue= Volley.newRequestQueue(SignupActivity.this);
                        JsonObjectRequest objectRequest= new JsonObjectRequest(Request.Method.POST, login_url, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    Log.e("MSGR", response.getString("message"));
                                     if(response.getString("message").equals("FAILED"))
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        submit.setClickable(true);
                                        submit.setEnabled(true);
                                        email.setText("");
                                        password.setText("");
                                        fname.setText("");
                                        lname.setText("");
                                        phone.setText("");
                                        cfPassword.setText("");

                                        area.setText("");
                                        address.setText("");
                                        landmark.setText("");
                                        pincode.setText("");
                                        city.setText("");
                                        location.setText("");
                                        degree.setText("");
                                        specialization.setText("");
                                        daysOpen.setText("");
                                        timeSlots.setText("");
                                        filename.setText("Choose Image");
                                        submit.setText("submit");

                                    }
                                    else if(response.getString("message").equals("SUCCESS"))
                                    {

                                        //Intent intent = new Intent(SignupActivity.this, ValidationActivity.class);


                                        submit.setText("Done!!");
                                        onBackPressed();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        submit.setClickable(true);
                                        submit.setEnabled(true);
                                        email.setText("");
                                        password.setText("");
                                        fname.setText("");
                                        lname.setText("");
                                        phone.setText("");
                                        cfPassword.setText("");

                                        area.setText("");
                                        address.setText("");
                                        landmark.setText("");
                                        pincode.setText("");
                                        city.setText("");
                                        location.setText("");
                                        degree.setText("");
                                        specialization.setText("");
                                        daysOpen.setText("");
                                        timeSlots.setText("");
                                        filename.setText("Choose Image");
                                        submit.setText("submit");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                        );


                        requestQueue.add(objectRequest);

                    }



                    break;
                case R.id.doctorRadio:

                    loc = location.getText().toString().trim();
                    deg = degree.getText().toString().trim();
                    spec = specialization.getText().toString().trim();
                    dop = daysOpen.getText().toString().trim();
                    tslo = timeSlots.getText().toString().trim();
                    fees = fee.getText().toString().trim();

                    if( mid.isEmpty()||fn.isEmpty()||ln.isEmpty()||ph.isEmpty()||pass.isEmpty()||cfpass.isEmpty()||loc.isEmpty() ||spec.isEmpty() ||dop.isEmpty() ||deg.isEmpty() ||tslo.isEmpty()|| fees.isEmpty())
                    {

                        if(mid.isEmpty()) email.setError("Enter a value");
                        if(fn.isEmpty()) fname.setError("Enter a value");
                        if(ln.isEmpty()) lname.setError("Enter a value");
                        if(ph.isEmpty()) phone.setError("Enter a value");
                        if(pass.isEmpty()) password.setError("Enter a value");
                        if(cfpass.isEmpty()) cfPassword.setError("Enter a value");
                        if(loc.isEmpty()) location.setError("Enter a value");
                        if(deg.isEmpty())degree.setError("Enter a value");
                        if(spec.isEmpty())specialization.setError("Enter a value");
                        if(dop.isEmpty())daysOpen.setError("Enter a value");
                        if(tslo.isEmpty())timeSlots.setError("Enter a value");
                        if(fees.isEmpty())fee.setError("Enter a value");
                    }
                    else if(cfpass.equals(pass) == false)
                    {
                        cfPassword.setError("Password doesn't match");
                        cfPassword.setText("");
                    }
                    else if(Patterns.EMAIL_ADDRESS.matcher(mid).matches() == false)
                    {
                        email.setError("Enter a valid email address");
                        email.setText("");
                    }
                    else
                    {
                        String type = "DOCTOR";
                        submit.setEnabled(false);
                        submit.setText("Creating...");
                        String login_url = getResources().getString(R.string.HM_LOGIN_API);
                        JSONObject jsonObject= new JSONObject();
                        Bitmap bm = null;
                        try {
                            bm = BitmapFactory.decodeFile(path);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                        byte[] ba = bao.toByteArray();
                        ba1 = Base64.encodeToString(ba,Base64.DEFAULT);
                        try {
                            jsonObject.put("MODE","SIGNUP");
                            jsonObject.put("type",type);
                            jsonObject.put("email",mid);
                            jsonObject.put("phone",ph);
                            jsonObject.put("fname",fn);
                            jsonObject.put("lname",ln);
                            jsonObject.put("password",pass);
                            jsonObject.put("location",loc);
                            jsonObject.put("degree",deg);
                            jsonObject.put("specialization",spec);
                            jsonObject.put("dopen",dop);
                            jsonObject.put("timings",tslo);
                            jsonObject.put("imgcontent",ba1);
                            jsonObject.put("fees",fees);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        RequestQueue requestQueue= Volley.newRequestQueue(SignupActivity.this);
                        JsonObjectRequest objectRequest= new JsonObjectRequest(Request.Method.POST, login_url, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    Log.e("MSGR", response.getString("message"));
                                    if(response.getString("emsg").contains("1062"))
                                    {
                                        Log.e("EMSGR", response.getString("emsg"));

                                        submit.setText("Done!!");
                                        onBackPressed();
                                    }
                                    else if(response.getString("message").equals("FAILED"))
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        submit.setClickable(true);
                                        submit.setEnabled(true);
                                        email.setText("");
                                        password.setText("");
                                        fname.setText("");
                                        lname.setText("");
                                        phone.setText("");
                                        cfPassword.setText("");

                                        area.setText("");
                                        address.setText("");
                                        landmark.setText("");
                                        pincode.setText("");
                                        city.setText("");
                                        location.setText("");
                                        degree.setText("");
                                        specialization.setText("");
                                        daysOpen.setText("");
                                        timeSlots.setText("");
                                        filename.setText("Choose Image");
                                        fee.setText("");
                                        submit.setText("submit");

                                    }
                                    else if(response.getString("message").equals("SUCCESS"))
                                    {

                                        //Intent intent = new Intent(SignupActivity.this, ValidationActivity.class);


                                        submit.setText("Done!!");
                                        onBackPressed();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        submit.setClickable(true);
                                        submit.setEnabled(true);
                                        email.setText("");
                                        password.setText("");
                                        fname.setText("");
                                        lname.setText("");
                                        phone.setText("");
                                        cfPassword.setText("");

                                        area.setText("");
                                        address.setText("");
                                        landmark.setText("");
                                        pincode.setText("");
                                        city.setText("");
                                        location.setText("");
                                        degree.setText("");
                                        specialization.setText("");
                                        daysOpen.setText("");
                                        timeSlots.setText("");
                                        filename.setText("Choose Image");
                                        fee.setText("");
                                        submit.setText("submit");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                        );


                        requestQueue.add(objectRequest);


                    }

                    break;
                case R.id.pharmacyRadio:
                    are = area.getText().toString().trim();
                    add = address.getText().toString().trim();
                    land = landmark.getText().toString().trim();
                    pin = pincode.getText().toString().trim();
                    cit = city.getText().toString().trim();
                    if( mid.isEmpty()||fn.isEmpty()||ln.isEmpty()||ph.isEmpty()||pass.isEmpty()||cfpass.isEmpty()||are.isEmpty() ||add.isEmpty() ||land.isEmpty() ||pin.isEmpty() ||cit.isEmpty())
                    {

                        if(mid.isEmpty()) email.setError("Enter a value");
                        if(fn.isEmpty()) fname.setError("Enter a value");
                        if(ln.isEmpty()) lname.setError("Enter a value");
                        if(ph.isEmpty()) phone.setError("Enter a value");
                        if(pass.isEmpty()) password.setError("Enter a value");
                        if(cfpass.isEmpty()) cfPassword.setError("Enter a value");
                        if(are.isEmpty()) area.setError("Enter a value");
                        if(add.isEmpty())address.setError("Enter a value");
                        if(land.isEmpty())landmark.setError("Enter a value");
                        if(pin.isEmpty())pincode.setError("Enter a value");
                        if(cit.isEmpty())city.setError("Enter a value");

                    }
                    else if(cfpass.equals(pass) == false)
                    {
                        cfPassword.setError("Password doesn't match");
                        cfPassword.setText("");
                    }
                    else if(Patterns.EMAIL_ADDRESS.matcher(mid).matches() == false)
                    {
                        email.setError("Enter a valid email address");
                        email.setText("");
                    }
                    else
                    {
                        String type = "PHARMACY";
                        submit.setEnabled(false);
                        submit.setText("Creating...");
                        String login_url = getResources().getString(R.string.HM_LOGIN_API);
                        JSONObject jsonObject= new JSONObject();
                        try {
                            jsonObject.put("MODE","SIGNUP");
                            jsonObject.put("type",type);
                            jsonObject.put("email",mid);
                            jsonObject.put("phone",ph);
                            jsonObject.put("fname",fn);
                            jsonObject.put("lname",ln);
                            jsonObject.put("password",pass);
                            jsonObject.put("area",are);
                            jsonObject.put("address",add);
                            jsonObject.put("pincode",pin);
                            jsonObject.put("landmark",land);
                            jsonObject.put("city",cit);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RequestQueue requestQueue= Volley.newRequestQueue(SignupActivity.this);
                        JsonObjectRequest objectRequest= new JsonObjectRequest(Request.Method.POST, login_url, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    Log.e("MSGR", response.getString("message"));
                                    if(response.getString("message").equals("FAILED"))
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        submit.setClickable(true);
                                        submit.setEnabled(true);
                                        email.setText("");
                                        password.setText("");
                                        fname.setText("");
                                        lname.setText("");
                                        phone.setText("");
                                        cfPassword.setText("");

                                        area.setText("");
                                        address.setText("");
                                        landmark.setText("");
                                        pincode.setText("");
                                        city.setText("");
                                        location.setText("");
                                        degree.setText("");
                                        specialization.setText("");
                                        daysOpen.setText("");
                                        timeSlots.setText("");
                                        filename.setText("Choose Image");
                                        submit.setText("submit");

                                    }
                                    else if(response.getString("message").equals("SUCCESS"))
                                    {

                                        //Intent intent = new Intent(SignupActivity.this, ValidationActivity.class);


                                        submit.setText("Done!!");
                                        onBackPressed();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        submit.setClickable(true);
                                        submit.setEnabled(true);
                                        email.setText("");
                                        password.setText("");
                                        fname.setText("");
                                        lname.setText("");
                                        phone.setText("");
                                        cfPassword.setText("");

                                        area.setText("");
                                        address.setText("");
                                        landmark.setText("");
                                        pincode.setText("");
                                        city.setText("");
                                        location.setText("");
                                        degree.setText("");
                                        specialization.setText("");
                                        daysOpen.setText("");
                                        timeSlots.setText("");
                                        filename.setText("Choose Image");
                                        submit.setText("submit");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                        );


                        requestQueue.add(objectRequest);
                    }
                    break;
            }



        }
    };
}
