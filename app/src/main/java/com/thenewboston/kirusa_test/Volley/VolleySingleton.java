package com.thenewboston.kirusa_test.Volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.thenewboston.kirusa_test.ApplicationContext.MyApplication;


public class VolleySingleton {

    // Instance of the this class
    private static VolleySingleton mInstance = null;


    private static RequestQueue mRequestQueue;

    // Constructor
    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

    }

    // Instance getter
    public static VolleySingleton getInstance() {
        if (mInstance == null) {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    // Request queue getter
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}

