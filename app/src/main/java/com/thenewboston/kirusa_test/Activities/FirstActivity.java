package com.thenewboston.kirusa_test.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thenewboston.kirusa_test.Fragments.Officer;
import com.thenewboston.kirusa_test.R;
import com.thenewboston.kirusa_test.Sqlite.Officer_Group_DbOperation;
import com.thenewboston.kirusa_test.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstActivity extends AppCompatActivity implements Officer.OnFragmentInteractionListener{

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue ;
    private ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    Map<String, String> params= new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        bindViews();

    }

    private void bindViews() {

        mToolbar = (Toolbar) findViewById(R.id.m_toolbar);

        mViewPager = (ViewPager) findViewById(R.id.m_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mToolbar != null)
            setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("FARMER FRIEND");

        setupViewPager(mViewPager);

        if (mTabLayout != null)
            mTabLayout.setupWithViewPager(mViewPager);


    }


    public void setupViewPager(ViewPager viewPager) {
        mAdapter.addFrag(new Officer(), "Officer");
        mAdapter.addFrag(new Officer(), "Farmer");
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings_logged_in:
                signOut();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
    public void signOut(){
        String URL = "https://devblogs.instavoice.com/iv?data={cmd:sign_out,api_ver:2,app_secure_key:b2ff398f8db492c19ef89b548b04889c,client_app_ver:iv.02.09.000,client_os:a,client_os_ver:6.0,iv_user_id:"+iv_user_id()+",user_secure_key:"+user_secure_key()+"}";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(sign_in_activity.this,response.toString(),Toast.LENGTH_LONG).show();
                        if(!response.toString().isEmpty())
                            parseJson(response.toString());
                        else
                            Toast.makeText(FirstActivity.this,"Wrong Input",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog


                        //Showing toast
                        Toast.makeText(FirstActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
            if(res.getString("status").equals("ok")){
                Toast.makeText(this,"Signing Out "+res.getString("request_device"),Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("logged_in",false).apply();
                startActivity(new Intent(this,sign_in_activity.class));
                finish();
            }
            else
                Toast.makeText(this,"Error Signing Out",Toast.LENGTH_SHORT).show();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main_logged_in, menu);
        return true;
    }



    static class ViewPagerAdapter extends FragmentStatePagerAdapter {


        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
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

    public void add_group(View view){
        startActivity(new Intent(this,Add_Group.class));
    }
}
