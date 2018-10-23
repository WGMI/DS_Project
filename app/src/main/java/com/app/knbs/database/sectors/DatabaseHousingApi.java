
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
        /*insertInto_housing_conditions_kihibs_waste_disposal_method(d);
        insertInto_housing_conditions_kihibs_volume_of_water_used(d);
        insertInto_housing_conditions_kihibs_time_taken_to_fetch_drinking_water(d);
        insertInto_housing_conditions_kihibs_sharing_of_toilet_facility(d);
        insertInto_housing_conditions_kihibs_place_for_washing_hands_near_toilet(d);

        insertInto_housing_conditions_kihibs_owner_occupier_dwellings(d);
        insertInto_housing_conditions_kihibs_methods_used_to_make_water_safer(d);
        insertInto_housing_conditions_kihibs_hholds_by_habitable_rooms(d);
        insertInto_housing_conditions_kihibs_hholds_by_housing_tenure(d);
        insertInto_housing_conditions_kihibs_hholds_by_type_of_housing_unit(d);

        insertInto_housing_conditions_kihibs_primary_type_of_cooking_appliance(d);
        insertInto_housing_conditions_kihibs_hholds_in_rented_dwellings(d);
        insertInto_housing_conditions_kihibs_main_floor_material(d);
        insertInto_housing_conditions_kihibs_main_roofing_material(d);
        insertInto_housing_conditions_kihibs_main_source_of_cooking_fuel(d);

        insertInto_housing_conditions_kihibs_main_source_of_lighting_fuel(d);
        insertInto_housing_conditions_kihibs_main_toilet_facility(d);
        insertInto_housing_conditions_kihibs_main_wall_material(d);*/
        insertInto_housing_conditions_kihibs_main_source_of_drinking_water(d);
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
                        Log.d(TAG, "error: " + volleyError.toString());
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
                        Log.d(TAG, "error: " + volleyError.toString());
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
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_sharing_of_toilet_facility(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("sharing of toilet"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject shObject = array.getJSONObject(1);
                            JSONObject nshObject = array.getJSONObject(2);
                            JSONObject nsObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray shArray = shObject.getJSONArray("data");
                            JSONArray nshArray = nshObject.getJSONArray("data");
                            JSONArray nsArray = nsObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_sharing_of_toilet_facility",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("proportion_id",i+1);
                                values.put("shared_toilet",shArray.getDouble(i));
                                values.put("not_shared",nshArray.getDouble(i));
                                values.put("not_stated",nsArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_sharing_of_toilet_facility",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_sharing_of_toilet_facility: " + success);
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

    private void insertInto_housing_conditions_kihibs_place_for_washing_hands_near_toilet(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("place of washing hands near toilet"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject plObject = array.getJSONObject(1);
                            JSONObject npObject = array.getJSONObject(2);
                            JSONObject nsObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray plArray = plObject.getJSONArray("data");
                            JSONArray npArray = npObject.getJSONArray("data");
                            JSONArray nsArray = nsObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_place_for_washing_hands_near_toilet",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("place_id",i+1);
                                values.put("place",plArray.getDouble(i));
                                values.put("no_place",npArray.getDouble(i));
                                values.put("not_stated",nsArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_place_for_washing_hands_near_toilet",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_place_for_washing_hands_near_toilet: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_owner_occupier_dwellings(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("owner occupier dwellings"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject pclObject = array.getJSONObject(1);
                            JSONObject cclObject = array.getJSONObject(2);
                            JSONObject othObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray pclArray = pclObject.getJSONArray("data");
                            JSONArray cclArray = cclObject.getJSONArray("data");
                            JSONArray othArray = othObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_owner_occupier_dwellings",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("dwelling_id",i+1);
                                values.put("purchased_cash_loan",pclArray.getDouble(i));
                                values.put("constructed_cash_loan",cclArray.getDouble(i));
                                values.put("other",othArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_owner_occupier_dwellings",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_owner_occupier_dwellings: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_methods_used_to_make_water_safer(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("methods used to make water safer"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject boilObject = array.getJSONObject(1);
                            JSONObject filObject = array.getJSONObject(2);
                            JSONObject othObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray boilArray = boilObject.getJSONArray("data");
                            JSONArray filArray = filObject.getJSONArray("data");
                            JSONArray othArray = othObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_methods_used_to_make_water_safer",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("method_id",i+1);
                                values.put("boil",boilArray.getDouble(i));
                                values.put("water_filter",filArray.getDouble(i));
                                values.put("other",othArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_methods_used_to_make_water_safer",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_methods_used_to_make_water_safer: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_hholds_by_habitable_rooms(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("hhholds by habitable rooms"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject oneObject = array.getJSONObject(1);
                            JSONObject twoObject = array.getJSONObject(2);
                            JSONObject threeObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray oneArray = oneObject.getJSONArray("data");
                            JSONArray twoArray = twoObject.getJSONArray("data");
                            JSONArray threeArray = threeObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_hholds_by_habitable_rooms",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("room_id",i+1);
                                values.put("one",oneArray.getDouble(i));
                                values.put("two",twoArray.getDouble(i));
                                values.put("three",threeArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_hholds_by_habitable_rooms",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_hholds_by_habitable_rooms: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_hholds_by_housing_tenure(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("hholds by housing tenure"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject ownObject = array.getJSONObject(1);
                            JSONObject rentObject = array.getJSONObject(2);
                            JSONObject norentObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray ownArray = ownObject.getJSONArray("data");
                            JSONArray rentArray = rentObject.getJSONArray("data");
                            JSONArray norentArray = norentObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_hholds_by_housing_tenure",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("tenure_id",i+1);
                                values.put("owner_occupier",ownArray.getDouble(i));
                                values.put("pays_rent",rentArray.getDouble(i));
                                values.put("no_rent_consent",norentArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_hholds_by_housing_tenure",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_hholds_by_housing_tenure: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_hholds_by_type_of_housing_unit(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("hholds by type of housing unit"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject flatObject = array.getJSONObject(2);
                            JSONObject maisObject = array.getJSONObject(3);
                            JSONObject shObject = array.getJSONObject(5);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray flArray = flatObject.getJSONArray("data");
                            JSONArray maisArray = maisObject.getJSONArray("data");
                            JSONArray shArray = shObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_hholds_by_type_of_housing_unit",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("unit_id",i+1);
                                values.put("flat",flArray.getDouble(i));
                                values.put("maisonnette",maisArray.getDouble(i));
                                values.put("shanty",shArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_hholds_by_type_of_housing_unit",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_hholds_by_type_of_housing_unit: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_primary_type_of_cooking_appliance(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("primary type of cooking appliance"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject jkObject = array.getJSONObject(4);
                            JSONObject gsObject = array.getJSONObject(6);
                            JSONObject elObject = array.getJSONObject(8);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray jkArray = jkObject.getJSONArray("data");
                            JSONArray gsArray = gsObject.getJSONArray("data");
                            JSONArray elArray = elObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_primary_type_of_cooking_appliance",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("appliance_id",i+1);
                                values.put("ordinary_jiko",jkArray.getDouble(i));
                                values.put("gas_cooker",gsArray.getDouble(i));
                                values.put("electric_cooker",elArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_primary_type_of_cooking_appliance",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_primary_type_of_cooking_appliance: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_hholds_in_rented_dwellings(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("hholds in rented dwellings"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject govtObject = array.getJSONObject(1);
                            JSONObject coObject = array.getJSONObject(4);
                            JSONObject indObject = array.getJSONObject(6);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray govtArray = govtObject.getJSONArray("data");
                            JSONArray coArray = coObject.getJSONArray("data");
                            JSONArray indArray = indObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_hholds_in_rented_dwellings",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("dwelling_id",i+1);
                                values.put("govt_national",govtArray.getDouble(i));
                                values.put("company_directly",coArray.getDouble(i));
                                values.put("individual_directly",indArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_hholds_in_rented_dwellings",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_hholds_in_rented_dwellings: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_main_floor_material(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("main floor material"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject tileObject = array.getJSONObject(1);
                            JSONObject cemObject = array.getJSONObject(2);
                            JSONObject woodObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray tlArray = tileObject.getJSONArray("data");
                            JSONArray cemArray = cemObject.getJSONArray("data");
                            JSONArray woodArray = woodObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_main_floor_material",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("material_id",i+1);
                                values.put("tiles",tlArray.getDouble(i));
                                values.put("cement",cemArray.getDouble(i));
                                values.put("wood",woodArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_main_floor_material",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_main_floor_material: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_main_roofing_material(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("main roofing material"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject tileObject = array.getJSONObject(7);
                            JSONObject cemObject = array.getJSONObject(6);
                            JSONObject mudObject = array.getJSONObject(2);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray tlArray = tileObject.getJSONArray("data");
                            JSONArray cemArray = cemObject.getJSONArray("data");
                            JSONArray mudArray = mudObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_main_roofing_material",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("material_id",i+1);
                                values.put("tiles",tlArray.getDouble(i));
                                values.put("concrete",cemArray.getDouble(i));
                                values.put("mud",mudArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_main_roofing_material",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_main_roofing_material: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_main_source_of_cooking_fuel(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("main source of cooking fuel"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject frObject = array.getJSONObject(1);
                            JSONObject elObject = array.getJSONObject(2);
                            JSONObject bioObject = array.getJSONObject(4);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray frArray = frObject.getJSONArray("data");
                            JSONArray elArray = elObject.getJSONArray("data");
                            JSONArray bioArray = bioObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_main_source_of_cooking_fuel",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("source_id",i+1);
                                values.put("firewood",frArray.getDouble(i));
                                values.put("electricity",elArray.getDouble(i));
                                values.put("biogas",bioArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_main_source_of_cooking_fuel",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_main_source_of_cooking_fuel: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_main_source_of_drinking_water(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("main source of drinking water"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject pdObject = array.getJSONObject(1);
                            JSONObject rwObject = array.getJSONObject(7);
                            JSONObject vdObject = array.getJSONObject(12);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray pdArray = pdObject.getJSONArray("data");
                            JSONArray rwArray = rwObject.getJSONArray("data");
                            JSONArray vdArray = vdObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_main_source_of_drinking_water",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("source_id",i+1);
                                values.put("piped_dwelling",pdArray.getDouble(i));
                                values.put("rain_water",rwArray.getDouble(i));
                                values.put("vendor_truck",vdArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_main_source_of_drinking_water",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_main_source_of_drinking_water: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error:" + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_main_source_of_lighting_fuel(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("main source of ligthing fuel"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject elObject = array.getJSONObject(1);
                            JSONObject gsObject = array.getJSONObject(2);
                            JSONObject soObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray elArray = elObject.getJSONArray("data");
                            JSONArray gsArray = gsObject.getJSONArray("data");
                            JSONArray soArray = soObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_main_source_of_lighting_fuel",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("source_id",i+1);
                                values.put("electricity",elArray.getDouble(i));
                                values.put("generator",gsArray.getDouble(i));
                                values.put("solar_energy",soArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_main_source_of_lighting_fuel",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_main_source_of_lighting_fuel: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_main_toilet_facility(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("main toilet facility"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject psObject = array.getJSONObject(1);
                            JSONObject stObject = array.getJSONObject(2);
                            JSONObject ltObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray psArray = psObject.getJSONArray("data");
                            JSONArray stArray = stObject.getJSONArray("data");
                            JSONArray ltArray = ltObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_main_toilet_facility",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("facility_id",i+1);
                                values.put("piped_sewer",psArray.getDouble(i));
                                values.put("septic_tank",stArray.getDouble(i));
                                values.put("latrine",ltArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_main_toilet_facility",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_main_toilet_facility: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_housing_conditions_kihibs_main_wall_material(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("main wall material"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject lsObject = array.getJSONObject(1);
                            JSONObject brObject = array.getJSONObject(2);
                            JSONObject cmObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray lsArray = lsObject.getJSONArray("data");
                            JSONArray brArray = brObject.getJSONArray("data");
                            JSONArray cmArray = cmObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("housing_conditions_kihibs_main_wall_material",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("material_id",i+1);
                                values.put("lime_stone",lsArray.getDouble(i));
                                values.put("bricks",brArray.getDouble(i));
                                values.put("cement_blocks",cmArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("housing_conditions_kihibs_main_wall_material",null,values);
                            }

                            db.close();
                            Log.d(TAG, "housing_conditions_kihibs_main_wall_material: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

}