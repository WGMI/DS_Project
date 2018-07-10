package com.app.knbs.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.knbs.app.EndPoints;
import com.app.knbs.app.MyApplication;
import com.app.knbs.database.DBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Application Created by Rodney on 14-Sep-16.
 */
public class LoadTableData extends IntentService {
    DBHandler handler;

    public LoadTableData() {
        super(LoadTableData.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        handler = new DBHandler(getApplicationContext());

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new DownloadBranches().execute();
            }
        });
        thread.start();

    }

    private class DownloadBranches extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showing refresh animation before making http call
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("URL", EndPoints.BASE_URL);
            JsonArrayRequest req = new JsonArrayRequest(EndPoints.BASE_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("Response", response.toString());
                            if (response.length() > 0) {
                                // looping through json and adding to list
                                for (int i = 0; i < response.length(); i++) {

                                    try {

                                        JSONObject branchObj = response.getJSONObject(i);

                                        String branch_id = branchObj.getString("branch_id");
                                        String business_id = branchObj.getString("business_id");
                                        String branch_name = branchObj.getString("branch_name");
                                        String info = branchObj.getString("info_branch");
                                        String open_hours = branchObj.getString("open_hours");
                                        String location = branchObj.getString("location");
                                        String phonenumber = branchObj.getString("number");
                                        double lat = branchObj.getDouble("latitude");
                                        double lng = branchObj.getDouble("longitude");

                                        //Business business = new Business(branch_id,business_id,branch_name,info,open_hours,location,phonenumber,lat,lng) ;
                                        //handler.addBranch(business);

                                    } catch (JSONException e) {
                                        // hiding the progress bar
                                        e.printStackTrace();
                                    }
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //error.printStackTrace();

                }
            });

            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(req);

            return null;
        }
        @Override
        protected void onPostExecute(Void args) {

        }
    }

}
