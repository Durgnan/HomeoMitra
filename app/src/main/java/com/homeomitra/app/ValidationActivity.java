package com.homeomitra.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ValidationActivity extends AppCompatActivity {
    Button log_in,login,signup;
    EditText username, password;
    RelativeLayout login_container;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String u,p;
    int LOGIN_CLICKED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);


        if (checkPermissions())
        {}
        else
        {
            requestPermission();
        }
        pref = getSharedPreferences("HomeoMitra",MODE_PRIVATE);
        editor = pref.edit();



        login_container = findViewById(R.id.login_container);
        login = findViewById(R.id.login);

        signup  =findViewById(R.id.signup_button);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              u  = username.getText().toString().trim();
             p = password.getText().toString().trim();

                if(TextUtils.isEmpty(u) || TextUtils.isEmpty(p))
                {
                    Toast.makeText(getApplicationContext(),"Please enter your credentials",Toast.LENGTH_LONG).show();
                }


                else
                    {
                        login.setEnabled(false);
                        login.setText("Verifying...");
                        String login_url = getResources().getString(R.string.HM_LOGIN_API);
                        JSONObject jsonObject= new JSONObject();
                        try {
                            jsonObject.put("MODE","LOGIN");
                            jsonObject.put("username",u);

                            jsonObject.put("password",p);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RequestQueue requestQueue= Volley.newRequestQueue(ValidationActivity.this);
                        JsonObjectRequest objectRequest= new JsonObjectRequest(Request.Method.POST, login_url, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    Log.e("MSGR", response.getString("message"));
                                    if(response.getString("message").equals("INCORRECT CREDENTIALS"))
                                    {
                                        Toast.makeText(getApplicationContext(),"Incorrect Username or Password\nPlease Try Again",Toast.LENGTH_LONG).show();
                                        login.setClickable(true);
                                        username.setText("");
                                        password.setText("");
                                        login.setEnabled(true);
                                        login.setText("login");

                                    }
                                    else if(response.getString("message").equals("FAILED"))
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        login.setClickable(true);
                                        login.setEnabled(true);
                                        username.setText("");
                                        password.setText("");
                                        login.setText("login");

                                    }
                                    else if(response.getString("message").equals("SUCCESS"))
                                    {

                                        String type = response.getString("TYPE");
                                        Intent intent = null;
                                        switch (type)
                                        {
                                            case "DOCTOR":
                                            intent = new Intent(ValidationActivity.this, DoctorLandingActivity.class);
                                            break;
                                            case "PATIENT":
                                                intent = new Intent(ValidationActivity.this, PatientLandingActivity.class);
                                                break;
                                            case "PHARMACY":
                                                intent = new Intent(ValidationActivity.this, PharmacyLandingActivity.class);
                                                break;
                                        }




                                        login.setText("Good to go!");
                                        editor.putString("username",u);
                                        editor.putString("TYPE",type);
                                        editor.apply();
                                        editor.commit();
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Please Try again later or contact the Administrator",Toast.LENGTH_LONG).show();
                                        login.setClickable(true);
                                        login.setEnabled(true);
                                        username.setText("");
                                        password.setText("");

                                        login.setText("Sign in");
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





            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ValidationActivity.this,SignupActivity.class));

            }
        });



    }
    public boolean checkPermissions()
    {

        int internet = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        int camera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int writes = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int reads = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.e("MGG",internet+""+writes+""+reads);

        return   internet== PackageManager.PERMISSION_GRANTED
                && writes==PackageManager.PERMISSION_GRANTED
                && reads==PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(ValidationActivity.this,new String[]
                {
                        Manifest.permission.INTERNET,

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                Log.e("MGG",grantResults.length+"");
                if (grantResults.length>0)
                {
                    boolean a=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean b=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean c=grantResults[2]==PackageManager.PERMISSION_GRANTED;

                    Log.e("MGG",a+""+b+""+c+"");

                    if (a && b && c)
                        ;
                    else
                        finish();
                }
                break;
        }
    }
}
