package com.thenewboston.kirusa_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.LoudnessEnhancer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue ;
    Map<String,JSONObject> params  = new HashMap<>();
    Map<String, String> postParam= new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        getResponse();

    }
    public void getResponse(){
        String URL = "https://devblogs.instavoice.com/iv?data={\"cmd\":\"reg_user\",\"phone_num\":\"919742219423\",\"pwd\":\"adarsh\",\"app_secure_key\":\"b2ff398f8db492c19ef89b548b04889c\",\"client_os\":\"a\",\"client_os_ver\":\"6.0\",\"client_app_ver\":\"iv.01.01.001\",\"device_id\":\"d22564a098e95c4e\",\"phone_num_edited\":\"true\",\"opr_info_edited\":\"true\",\"sim_serial_num\":\"\"}";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog


                        //Showing toast
                        Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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

}
