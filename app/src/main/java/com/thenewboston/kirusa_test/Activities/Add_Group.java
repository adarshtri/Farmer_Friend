package com.thenewboston.kirusa_test.Activities;

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
import com.thenewboston.kirusa_test.PojoClasses.officers_group;
import com.thenewboston.kirusa_test.R;
import com.thenewboston.kirusa_test.Sqlite.Officer_Group_DbOperation;
import com.thenewboston.kirusa_test.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Group extends AppCompatActivity {


    EditText group_name;
    String[] numbers = { "918900000001","918900000002","919742219421"};
    JSONObject params_create_group = new JSONObject();
    JSONObject params_check_numbers = new JSONObject();
    RequestQueue requestQueue;
    VolleySingleton volleySingleton;
    Officer_Group_DbOperation officer_group_dbOperation;
    String gna,gid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__group);
        group_name = (EditText) findViewById(R.id.group_activity_group_name);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        officer_group_dbOperation = new Officer_Group_DbOperation(this);
    }

    public void submit_add_group(View view){
        if(group_name.getText().toString().isEmpty())
            Toast.makeText(this,"Group Name Can't be Empty",Toast.LENGTH_SHORT).show();
        else
        {



            try {
                params_check_numbers.put("cmd","enquire_iv_users");
                params_check_numbers.put("api_ver","2");
                params_check_numbers.put("app_secure_key","b2ff398f8db492c19ef89b548b04889c");
                params_check_numbers.put("client_app_ver","iv.02.09.000");
                params_check_numbers.put("client_os","a");
                params_check_numbers.put("client_os_ver","6.0");
                params_check_numbers.put("iv_user_id",iv_user_id());
                params_check_numbers.put("user_secure_key",user_secure_key());
                params_check_numbers.put("clear_address_book","false");
                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<numbers.length;i++)
                    jsonArray.put(numbers[i]);
                params_check_numbers.put("contact_ids",jsonArray);
                Log.e("ADARSH","https://devblogs.instavoice.com/iv?data="+params_check_numbers.toString());
                getResponse();





            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
    public void getResponse(){
        String URL = "https://devblogs.instavoice.com/iv?data="+params_check_numbers.toString();
        Map<String, String> params= new HashMap<String, String>();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(sign_in_activity.this,response.toString(),Toast.LENGTH_LONG).show();
                        if(!response.toString().isEmpty())
                            parseJson(response.toString());
                        else
                            Toast.makeText(Add_Group.this,"Wrong Input",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Showing toast
                        Toast.makeText(Add_Group.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
        Toast.makeText(this,response,Toast.LENGTH_LONG).show();
        try{
            ArrayList<String> iv_user_id = new ArrayList<>();
            JSONObject res = new JSONObject(response);
            if(res.getString("status").equals("ok"))
            {
                JSONArray contact_ids = res.getJSONArray("iv_contact_ids");
                for(int i=0;i<contact_ids.length();i++)
                    iv_user_id.add(contact_ids.getJSONObject(i).getString("iv_user_id"));
                Send_Create_Group(iv_user_id);

            }
            else
                Toast.makeText(this,"Error Verifying Numbers",Toast.LENGTH_LONG).show();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void Send_Create_Group(ArrayList<String> iv_user_id){
           /*

    https://devblogs.instavoice.com/iv?data={cmd:create_group,app_secure_key:b2ff398f8db492c19ef89b548b04889c,
    client_app_ver:iv.02.09.000,client_os:a,client_os_ver:6.0,iv_user_id:17198707,
    user_secure_key:b2ff39178db49257848bf7daccdf291a841ef8e7cc52d7a95f0129f54aa99fa659f89b547d04889c,
    group_type:2,group_desc:timepasss,member_contacts:[{contact:17198707,type:iv}]}
     */
        String g_n = group_name.getText().toString();
        try {
            params_create_group.put("cmd", "create_group");
            params_create_group.put("app_secure_key", "b2ff398f8db492c19ef89b548b04889c");
            params_create_group.put("client_app_ver", "iv.02.09.000");
            params_create_group.put("client_os", "a");
            params_create_group.put("client_os_ver", "6.0");
            params_create_group.put("iv_user_id", iv_user_id());
            params_create_group.put("user_secure_key", user_secure_key());
            params_create_group.put("group_type", "2");
            params_create_group.put("group_desc", g_n);
            JSONArray uids = new JSONArray();
            for(int i=0;i<iv_user_id.size();i++) {
                JSONObject temp = new JSONObject();
                temp.put("type","iv");
                temp.put("contact",iv_user_id.get(i));
                uids.put(temp);
            }
            params_create_group.put("member_contacts",uids);
            getResponseForCreateGroup();
        }catch (JSONException e){
            e.printStackTrace();
        }


    }
    public void getResponseForCreateGroup(){
        String URL = "https://devblogs.instavoice.com/iv?data="+params_create_group.toString();
        Log.e("ADARSH",URL);
        Map<String, String> params= new HashMap<String, String>();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(sign_in_activity.this,response.toString(),Toast.LENGTH_LONG).show();
                        if(!response.toString().isEmpty())
                            parseJsonCreateGroup(response.toString());
                        else
                            Toast.makeText(Add_Group.this,"Wrong Input",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Showing toast
                        Toast.makeText(Add_Group.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
    public void parseJsonCreateGroup(String response){
        Toast.makeText(this,response,Toast.LENGTH_LONG).show();
        Log.e("ADARSH",response);
        try{
            JSONObject res = new JSONObject(response);
            if(res.getString("status").equals("ok")){
                gna=group_name.getText().toString();
                gid = res.getString("group_id");
                insert_values();
            }
            else
                Toast.makeText(this,"Couldn't Create Group",Toast.LENGTH_LONG).show();

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void insert_values(){
        officers_group og = new officers_group(gid,gna);
        Log.e("ADARSH","ACT "+og.getGroup_id()+" "+og.getGroup_id());
        officer_group_dbOperation.insert_group(officer_group_dbOperation,og);
        finish();
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
