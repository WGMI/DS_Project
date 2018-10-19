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
import com.app.knbs.services.ReportLoader;
import com.app.knbs.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseCPIApi {
    private Context context;
    public DatabaseCPIApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);

    public void loadData(final ProgressDialog d){
        insertInto_cpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya(d);
        insertInto_cpi_consumer_price_index(d);
        insertInto_cpi_elementary_aggregates_weights_in_the_cpi_baskets(d);
        insertInto_cpi_group_weights_for_kenya_cpi_febuary_base_2009(d);
        insertInto_cpi_group_weights_for_kenya_cpi_october_base_1997(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_cpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Annual Average Retail Prices of Certain Consumer Goods in Kenya"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject itemObject  = response.getJSONObject(1);
                            JSONObject unitObject  = response.getJSONObject(2);
                            JSONObject kesObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray itemArray = itemObject.getJSONArray("data");
                            JSONArray unitArray = unitObject.getJSONArray("data");
                            JSONArray kesArray = kesObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("cpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("avg_retail_price_id",i+1);
                                values.put("item",itemArray.getString(i));
                                values.put("unit",unitArray.getString(i));
                                values.put("ksh_per_unit",Float.parseFloat(String.valueOf(kesArray.get(i))));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("cpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya",null,values);
                            }

                            db.close();
                            Log.d(TAG, "cpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya: " + success);

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

    private void insertInto_cpi_consumer_price_index(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Consumer Price Index"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject monthObject  = response.getJSONObject(1);
                            JSONObject groupObject  = response.getJSONObject(2);
                            JSONObject foodAndNABevObject  = response.getJSONObject(3);
                            JSONObject alcoholicBevAndTobbObject  = response.getJSONObject(4);
                            JSONObject clothAndFoodObject  = response.getJSONObject(5);
                            JSONObject gasFuelsObject  = response.getJSONObject(6);
                            JSONObject furnishObject  = response.getJSONObject(7);
                            JSONObject healthObject  = response.getJSONObject(8);
                            JSONObject transportObject  = response.getJSONObject(9);
                            JSONObject commsObject  = response.getJSONObject(10);
                            JSONObject recreationObject  = response.getJSONObject(11);
                            JSONObject restaurantObject  = response.getJSONObject(12);
                            JSONObject miscObject  = response.getJSONObject(13);
                            JSONObject educationObject  = response.getJSONObject(14);
                            JSONObject totalObject  = response.getJSONObject(15);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray monthArray = monthObject.getJSONArray("data");
                            JSONArray groupArray = groupObject.getJSONArray("data");
                            JSONArray foodAndNABevArray = foodAndNABevObject.getJSONArray("data");
                            JSONArray alcoholicBevAndTobbArray = alcoholicBevAndTobbObject.getJSONArray("data");
                            JSONArray clothAndFoodArray = clothAndFoodObject.getJSONArray("data");
                            JSONArray gasFuelsArray = gasFuelsObject.getJSONArray("data");
                            JSONArray furnishArray = furnishObject.getJSONArray("data");
                            JSONArray healthArray = healthObject.getJSONArray("data");
                            JSONArray transportArray = transportObject.getJSONArray("data");
                            JSONArray commsArray = commsObject.getJSONArray("data");
                            JSONArray recreationArray = recreationObject.getJSONArray("data");
                            JSONArray restaurantArray = restaurantObject.getJSONArray("data");
                            JSONArray miscArray = miscObject.getJSONArray("data");
                            JSONArray educationArray = educationObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("cpi_consumer_price_index",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("cpi_retail_price_id",i+1);
                                values.put("month",monthArray.getString(i));
                                values.put("year",yearArray.getInt(i));
                                values.put("income_group",groupArray.getString(i));
                                values.put("food_and_non_alcoholic_beverages",Float.parseFloat(String.valueOf(foodAndNABevArray.get(i))));
                                values.put("alcoholic_beverages_tobacco_narcotics",Float.parseFloat(String.valueOf(alcoholicBevAndTobbArray.get(i))));
                                values.put("clothing_and_footwear",Float.parseFloat(String.valueOf(clothAndFoodArray.get(i))));
                                values.put("housing_water_electricity_gas_and_other_fuels",Float.parseFloat(String.valueOf(gasFuelsArray.get(i))));
                                values.put("furnishings_household_equipment_routine_household_maintenance",Float.parseFloat(String.valueOf(furnishArray.get(i))));
                                values.put("health",Float.parseFloat(String.valueOf(healthArray.get(i))));
                                values.put("transport",Float.parseFloat(String.valueOf(transportArray.get(i))));
                                values.put("communication",Float.parseFloat(String.valueOf(commsArray.get(i))));
                                values.put("recreation_and_culture",Float.parseFloat(String.valueOf(recreationArray.get(i))));
                                values.put("education",Float.parseFloat(String.valueOf(educationArray.get(i))));
                                values.put("restaurant_and_hotels",Float.parseFloat(String.valueOf(restaurantArray.get(i))));
                                values.put("miscellaneous_goods_and_services",Float.parseFloat(String.valueOf(miscArray.get(i))));
                                values.put("total",Float.parseFloat(String.valueOf(totalArray.get(i))));

                                success = db.insertOrThrow("cpi_consumer_price_index",null,values);
                            }

                            db.close();
                            Log.d(TAG, "cpi_consumer_price_index: " + success);

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

    private void insertInto_cpi_elementary_aggregates_weights_in_the_cpi_baskets(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Elementary Aggregates Weights in the CPI Baskets"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject coicopObject  = response.getJSONObject(0);
                            JSONObject descObject  = response.getJSONObject(1);
                            JSONObject lowerObject  = response.getJSONObject(2);
                            JSONObject middleObject  = response.getJSONObject(3);
                            JSONObject upperObject  = response.getJSONObject(4);
                            JSONObject urbanObject  = response.getJSONObject(5);
                            JSONObject kenyaObject  = response.getJSONObject(6);

                            JSONArray coicopArray = coicopObject.getJSONArray("data");
                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray lowerArray = lowerObject.getJSONArray("data");
                            JSONArray middleArray = middleObject.getJSONArray("data");
                            JSONArray upperArray = upperObject.getJSONArray("data");
                            JSONArray urbanArray = urbanObject.getJSONArray("data");
                            JSONArray kenyaArray = kenyaObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("cpi_elementary_aggregates_weights_in_the_cpi_baskets",null,null);

                            for(int i=0;i<coicopArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("aggregate_weights_id",i+1);
                                values.put("coicop_code",coicopArray.getString(i));
                                values.put("description",descArray.getString(i));
                                values.put("nairobi_lower",Float.parseFloat(String.valueOf(lowerArray.get(i))));
                                values.put("nairobi_middle",Float.parseFloat(String.valueOf(middleArray.get(i))));
                                values.put("nairobi_upper",Float.parseFloat(String.valueOf(upperArray.get(i))));
                                values.put("rest_of_urban_areas",Float.parseFloat(String.valueOf(urbanArray.get(i))));
                                values.put("kenya",Float.parseFloat(String.valueOf(kenyaArray.get(i))));

                                success = db.insertOrThrow("cpi_elementary_aggregates_weights_in_the_cpi_baskets",null,values);
                            }

                            db.close();
                            Log.d(TAG, "cpi_elementary_aggregates_weights_in_the_cpi_baskets: " + success);

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

    private void insertInto_cpi_group_weights_for_kenya_cpi_febuary_base_2009(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Group Weights for Kenya CPI February Base Period 2009"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject descObject  = response.getJSONObject(0);
                            JSONObject householdObject  = response.getJSONObject(1);
                            JSONObject weightObject  = response.getJSONObject(2);

                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray householdArray = householdObject.getJSONArray("data");
                            JSONArray weightArray = weightObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("cpi_group_weights_for_kenya_cpi_febuary_base_2009",null,null);

                            for(int i=0;i<descArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("group_weights_id",i+1);
                                values.put("description",descArray.getString(i));
                                values.put("household",householdArray.getString(i));
                                values.put("group_weights",Float.parseFloat(String.valueOf(weightArray.get(i))));

                                success = db.insertOrThrow("cpi_group_weights_for_kenya_cpi_febuary_base_2009",null,values);
                            }

                            db.close();
                            Log.d(TAG, "cpi_group_weights_for_kenya_cpi_febuary_base_2009: " + success);

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

    private void insertInto_cpi_group_weights_for_kenya_cpi_october_base_1997(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Group Weights for Kenya CPI October Base Period 1997"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject itemObject  = response.getJSONObject(0);
                            JSONObject householdObject  = response.getJSONObject(1);
                            JSONObject weightObject  = response.getJSONObject(2);

                            JSONArray itemArray = itemObject.getJSONArray("data");
                            JSONArray householdArray = householdObject.getJSONArray("data");
                            JSONArray weightArray = weightObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("cpi_group_weights_for_kenya_cpi_october_base_1997",null,null);

                            for(int i=0;i<itemArray.length();i++){
                                ContentValues values = new ContentValues();

                                //values.put("group_weights_id",i+1);
                                values.put("item_group",itemArray.getString(i));
                                values.put("household",householdArray.getString(i));
                                values.put("group_weights",Float.parseFloat(String.valueOf(weightArray.get(i))));

                                success = db.insertOrThrow("cpi_group_weights_for_kenya_cpi_october_base_1997",null,values);
                            }

                            db.close();
                            Log.d(TAG, "cpi_group_weights_for_kenya_cpi_october_base_1997: " + success);

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