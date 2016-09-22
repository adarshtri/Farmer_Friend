package com.thenewboston.kirusa_test.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.thenewboston.kirusa_test.R;
import com.thenewboston.kirusa_test.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Group_Update extends AppCompatActivity {

    EditText phone;
    Button add,delete;
    JSONObject update_params = new JSONObject();
    String group_name,group_id;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__update);
        phone = (EditText) findViewById(R.id.update_group_phone);
        add = (Button) findViewById(R.id.update_group_button);
        delete = (Button) findViewById(R.id.update_group_delete);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        Bundle extras = getIntent().getExtras();
        group_name = extras.getString("group_name");
        group_id = extras.getString("group_id");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText().toString().isEmpty())
                    Toast.makeText(Group_Update.this,"Phone Field Empty",Toast.LENGTH_SHORT).show();
                else
                    update_group();
            }
        });
    }
    public void update_group(){
        try{
            update_params.put("app_secure_key","b2ff398f8db492c19ef89b548b04889c");
            update_params.put("client_app_ver","iv.02.09.000");
            update_params.put("client_os","a");
            update_params.put("client_os_ver","6.0");
            update_params.put("cmd","update_group");
            update_params.put("iv_user_id",iv_user_id());
            update_params.put("user_secure_key",user_secure_key());
            update_params.put("group_id",group_id);
            JSONArray jsonArray = new JSONArray();
            JSONObject member = new JSONObject();
            member.put("type","tel");
            member.put("contact",phone.getText().toString());
            jsonArray.put(member);
            update_params.put("member_updates",jsonArray);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String URL = "https://devblogs.instavoice.com/iv?data="+update_params.toString();
        Log.e("ADARSH",URL);
        Map<String, String> params= new HashMap<String, String>();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(sign_in_activity.this,response.toString(),Toast.LENGTH_LONG).show();
                        if(!response.toString().isEmpty())
                            parseJsonUpdateGroup(response.toString());
                        else
                            Toast.makeText(Group_Update.this,"Wrong Input",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Showing toast
                        Toast.makeText(Group_Update.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
    public void parseJsonUpdateGroup(String response){
        Toast.makeText(this,response,Toast.LENGTH_LONG).show();
        Log.e("ADARSH",response);
        try{
            JSONObject res = new JSONObject(response);
            if(res.getString("status").equals("ok")){
                Toast.makeText(this,"Group Updated",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"Couldn't Update Group",Toast.LENGTH_LONG).show();

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public String user_secure_key() {

        SharedPreferences sharedPreferences = this.getSharedPreferences("user_details",MODE_PRIVATE);
        String user_secure_key = sharedPreferences.getString("user_secure_key", null);

        if (!user_secure_key.isEmpty()) {
            return user_secure_key;
        } else
            return null;
    }
    public String iv_user_id(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_details",MODE_PRIVATE);
        String iv_user_id = sharedPreferences.getString("iv_user_id",null);
        if(!iv_user_id.isEmpty())
            return iv_user_id;
        else
            return null;
    }
}
