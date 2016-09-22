package com.thenewboston.kirusa_test.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.thenewboston.kirusa_test.Activities.Add_Group;
import com.thenewboston.kirusa_test.Adapters.message_adapter;
import com.thenewboston.kirusa_test.Adapters.office_adapter;
import com.thenewboston.kirusa_test.ApplicationContext.MyApplication;
import com.thenewboston.kirusa_test.PojoClasses.message_object;
import com.thenewboston.kirusa_test.PojoClasses.officers_group;
import com.thenewboston.kirusa_test.R;
import com.thenewboston.kirusa_test.Sqlite.Officer_Group_DbOperation;
import com.thenewboston.kirusa_test.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Farmer extends Fragment {

    private RecyclerView recyclerView;
    message_adapter messageAdapter;
    private ArrayList<message_object> message_objects = new ArrayList<>();
    String[] numbers = { "918900000001","918900000002","919742219421"};
    JSONObject params_send_text = new JSONObject();
    RequestQueue requestQueue;
    VolleySingleton volleySingleton;
    private Officer_Group_DbOperation officer_group_dbOperation;
    MyApplication myApplication;
    private Button button,send ;
    EditText gid,mes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_farmer, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.message_farmers);
        myApplication = MyApplication.getInstance();
        officer_group_dbOperation = new Officer_Group_DbOperation(myApplication.getApplicationContext());
        button = (Button) view.findViewById(R.id.refresh_message);
        send = (Button) view.findViewById(R.id.send_button);
        gid = (EditText) view.findViewById(R.id.message_group_id);
        mes = (EditText) view.findViewById(R.id.message_box);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        message_objects = officer_group_dbOperation.read_message(officer_group_dbOperation);
        messageAdapter = new message_adapter(getActivity().getBaseContext(),message_objects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(messageAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message_objects = officer_group_dbOperation.read_message(officer_group_dbOperation);
                messageAdapter.setFeed(message_objects);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mes.getText().toString().isEmpty()&&!gid.getText().toString().isEmpty())
                    send_text_message();
                else
                    Toast.makeText(myApplication.getApplicationContext(),"Fields Empty!",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    public void send_text_message(){
        String group_id = gid.getText().toString();
        String message = mes.getText().toString();
        try {
            params_send_text.put("guid", "216940--4222389732955633961-1474458886323");
            params_send_text.put("msg_text",message);
            params_send_text.put("api_ver","2");
            params_send_text.put("app_secure_key","b2ff398f8db492c19ef89b548b04889c");
            params_send_text.put("client_app_ver","iv.02.09.001");
            params_send_text.put("client_os","a");
            params_send_text.put("client_os_ver","6.0.1");
            params_send_text.put("cmd","send_text");
            params_send_text.put("iv_user_id",iv_user_id());
            params_send_text.put("user_secure_key",user_secure_key());
            JSONArray jsonArray = new JSONArray();
            JSONObject from = new JSONObject();
            from.put("type","iv");
            from.put("contact",iv_user_id());
            jsonArray.put(from);
            JSONObject to = new JSONObject();
            to.put("type","g");
            to.put("contact",group_id);
            jsonArray.put(to);
            params_send_text.put("contact_ids",jsonArray);
            Log.e("ADARSH",params_send_text.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        try {
            String URL = "https://devblogs.instavoice.com/iv?data=" + URLEncoder.encode(params_send_text.toString(), "UTF-8");
            Map<String, String> params = new HashMap<String, String>();
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //Toast.makeText(sign_in_activity.this,response.toString(),Toast.LENGTH_LONG).show();
                            if (!response.toString().isEmpty())
                                parseJson(response.toString());
                            else
                                Toast.makeText(getActivity().getBaseContext(), "Wrong Input", Toast.LENGTH_LONG).show();
                            gid.setText("");
                            mes.setText("");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Showing toast
                            Toast.makeText(getActivity().getBaseContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
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
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }


    }
    public void parseJson(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString("status").equals("ok")){
                insert_message();
            }
            else if(jsonObject.getString("status").equals("error"))
                Toast.makeText(getActivity().getBaseContext(),jsonObject.getString("error_reason"),Toast.LENGTH_LONG).show();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void insert_message(){
        String group_id = gid.getText().toString();
        String message = mes.getText().toString();
        officer_group_dbOperation.insert_message(officer_group_dbOperation,new message_object(group_id,message));
    }
    public String user_secure_key() {

        SharedPreferences sharedPreferences = myApplication.getApplicationContext().getSharedPreferences("user_details",MODE_PRIVATE);
        String user_secure_key = sharedPreferences.getString("user_secure_key", null);

        if (!user_secure_key.isEmpty()) {
            return user_secure_key;
        } else
            return null;
    }
    public String iv_user_id(){
        SharedPreferences sharedPreferences = myApplication.getApplicationContext().getSharedPreferences("user_details",MODE_PRIVATE);
        String iv_user_id = sharedPreferences.getString("iv_user_id",null);
        if(!iv_user_id.isEmpty())
            return iv_user_id;
        else
            return null;
    }

}
