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

public class DatabaseTradeCommerceApi {
    private Context context;
    public DatabaseTradeCommerceApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        insertInto_trade_and_commerce_values_of_principal_imports(d);
        insertInto_trade_and_commerce_values_of_principal_domestic_exports(d);
        insertInto_trade_and_commerce_balance_of_trade(d);
        insertInto_trade_and_commerce_quantities_of_principle_imports(d);
        insertInto_trade_and_commerce_quantities_of_principle_domestic_exports(d);
        insertInto_trade_and_commerce_value_of_total_exports_european_union(d);
        insertInto_trade_and_commerce_value_total_exports_east_africa_communities(d);
        insertInto_trade_and_commerce_value_of_total_exports_all_destinations(d);
        insertInto_trade_and_commerce_import_trade_africa_countries(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_trade_and_commerce_values_of_principal_imports(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_values_of_principal_imports",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject descObject  = response.getJSONObject(1);
                            JSONObject qtyObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray qtyArray = qtyObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_values_of_principal_imports",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("trade_id",i+1);
                                values.put("description",descArray.getString(i));
                                values.put("quantity",qtyArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_values_of_principal_imports",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_values_of_principal_imports: " + success);

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

    private void insertInto_trade_and_commerce_values_of_principal_domestic_exports(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_values_of_principal_domestic_exports",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject descObject  = response.getJSONObject(1);
                            JSONObject valObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_values_of_principal_domestic_exports",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("trade_id",i+1);
                                values.put("description",descArray.getString(i));
                                values.put("total_values",valArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_values_of_principal_domestic_exports",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_values_of_principal_domestic_exports: " + success);

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

    private void insertInto_trade_and_commerce_balance_of_trade(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_balance_of_trade",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject descObject  = response.getJSONObject(1);
                            JSONObject valObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_balance_of_trade",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("trade_id",i+1);
                                values.put("description",descArray.getString(i));
                                values.put("amount_in_millions",valArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_balance_of_trade",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_balance_of_trade: " + success);

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

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_trade_and_commerce_quantities_of_principle_imports(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_quantities_principal_imports",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject commodityObject  = response.getJSONObject(1);
                            JSONObject unitObject  = response.getJSONObject(2);
                            JSONObject qtyObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray commodityArray = commodityObject.getJSONArray("data");
                            JSONArray unitArray = unitObject.getJSONArray("data");
                            JSONArray qtyArray = qtyObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_quantities_principal_imports",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("trade_id",i+1);
                                values.put("commodity",commodityArray.getString(i));
                                values.put("quantity",qtyArray.getInt(i));
                                values.put("unit_of_quantity",unitArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_quantities_principal_imports",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_quantities_principal_imports: " + success);

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

    private void insertInto_trade_and_commerce_quantities_of_principle_domestic_exports(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_quantities_principal_domestic_exports",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject descObject  = response.getJSONObject(1);
                            JSONObject qtyObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray qtyArray = qtyObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_quantities_principal_domestic_exports",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("trade_id",i+1);
                                values.put("description",descArray.getString(i));
                                values.put("quantity",qtyArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_quantities_principal_domestic_exports",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_quantities_principal_domestic_exports: " + success);

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

    private void insertInto_trade_and_commerce_value_of_total_exports_european_union(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_value_of_total_exports_european_union",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countryObject  = response.getJSONObject(1);
                            JSONObject valObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countryArray = countryObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_value_of_total_exports_european_union",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("export_id",i+1);
                                values.put("country",countryArray.getString(i));
                                values.put("value_in_millions",valArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_value_of_total_exports_european_union",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_value_of_total_exports_european_union: " + success);

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

    private void insertInto_trade_and_commerce_value_total_exports_east_africa_communities(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_value_total_exports_east_africa_communities",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countryObject  = response.getJSONObject(1);
                            JSONObject valObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countryArray = countryObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_value_total_exports_east_africa_communities",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("export_id",i+1);
                                values.put("country",countryArray.getString(i));
                                values.put("value_in_millions",valArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_value_total_exports_east_africa_communities",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_value_total_exports_east_africa_communities: " + success);

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

    private void insertInto_trade_and_commerce_value_of_total_exports_all_destinations(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_value_of_total_exports_all_destinations",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject regionObject  = response.getJSONObject(1);
                            JSONObject countryObject  = response.getJSONObject(2);
                            JSONObject valObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray regionArray = regionObject.getJSONArray("data");
                            JSONArray countryArray = countryObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_value_of_total_exports_all_destinations",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("export_id",i+1);
                                values.put("region",regionArray.getString(i));
                                values.put("country",countryArray.getString(i));
                                values.put("value_in_millions",valArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_value_of_total_exports_all_destinations",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_value_of_total_exports_all_destinations: " + success);

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

    private void insertInto_trade_and_commerce_import_trade_africa_countries(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/trade/all_trade_and_commerce_import_trade_africa_countries",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject zonesObject  = response.getJSONObject(1);
                            JSONObject countryObject  = response.getJSONObject(2);
                            JSONObject valObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray zonesArray = zonesObject.getJSONArray("data");
                            JSONArray countryArray = countryObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("trade_and_commerce_import_trade_africa_countries",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("import_id",i+1);
                                values.put("zones",zonesArray.getString(i));
                                values.put("country",countryArray.getString(i));
                                values.put("total_values",valArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("trade_and_commerce_import_trade_africa_countries",null,values);
                            }

                            db.close();
                            Log.d(TAG, "trade_and_commerce_import_trade_africa_countries: " + success);

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