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

public class DatabaseLabourApi {
    private Context context;
    public DatabaseLabourApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        insertInto_labour_average_wage_earnings_per_employee_public_sector(d);
        insertInto_labour_memorandum_items_in_public_sector(d);
        insertInto_labour_total_recorded_employment(d);
        insertInto_labour_wage_employment_by_industry_and_sex(d);
        insertInto_labour_wage_employment_by_industry_in_private_sector(d);
        insertInto_labour_wage_employment_by_industry_in_public_sector(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    //Check table name
    private void insertInto_labour_average_wage_earnings_per_employee_private_sector(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/energy/all_average_retail_prices_of_selected_petroleum_products",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject sectorObject  = response.getJSONObject(1);
                            JSONObject wageObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray sectorArray = sectorObject.getJSONArray("data");
                            JSONArray wageArray = wageObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("labour_total_recorded_employment",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("retail_price_id",i+1);
                                /*values.put("petroleum_product",productArray.getString(i));
                                values.put("retail_price_ksh",priceArray.getDouble(i));
                                values.put("month",monthArray.getString(i));*/
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("labour_total_recorded_employment",null,values);
                            }

                            db.close();
                            Log.d(TAG, "labour_total_recorded_employment: " + success);

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

    private void insertInto_labour_average_wage_earnings_per_employee_public_sector(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/labour/all_labour_average_wage_earnings_per_employee_public_sector",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject sectorObject  = response.getJSONObject(1);
                            JSONObject wageObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray sectorArray = sectorObject.getJSONArray("data");
                            JSONArray wageArray = wageObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("average_wage_earnings_per_employee_public_sector",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("wage_earnings_id",i+1);
                                values.put("public_sector",sectorArray.getString(i));
                                values.put("wage_earnings",wageArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("average_wage_earnings_per_employee_public_sector",null,values);
                            }

                            db.close();
                            Log.d(TAG, "labour_average_wage_earnings_per_employee_public_sector: " + success);

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

    private void insertInto_labour_memorandum_items_in_public_sector(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/labour/all_labour_memorandum_items_in_public_sector",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject itemsObject  = response.getJSONObject(1);
                            JSONObject wageObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray itemsArray = itemsObject.getJSONArray("data");
                            JSONArray wageArray = wageObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("memorandum_items_in_public_sector",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("memorandum_item_id",i+1);
                                values.put("memorandum_item",itemsArray.getString(i));
                                values.put("wage_earnings",wageArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("memorandum_items_in_public_sector",null,values);
                            }

                            db.close();
                            Log.d(TAG, "memorandum_items_in_public_sector: " + success);

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

    private void insertInto_labour_total_recorded_employment(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/labour/all_labour_total_recorded_employment",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject sectorObject  = response.getJSONObject(1);
                            JSONObject totalObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray sectorArray = sectorObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("labour_total_recorded_employment",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("total_employment_id",i+1);
                                values.put("sector_category",sectorArray.getString(i));
                                values.put("total_employment",totalArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("labour_total_recorded_employment",null,values);
                            }

                            db.close();
                            Log.d(TAG, "labour_total_recorded_employment: " + success);

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

    private void insertInto_labour_wage_employment_by_industry_and_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/labour/all_labour_wage_employment_by_industry_and_sex",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject industryObject  = response.getJSONObject(1);
                            JSONObject wageObject  = response.getJSONObject(2);
                            JSONObject genderObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray industryArray = industryObject.getJSONArray("data");
                            JSONArray wageArray = wageObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("labour_wage_employment_by_industry_and_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("wage_employment_id",i+1);
                                values.put("industry",industryArray.getString(i));
                                values.put("wage_employment",wageArray.getInt(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("labour_wage_employment_by_industry_and_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "labour_wage_employment_by_industry_and_sex: " + success);

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

    private void insertInto_labour_wage_employment_by_industry_in_private_sector(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/labour/all_labour_wage_employment_by_industry_and_sex",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject sectorObject  = response.getJSONObject(1);
                            JSONObject wageObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray sectorArray = sectorObject.getJSONArray("data");
                            JSONArray wageArray = wageObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("labour_wage_employment_by_industry_in_private_sector",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("wage_employment_id",i+1);
                                values.put("private_sector",sectorArray.getString(i));
                                values.put("wage_employment",wageArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("labour_wage_employment_by_industry_in_private_sector",null,values);
                            }

                            db.close();
                            Log.d(TAG, "labour_wage_employment_by_industry_in_private_sector: " + success);

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

    private void insertInto_labour_wage_employment_by_industry_in_public_sector(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/labour/all_labour_wage_employment_by_industry_in_public_sector",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject sectorObject  = response.getJSONObject(1);
                            JSONObject wageObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray sectorArray = sectorObject.getJSONArray("data");
                            JSONArray wageArray = wageObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("labour_wage_employment_by_industry_in_public_sector",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("wage_employment_id",i+1);
                                values.put("public_sector",sectorArray.getString(i));
                                values.put("wage_employment",wageArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("labour_wage_employment_by_industry_in_public_sector",null,values);
                            }

                            db.close();
                            Log.d(TAG, "labour_wage_employment_by_industry_in_public_sector: " + success);

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