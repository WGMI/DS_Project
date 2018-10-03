package com.app.knbs.services;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Muganda Imo on 9/25/2018.
 */

public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue queue;

    private VolleySingleton(Context context){
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(instance == null){
            instance = new VolleySingleton(context);
        }

        return instance;
    }

    public RequestQueue getQueue(){
        return queue;
    }
}
