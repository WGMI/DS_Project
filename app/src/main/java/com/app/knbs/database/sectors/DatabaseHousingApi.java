package com.app.knbs.database.sectors;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.knbs.database.CountyHelper;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.ReportLoader;
import com.app.knbs.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseHousingApi {
    private Context context;
    public DatabaseHousingApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    private CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        //Incomplete
        insertInto_housing_conditions_kihibs_waste_disposal_method(d);
        insertInto_housing_conditions_kihibs_volume_of_water_used(d);
        insertInto_housing_conditions_kihibs_time_taken_to_fetch_drinking_water(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_housing_conditions_kihibs_waste_disposal_method(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("waste disposal method"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject govObject = array.getJSONObject(1);
                            JSONObject commObject = array.getJSONObject(2);
                            JSONObject prvObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray govArray = govObject.getJSONArray("data");
                            JSONArray commArray = commObject.getJSONArray("data");
                            JSONArray prvArray = prvObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_waste_disposal_method",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("method_id",i+1);
                                values.put("county_gov",govArray.getDouble(i));
                                values.put("community",commArray.getDouble(i));
                                values.put("private_co",prvArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_waste_disposal_method",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_waste_disposal_method: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_volume_of_water_used(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("volume of water used"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject to1000Object = array.getJSONObject(1);
                            JSONObject to2000Object = array.getJSONObject(2);
                            JSONObject to3000Object = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray to1Array = to1000Object.getJSONArray("data");
                            JSONArray to2Array = to2000Object.getJSONArray("data");
                            JSONArray to3Array = to3000Object.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_volume_of_water_used",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("volume_id",i+1);
                                values.put("lit_0_1000",to1Array.getDouble(i));
                                values.put("lit_1001_2000",to2Array.getDouble(i));
                                values.put("lit_2001_3000",to3Array.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_volume_of_water_used",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_volume_of_water_used: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_time_taken_to_fetch_drinking_water(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("time taken to fetch drinking water"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject zeroObject = array.getJSONObject(1);
                            JSONObject l30Object = array.getJSONObject(2);
                            JSONObject o30Object = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray zeroArray = zeroObject.getJSONArray("data");
                            JSONArray l30Array = l30Object.getJSONArray("data");
                            JSONArray o30Array = o30Object.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_time_taken_to_fetch_drinking_water",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("time_id",i+1);
                                values.put("zero",zeroArray.getDouble(i));
                                values.put("less_thirty_min",l30Array.getDouble(i));
                                values.put("over_thirty_min",o30Array.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_time_taken_to_fetch_drinking_water",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_time_taken_to_fetch_drinking_water: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

}