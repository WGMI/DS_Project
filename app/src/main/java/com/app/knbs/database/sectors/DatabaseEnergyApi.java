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

public class DatabaseEnergyApi {
    private Context context;
    public DatabaseEnergyApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);

    public void loadData(final ProgressDialog d){
        insertInto_energy_average_retail_prices_of_selected_petroleum_products(d);
        insertInto_energy_net_domestic_sale_of_petroleum_fuels_by_consumer_category(d);
        insertInto_energy_electricity_demand_and_supply(d);
        insertInto_energy_petroleum_supply_and_demand(d);
        insertInto_energy_value_and_quantity_of_imports_exports(d);

        insertInto_energy_averagemonthlypumppricesforfuelbycategory(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_energy_average_retail_prices_of_selected_petroleum_products(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Average Retail Prices of Selected Petroleum Products"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject productsObject  = response.getJSONObject(1);
                            JSONObject priceObject  = response.getJSONObject(2);
                            JSONObject monthObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray productArray = productsObject.getJSONArray("data");
                            JSONArray priceArray = priceObject.getJSONArray("data");
                            JSONArray monthArray = monthObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("energy_average_retail_prices_of_selected_petroleum_products",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("retail_price_id",i+1);
                                values.put("petroleum_product",productArray.getString(i));
                                values.put("retail_price_ksh",priceArray.getDouble(i));
                                values.put("month",monthArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("energy_average_retail_prices_of_selected_petroleum_products",null,values);
                            }

                            db.close();
                            Log.d(TAG, "energy_average_retail_prices_of_selected_petroleum_products: " + success);

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

    private void insertInto_energy_net_domestic_sale_of_petroleum_fuels_by_consumer_category(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Net Domestic Sale of Petroleum Fuels by Consumer Category"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject userObject  = response.getJSONObject(1);
                            JSONObject quantityObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray userArray = userObject.getJSONArray("data");
                            JSONArray quantityArray = quantityObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("energy_net_domestic_sale_of_petroleum_fuels_by_consumer_category",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("domestic_sale_id",i+1);
                                values.put("user",userArray.getString(i));
                                values.put("quantity_tonnes",quantityArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("energy_net_domestic_sale_of_petroleum_fuels_by_consumer_category",null,values);
                            }

                            db.close();
                            Log.d(TAG, "energy_net_domestic_sale_of_petroleum_fuels_by_consumer_category: " + success);

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

    private void insertInto_energy_electricity_demand_and_supply(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Electricity Demand and Supply"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject dAndSObject  = response.getJSONObject(1);
                            JSONObject capacityObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray dAndSArray = dAndSObject.getJSONArray("data");
                            JSONArray capacityArray = capacityObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("energy_electricity_demand_and_supply",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("electricity_id",i+1);
                                values.put("demand_supply",dAndSArray.getString(i));
                                values.put("capacity_megawatts",capacityArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("energy_electricity_demand_and_supply",null,values);
                            }

                            db.close();
                            Log.d(TAG, "energy_electricity_demand_and_supply: " + success);

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

    //Incomplete API
    private void insertInto_energy_installed_and_effective_capacity_of_electricity(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Installed and Effective Capacity of Electricity"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject typeObject  = response.getJSONObject(1);
                            JSONObject sourceObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray typeArray = typeObject.getJSONArray("data");
                            JSONArray sourceArray = sourceObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("energy_installed_and_effective_capacity_of_electricity",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("capacity_id",i+1);
                                //values.put("type",dAndSArray.getString(i));
                                //values.put("electricity_source",capacityArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("energy_installed_and_effective_capacity_of_electricity",null,values);
                            }

                            db.close();
                            Log.d(TAG, "energy_installed_and_effective_capacity_of_electricity: " + success);

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

    private void insertInto_energy_petroleum_supply_and_demand(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Petroleum Supply and Demand"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject typeObject  = response.getJSONObject(1);
                            JSONObject productObject  = response.getJSONObject(2);
                            JSONObject quantityObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray typeArray = typeObject.getJSONArray("data");
                            JSONArray productArray = productObject.getJSONArray("data");
                            JSONArray quantityArray = quantityObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("energy_petroleum_supply_and_demand",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("petroleum_id",i+1);
                                values.put("type",typeArray.getString(i));
                                values.put("petroleum_product",productArray.getString(i));
                                values.put("quantity_tonnes",quantityArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("energy_petroleum_supply_and_demand",null,values);
                            }

                            db.close();
                            Log.d(TAG, "energy_petroleum_supply_and_demand: " + success);

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

    private void insertInto_energy_value_and_quantity_of_imports_exports(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject typeObject  = response.getJSONObject(1);
                            JSONObject productObject  = response.getJSONObject(2);
                            JSONObject quantityObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray typeArray = typeObject.getJSONArray("data");
                            JSONArray productArray = productObject.getJSONArray("data");
                            JSONArray quantityArray = quantityObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("energy_value_and_quantity_of_imports_exports",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("petroleum_id",i+1);
                                values.put("type",typeArray.getString(i));
                                values.put("petroleum_product",productArray.getString(i));
                                values.put("quantity_tonnes",quantityArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("energy_value_and_quantity_of_imports_exports",null,values);
                            }

                            db.close();
                            Log.d(TAG, "energy_value_and_quantity_of_imports_exports: " + success);

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

    private void insertInto_energy_averagemonthlypumppricesforfuelbycategory(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Average Monthly Pump Prices For Fuel By Category"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject superObject  = response.getJSONObject(1);
                            JSONObject dieselObject  = response.getJSONObject(2);
                            JSONObject keroseneObject  = response.getJSONObject(3);
                            JSONObject countiesObject  = response.getJSONObject(4);
                            JSONObject monthObject  = response.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray superArray = superObject.getJSONArray("data");
                            JSONArray dieArray = dieselObject.getJSONArray("data");
                            JSONArray kerArray = keroseneObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray monthArray = monthObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("energy_averagemonthlypumppricesforfuelbycategory",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("count_id",i+1);
                                values.put("county_id",new CountyHelper().getCountyId(countiesArray.getString(i)));
                                values.put("month_id",1);
                                values.put("super_petrol",superArray.getDouble(i));
                                values.put("diesel",dieArray.getDouble(i));
                                values.put("kerosene",kerArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("energy_averagemonthlypumppricesforfuelbycategory",null,values);
                            }

                            db.close();
                            Log.d(TAG, "energy_averagemonthlypumppricesforfuelbycategory: " + success);

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