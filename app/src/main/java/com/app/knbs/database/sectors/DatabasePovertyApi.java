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

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabasePovertyApi {
    private Context context;
    public DatabasePovertyApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        insertInto_poverty_consumption_expenditure_and_quintile_distribution(d);
        insertInto_poverty_distribution_of_households_by_pointofpurchasedfooditems(d);
        insertInto_poverty_distribution_of_household_food_consumption(d);
        insertInto_poverty_food_and_non_food_expenditure_per_adult_equivalent(d);
        insertInto_poverty_food_estimates_by_residence_and_county(d);

        insertInto_poverty_hardcore_estimates_by_residence_and_county(d);
        insertInto_poverty_overall_estimates_by_residence_and_count(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_poverty_consumption_expenditure_and_quintile_distribution(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("consumption expenditure and quintile distribution"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray countyArray = response.getJSONObject(0).getJSONArray("data");
                            JSONArray q1Array = response.getJSONObject(3).getJSONArray("data");
                            JSONArray q2Array = response.getJSONObject(4).getJSONArray("data");
                            JSONArray q3Array = response.getJSONObject(5).getJSONArray("data");
                            JSONArray q4Array = response.getJSONObject(6).getJSONArray("data");
                            JSONArray q5Array = response.getJSONObject(7).getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("poverty_consumption_expenditure_and_quintile_distribution",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("poverty_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("quarter1",q1Array.getDouble(i));
                                values.put("quarter2",q2Array.getDouble(i));
                                values.put("quarter3",q3Array.getDouble(i));
                                values.put("quarter4",q4Array.getDouble(i));
                                values.put("quarter5",q5Array.getDouble(i));

                                success = db.insertOrThrow("poverty_consumption_expenditure_and_quintile_distribution",null,values);
                            }

                            db.close();
                            Log.d(TAG, "poverty_consumption_expenditure_and_quintile_distribution: " + success);

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

    private void insertInto_poverty_distribution_of_households_by_pointofpurchasedfooditems(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("distribution of households by pointofpurchasedfooditems"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray countyArray = response.getJSONObject(0).getJSONArray("data");
                            JSONArray suArray = response.getJSONObject(1).getJSONArray("data");
                            JSONArray omArray = response.getJSONObject(2).getJSONArray("data");
                            JSONArray kiArray = response.getJSONObject(3).getJSONArray("data");
                            JSONArray gsArray = response.getJSONObject(4).getJSONArray("data");
                            JSONArray ssArray = response.getJSONObject(5).getJSONArray("data");
                            JSONArray isArray = response.getJSONObject(6).getJSONArray("data");
                            JSONArray ofArray = response.getJSONObject(7).getJSONArray("data");
                            JSONArray noArray = response.getJSONObject(8).getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("poverty_distribution_of_households_by_pointofpurchasedfooditems",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("poverty_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("supermarket",suArray.getDouble(i));
                                values.put("open_market",omArray.getDouble(i));
                                values.put("kiosk",kiArray.getDouble(i));
                                values.put("general_shop",gsArray.getDouble(i));
                                values.put("specialised_shop",ssArray.getDouble(i));
                                values.put("informal_sources",isArray.getDouble(i));
                                values.put("other_formal_points",ofArray.getDouble(i));
                                values.put("number_of_observations",noArray.getInt(i));

                                success = db.insertOrThrow("poverty_distribution_of_households_by_pointofpurchasedfooditems",null,values);
                            }

                            db.close();
                            Log.d(TAG, "poverty_distribution_of_households_by_pointofpurchasedfooditems: " + success);

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

    private void insertInto_poverty_distribution_of_household_food_consumption(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("distribution of household food consumption"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray countyArray = response.getJSONObject(0).getJSONArray("data");
                            JSONArray prArray = response.getJSONObject(1).getJSONArray("data");
                            JSONArray stArray = response.getJSONObject(2).getJSONArray("data");
                            JSONArray opArray = response.getJSONObject(3).getJSONArray("data");
                            JSONArray giArray = response.getJSONObject(4).getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("poverty_distribution_of_household_food_consumption",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("poverty_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("purchases",prArray.getDouble(i));
                                values.put("stock",stArray.getDouble(i));
                                values.put("own_production",opArray.getDouble(i));
                                values.put("gifts",giArray.getDouble(i));

                                success = db.insertOrThrow("poverty_distribution_of_household_food_consumption",null,values);
                            }

                            db.close();
                            Log.d(TAG, "poverty_distribution_of_household_food_consumption: " + success);

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

    private void insertInto_poverty_food_and_non_food_expenditure_per_adult_equivalent(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("food and non food expenditure per adult equivalent"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray countyArray = response.getJSONObject(0).getJSONArray("data");
                            JSONArray fdArray = response.getJSONObject(1).getJSONArray("data");
                            JSONArray nfArray = response.getJSONObject(2).getJSONArray("data");
                            JSONArray catArray = response.getJSONObject(3).getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("poverty_food_and_non_food_expenditure_per_adult_equivalent",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("poverty_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("food",fdArray.getDouble(i));
                                values.put("non_food",nfArray.getDouble(i));
                                values.put("category",catArray.getString(i));

                                success = db.insertOrThrow("poverty_food_and_non_food_expenditure_per_adult_equivalent",null,values);
                            }

                            db.close();
                            Log.d(TAG, "poverty_food_and_non_food_expenditure_per_adult_equivalent: " + success);

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

    private void insertInto_poverty_food_estimates_by_residence_and_county(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("food estimates by residence and county"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray countyArray = response.getJSONObject(0).getJSONArray("data");
                            JSONArray hcArray = response.getJSONObject(1).getJSONArray("data");
                            JSONArray dsArray = response.getJSONObject(2).getJSONArray("data");
                            JSONArray pgArray = response.getJSONObject(3).getJSONArray("data");
                            JSONArray spArray = response.getJSONObject(4).getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("poverty_food_estimates_by_residence_and_county",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("poverty_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("headcount_rate",hcArray.getDouble(i));
                                values.put("distribution_of_the_poor",dsArray.getDouble(i));
                                values.put("poverty_gap",pgArray.getDouble(i));
                                values.put("severity_of_poverty",spArray.getDouble(i));

                                success = db.insertOrThrow("poverty_food_estimates_by_residence_and_county",null,values);
                            }

                            db.close();
                            Log.d(TAG, "poverty_food_estimates_by_residence_and_county: " + success);

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

    private void insertInto_poverty_hardcore_estimates_by_residence_and_county(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("hardcore estimates by residence and county"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray countyArray = response.getJSONObject(0).getJSONArray("data");
                            JSONArray hcArray = response.getJSONObject(1).getJSONArray("data");
                            JSONArray dsArray = response.getJSONObject(2).getJSONArray("data");
                            JSONArray pgArray = response.getJSONObject(3).getJSONArray("data");
                            JSONArray spArray = response.getJSONObject(4).getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("poverty_hardcore_estimates_by_residence_and_county",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("poverty_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("headcount_rate",hcArray.getDouble(i));
                                values.put("distribution_of_the_poor",dsArray.getDouble(i));
                                values.put("poverty_gap",pgArray.getDouble(i));
                                values.put("severity_of_poverty",spArray.getDouble(i));

                                success = db.insertOrThrow("poverty_hardcore_estimates_by_residence_and_county",null,values);
                            }

                            db.close();
                            Log.d(TAG, "poverty_hardcore_estimates_by_residence_and_county: " + success);

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

    private void insertInto_poverty_overall_estimates_by_residence_and_count(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("overall estimates by residence and county"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray countyArray = response.getJSONObject(0).getJSONArray("data");
                            JSONArray hcArray = response.getJSONObject(1).getJSONArray("data");
                            JSONArray dsArray = response.getJSONObject(2).getJSONArray("data");
                            JSONArray pgArray = response.getJSONObject(3).getJSONArray("data");
                            JSONArray spArray = response.getJSONObject(4).getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("poverty_overall_estimates_by_residence_and_count",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("poverty_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("headcount_rate",hcArray.getDouble(i));
                                values.put("distribution_of_the_poor",dsArray.getDouble(i));
                                values.put("poverty_gap",pgArray.getDouble(i));
                                values.put("severity_of_poverty",spArray.getDouble(i));

                                success = db.insertOrThrow("poverty_overall_estimates_by_residence_and_count",null,values);
                            }

                            db.close();
                            Log.d(TAG, "poverty_overall_estimates_by_residence_and_count: " + success);

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