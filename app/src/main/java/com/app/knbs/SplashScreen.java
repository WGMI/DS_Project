package com.app.knbs;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.app.knbs.activity.Introduction;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    //private static int SPLASH_TIME_OUT = 2000;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        new Handler().postDelayed(new Runnable() {

                        /*
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app sub_menu_sectors activity
                Intent i = new Intent(SplashScreen.this, Introduction
                        .class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 1000);

        /*
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        final JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/health/all_sectors",
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject sectorNameObject = array.getJSONObject(0);
                            JSONObject reportObject = array.getJSONObject(1);
                            JSONObject sourceObject = array.getJSONObject(2);
                            JSONObject tableObject = array.getJSONObject(3);
                            JSONObject coverageObject = array.getJSONObject(4);
                            JSONObject apiObject = array.getJSONObject(5);

                            JSONArray sectorNameArray = sectorNameObject.getJSONArray("data");
                            JSONArray reportArray = reportObject.getJSONArray("data");
                            JSONArray sourceArray = sourceObject.getJSONArray("data");
                            JSONArray tableArray = tableObject.getJSONArray("data");
                            JSONArray coverageArray = coverageObject.getJSONArray("data");
                            JSONArray apiArray = apiObject.getJSONArray("data");

                            List<String> sectors = new ArrayList<>();
                            List<String> reports = new ArrayList<>();
                            List<String> sources = new ArrayList<>();
                            List<String> tables = new ArrayList<>();
                            List<String> coverages = new ArrayList<>();
                            List<String> apis = new ArrayList<>();

                            for(int i=0;i<sectorNameArray.length();i++){
                                sectors.add(sectorNameArray.getString(i));
                                reports.add(reportArray.getString(i));
                                sources.add(sourceArray.getString(i));
                                tables.add(tableArray.getString(i));
                                coverages.add(coverageArray.getString(i));
                                apis.add(apiArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(new DatabaseHelper(context).pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("sectors",null,null);

                            for(int i=0;i<sectorNameArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("sector_name",sectors.get(i));
                                values.put("report",reports.get(i));
                                values.put("coverage",coverages.get(i));
                                values.put("source",sources.get(i));
                                values.put("table_name",tables.get(i));
                                values.put("api_url",apis.get(i));
                                values.put("favourite",0);
                                values.put("isActive",1);

                                success = db.insertOrThrow("sectors",null,values);
                            }

                            db.close();

                            Log.d("volley_tosqlite", "onResponse: " + success);

                            new Handler().postDelayed(new Runnable() {

                        /*
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */

                              //  @Override
                           //     public void run() {
                                    // This method will be executed once the timer is over
                                    // Start your app sub_menu_sectors activity
            /*
                                    Intent i = new Intent(SplashScreen.this, Introduction
                                            .class);
                                    startActivity(i);

                                    // close this activity
                                    finish();
                                }
                            }, 1000);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("volley_error", "onErrorResponse: " + volleyError.toString());
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

        */


    }

}
