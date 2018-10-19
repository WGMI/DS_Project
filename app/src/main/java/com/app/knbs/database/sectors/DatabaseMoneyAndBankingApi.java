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

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseMoneyAndBankingApi {
    private Context context;
    public DatabaseMoneyAndBankingApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);

    public void loadData(final ProgressDialog d){
        insertInto_money_and_banking_nairobi_securities_exchange(d);
        insertInto_money_and_banking_monetary_indicators_domestic_credit(d);
        insertInto_money_and_banking_monetary_indicators_broad_money_supply(d);
        insertInto_money_and_banking_inflation_rates(d);
        insertInto_money_and_banking_commercial_banks_bills_loans_advances(d);

        insertInto_money_and_banking_interest_rates(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_money_and_banking_nairobi_securities_exchange(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Nairobi Securities Exchange (NSE) 20 Share Index"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject monthObject  = response.getJSONObject(1);
                            JSONObject nseObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray monArray = monthObject.getJSONArray("data");
                            JSONArray nseArray = nseObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("money_and_banking_nairobi_securities_exchange",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("nse_id",i+1);
                                values.put("month_id",0);
                                values.put("nse_20_share_index",nseArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("money_and_banking_nairobi_securities_exchange",null,values);
                            }

                            db.close();
                            Log.d(TAG, "money_and_banking_nairobi_securities_exchange: " + success);

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

    private void insertInto_money_and_banking_monetary_indicators_domestic_credit(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Domestic Credit"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject prvObject  = response.getJSONObject(1);
                            JSONObject natObject  = response.getJSONObject(2);
                            JSONObject totalObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray prvArray = prvObject.getJSONArray("data");
                            JSONArray natArray = natObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("money_and_banking_monetary_indicators_domestic_credit",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("year",yearArray.getInt(i));
                                values.put("domestic_credit_id",i+1);
                                values.put("private_and_other_public_bodies",prvArray.getInt(i));
                                values.put("national_government",natArray.getInt(i));
                                values.put("national_government",natArray.getInt(i));
                                values.put("total",totalArray.getInt(i));

                                success = db.insertOrThrow("money_and_banking_monetary_indicators_domestic_credit",null,values);
                            }

                            db.close();
                            Log.d(TAG, "money_and_banking_monetary_indicators_domestic_credit: " + success);

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

    private void insertInto_money_and_banking_monetary_indicators_broad_money_supply(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Broad Money Supply"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject suppObject  = response.getJSONObject(1);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray suppArray = suppObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("money_and_banking_monetary_indicators_broad_money_supply",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("broad_money_supply_id",i+1);
                                values.put("year",yearArray.getInt(i));
                                values.put("broad_money_supply",suppArray.getInt(i));

                                success = db.insertOrThrow("money_and_banking_monetary_indicators_broad_money_supply",null,values);
                            }

                            db.close();
                            Log.d(TAG, "money_and_banking_monetary_indicators_broad_money_supply: " + success);

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

    private void insertInto_money_and_banking_inflation_rates(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Inflation Rates"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject infObject  = response.getJSONObject(1);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray infArray = infObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("money_and_banking_inflation_rates",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("inflation_rate_id",i+1);
                                values.put("year",yearArray.getInt(i));
                                values.put("inflation_rate",infArray.getDouble(i));

                                success = db.insertOrThrow("money_and_banking_inflation_rates",null,values);
                            }

                            db.close();
                            Log.d(TAG, "money_and_banking_inflation_rates: " + success);

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

    private void insertInto_money_and_banking_commercial_banks_bills_loans_advances(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Commercial Banks Bills, Loans and Advances"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject monObject  = response.getJSONObject(1);
                            JSONObject secObject  = response.getJSONObject(2);
                            JSONObject subObject  = response.getJSONObject(3);
                            JSONObject amObject  = response.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray monArray = monObject.getJSONArray("data");
                            JSONArray secArray = secObject.getJSONArray("data");
                            JSONArray subArray = subObject.getJSONArray("data");
                            JSONArray amArray = amObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("money_and_banking_commercial_banks_bills_loans_advances",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("bills_loans_advances_id",i+1);
                                values.put("sector",secArray.getString(i));
                                values.put("sub_sector",subArray.getString(i));
                                values.put("month_id",monArray.getString(i));
                                values.put("amount",amArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("money_and_banking_commercial_banks_bills_loans_advances",null,values);
                            }

                            db.close();
                            Log.d(TAG, "money_and_banking_commercial_banks_bills_loans_advances: " + success);

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

    private void insertInto_money_and_banking_interest_rates(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Interest Rates"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject monObject  = response.getJSONObject(1);
                            JSONObject loanObject  = response.getJSONObject(2);
                            JSONObject depObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray monArray = monObject.getJSONArray("data");
                            JSONArray loanArray = loanObject.getJSONArray("data");
                            JSONArray depArray = depObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("money_and_banking_interest_rates",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("interest_rates_id",i+1);
                                values.put("bank_loans_and_advances_weighted_average_rates",loanArray.getDouble(i));
                                values.put("average_deposit_rate",depArray.getDouble(i));
                                values.put("month_id",0);
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("money_and_banking_interest_rates",null,values);
                            }

                            db.close();
                            Log.d(TAG, "money_and_banking_interest_rates: " + success);

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