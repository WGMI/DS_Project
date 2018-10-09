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
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseBuildingConstructionApi {
    private Context context;
    public DatabaseBuildingConstructionApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        insertInto_building_and_construction_quarterly_non_residential_build_cost(d);
        insertInto_building_and_construction_quarterly_overal_construction_cost(d);
        insertInto_building_and_construction_quarterly_residential_bulding_cost(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    //Check table
    private void insertInto_building_and_construction_amount(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/building/all_quarterly_civil_engineering_cost_index",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject marchObject  = response.getJSONObject(2);
                            JSONObject juneObject  = response.getJSONObject(3);
                            JSONObject septemberObject  = response.getJSONObject(4);
                            JSONObject decemberObject  = response.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray marchArray = marchObject.getJSONArray("data");
                            JSONArray juneArray = juneObject.getJSONArray("data");
                            JSONArray septemberArray = septemberObject.getJSONArray("data");
                            JSONArray decemberArray = decemberObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("building_and_construction_amount",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("buildingandconstruction_id",i+1);

                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("building_and_construction_amount",null,values);
                            }

                            db.close();
                            Log.d(TAG, "building_and_construction_amount: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_building_and_construction_quarterly_non_residential_build_cost(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/building/all__quarterly_non_residential_build_cost",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject marchObject  = response.getJSONObject(2);
                            JSONObject juneObject  = response.getJSONObject(3);
                            JSONObject septemberObject  = response.getJSONObject(4);
                            JSONObject decemberObject  = response.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray marchArray = marchObject.getJSONArray("data");
                            JSONArray juneArray = juneObject.getJSONArray("data");
                            JSONArray septemberArray = septemberObject.getJSONArray("data");
                            JSONArray decemberArray = decemberObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("building_and_construction_quarterly_non_residential_build_cost",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("cost_index_id",i+1);
                                values.put("category",categoryArray.getString(i));
                                values.put("march",marchArray.getDouble(i));
                                values.put("june",juneArray.getDouble(i));
                                values.put("sept",septemberArray.getDouble(i));
                                values.put("december",decemberArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("building_and_construction_quarterly_non_residential_build_cost",null,values);
                            }

                            db.close();
                            Log.d(TAG, "building_and_construction_quarterly_non_residential_build_cost: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_building_and_construction_quarterly_overal_construction_cost(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/building/all_quarterly_overal_construction_cost",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject marchObject  = response.getJSONObject(2);
                            JSONObject juneObject  = response.getJSONObject(3);
                            JSONObject septemberObject  = response.getJSONObject(4);
                            JSONObject decemberObject  = response.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray marchArray = marchObject.getJSONArray("data");
                            JSONArray juneArray = juneObject.getJSONArray("data");
                            JSONArray septemberArray = septemberObject.getJSONArray("data");
                            JSONArray decemberArray = decemberObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("building_and_construction_quarterly_overal_construction_cost",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("category",categoryArray.getString(i));
                                values.put("march",marchArray.getDouble(i));
                                values.put("june",juneArray.getDouble(i));
                                values.put("sept",septemberArray.getDouble(i));
                                values.put("december",decemberArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("building_and_construction_quarterly_overal_construction_cost",null,values);
                            }

                            db.close();
                            Log.d(TAG, "building_and_construction_quarterly_overal_construction_cost: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_building_and_construction_quarterly_residential_bulding_cost(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/building/all_quarterly_residential_bulding_cost",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject marchObject  = response.getJSONObject(2);
                            JSONObject juneObject  = response.getJSONObject(3);
                            JSONObject septemberObject  = response.getJSONObject(4);
                            JSONObject decemberObject  = response.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray marchArray = marchObject.getJSONArray("data");
                            JSONArray juneArray = juneObject.getJSONArray("data");
                            JSONArray septemberArray = septemberObject.getJSONArray("data");
                            JSONArray decemberArray = decemberObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("building_and_construction_quarterly_residential_bulding_cost",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("building_construction_id",i+1);
                                values.put("category",categoryArray.getString(i));
                                values.put("march",marchArray.getDouble(i));
                                values.put("june",juneArray.getDouble(i));
                                values.put("september",septemberArray.getDouble(i));
                                values.put("december",decemberArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("building_and_construction_quarterly_residential_bulding_cost",null,values);
                            }

                            db.close();
                            Log.d(TAG, "building_and_construction_quarterly_residential_bulding_cost: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

}