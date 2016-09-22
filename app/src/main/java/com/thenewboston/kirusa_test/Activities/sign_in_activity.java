package com.thenewboston.kirusa_test.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thenewboston.kirusa_test.MainActivity;
import com.thenewboston.kirusa_test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class sign_in_activity extends AppCompatActivity {

    EditText phone,password;
    RequestQueue requestQueue ;
    String ph,pwd;
    Map<String, String> params= new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_activity);
        if(get_logged_in_status()==true){
            startActivity(new Intent(this,FirstActivity.class));
            finish();
        }
        password = (EditText) findViewById(R.id.input_password);
        phone = (EditText) findViewById(R.id.input_phone);
        requestQueue = Volley.newRequestQueue(this);
    }
    public void submit ( View view ){

        ph=phone.getText().toString();
        pwd=password.getText().toString();
        if(pwd.equals("kirusa#123"))
            pwd="kirusa%23123";
        getResponse();

    }
    public void getResponse(){

                String URL = "https://devblogs.instavoice.com/iv?data={\"cmd\":\"sign_in\",\"app_secure_key\":\"b2ff398f8db492c19ef89b548b04889c\",\"client_os\":\"a\",\"client_os_ver\":\"6.0\",\"client_app_ver\":%20\"iv.02.09.000\",\"login_id\":\""+ph+"\",\"pwd\":\""+pwd+"\",\"device_id\":\"d22564a098e95c4e\"}";
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(sign_in_activity.this,response.toString(),Toast.LENGTH_LONG).show();
                        if(!response.toString().isEmpty())
                        parseJson(response.toString());
                        else
                            Toast.makeText(sign_in_activity.this,"Wrong Input",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog


                        //Showing toast
                        Toast.makeText(sign_in_activity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                        Log.e("Error", volleyError.toString());
                    }
                }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void parseJson(String response){
        try{
            JSONObject res = new JSONObject(response);
            if(res.getString("status").equals("ok")) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("logged_in",true).apply();
                sharedPreferences.edit().putString("user_secure_key", res.getString("user_secure_key")).apply();
                sharedPreferences.edit().putString("login_id", res.getString("login_id")).apply();
                sharedPreferences.edit().putString("iv_user_id", res.getString("iv_user_id")).apply();
                startActivity(new Intent(this, FirstActivity.class));
                finish();
            }
            else
                Toast.makeText(this,"Wrong Credentials.",Toast.LENGTH_LONG).show();
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    public boolean get_logged_in_status(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_details",MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("logged_in",false);
        return status;
    }

}
