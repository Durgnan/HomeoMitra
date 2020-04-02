package com.homeomitra.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
                        String login_url = "INSERT_URL_HERE";
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

                                        Intent intent = new Intent(ValidationActivity.this, MainActivity.class);


                                        login.setText("Good to go!");
                                        editor.putString("username",u);
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
}
