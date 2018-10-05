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

public class DatabaseManufacturingApi {
    private Context context;
    public DatabaseManufacturingApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        insertInto_manufacturing_quantum_indices_of_manufacturing_production(d);
        insertInto_manufacturing_per_change_in_quantum_indices_of_man_production(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_manufacturing_quantum_indices_of_manufacturing_production(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/manufacturing/all_quantum_indices_of_manufacturing_production",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject commodityObject  = response.getJSONObject(1);
                            JSONObject indiceObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray commodityArray = commodityObject.getJSONArray("data");
                            JSONArray indiceArray = indiceObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("manufacturing_quantum_indices_of_manufacturing_production",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("quantum_indice_id",i+1);
                                values.put("commodity",commodityArray.getString(i));
                                values.put("quantum_indice",indiceArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("manufacturing_quantum_indices_of_manufacturing_production",null,values);
                            }

                            db.close();
                            Log.d(TAG, "manufacturing_quantum_indices_of_manufacturing_production: " + success);

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

    private void insertInto_manufacturing_per_change_in_quantum_indices_of_man_production(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/manufacturing/all_per_change_in_quantum_indices_of_man_production",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject commodityObject  = response.getJSONObject(1);
                            JSONObject perChangeObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray commodityArray = commodityObject.getJSONArray("data");
                            JSONArray perChangeArray = perChangeObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("manufacturing_per_change_in_quantum_indices_of_man_production",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("percentage_change_id",i+1);
                                values.put("commodity",commodityArray.getString(i));
                                values.put("percentage_change",perChangeArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("manufacturing_per_change_in_quantum_indices_of_man_production",null,values);
                            }

                            db.close();
                            Log.d(TAG, "manufacturing_per_change_in_quantum_indices_of_man_production: " + success);

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