package com.app.knbs.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.knbs.app.EndPoints;
import com.app.knbs.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Developed by Rodney on 08/11/2016.
 */

public class PostDataRequest extends IntentService {

    private static String TAG = PostDataRequest.class.getSimpleName();
    public PostDataRequest() {
        super(PostDataRequest.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String dataset = intent.getStringExtra("dataset");
            String year = intent.getStringExtra("year");

            updateFCM(name,email,dataset,year);
        }
    }

    /**
     * Posting the to server the dataset being requested by a user.
     *
     * @param name user name
     * @param email user email
     * @param dataset dataset being requested
     * @param year year of the dataset
     */
    private void updateFCM(final String name, final String email,final String dataset, final String year) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.DATASET_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray responseArray = new JSONArray(response);
                    //JSONObject responseObj = new JSONObject(response);
                    // Parsing json object response
                    // response will be a json object
                    //boolean error = responseObj.getBoolean("error");
                    String message = responseArray.get(0).toString();
                    Log.d(TAG, message+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();}
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("dataset", dataset);
                params.put("year", year);

                Log.e(TAG, "Posting params: " + params.toString());
                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

}
